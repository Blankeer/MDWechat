package com.blanke.mdwechat.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.blanke.mdwechat.util.ImageHelper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.WCId.Conversation_ListView_Item_Avatar_Id;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by blanke on 2017/8/1.
 */

public class AvatarHook extends BaseHookUi {
    private final static String FIELD_HOOKAVATAR = "hookAvatar";
    private Paint paint;
    private String[] avatarIds = {
            Conversation_ListView_Item_Avatar_Id
//            , Firends_ListView_Item_Avatar_Id
    };

    public AvatarHook() {
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        findAndHookMethod(ImageView.class,
                "onDraw", Canvas.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        final ImageView view = (ImageView) param.thisObject;
                        if (isAvatar(view.getContext(), view.getId())) {
                            log("afterHookedMethod");
                            Object hookAvatar = XposedHelpers.getAdditionalInstanceField(view, FIELD_HOOKAVATAR);
                            if (hookAvatar != null) {
                                Boolean hooked = (Boolean) hookAvatar;
                                if (hooked) {
                                    XposedHelpers.setAdditionalInstanceField(view, FIELD_HOOKAVATAR, Boolean.FALSE);
                                    param.setResult(null);
                                    return;
                                }
                            }
//                            log("hook view id= j9 onDraw");
//                            if (roundBitmap != null) {
//                                Canvas canvas = (Canvas) param.args[0];
//                                canvas.drawBitmap(roundBitmap, 0, 0, paint);
//                                param.setResult(null);
//                            }
//                            if (drawableAvatar != null) {
//                                Canvas canvas = (Canvas) param.args[0];
//                                canvas.save();
//                                drawableAvatar.draw(canvas);
//                                param.setResult(null);
//                            }
                            param.getResult();
                            view.setDrawingCacheEnabled(true);
                            Bitmap source = view.getDrawingCache();
                            Bitmap roundBitmap = ImageHelper.getRoundedCornerBitmap(source, view.getMeasuredHeight());
                            XposedHelpers.setAdditionalInstanceField(view, FIELD_HOOKAVATAR, Boolean.TRUE);
                            view.setImageDrawable(new BitmapDrawable(view.getResources(), roundBitmap));
                        }
                    }
                });

//        findAndHookMethod(ImageView.class,
//                "setImageDrawable", Drawable.class, new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        View view = (View) param.thisObject;
//                        if (view.getId() ==
//                                getId(view.getContext(), "j9")) {
//                            if (hookAvatar) {
//                                hookAvatar = false;
//                                return;
//                            }
//                            log("hook view id= j9 setImageDrawable");
//                            // todo 不是BitmapDrawable,而是 svg 包下的...
//                            drawableAvatar = (Drawable) param.args[0];
//                            Bitmap res = ImageHelper.getBitmapFromDrawable(drawable);
//                            roundBitmap = res;
//                            roundBitmap = ImageHelper.getRoundedCornerBitmap(res, view.getMeasuredHeight());
//                            param.args[0] = new BitmapDrawable(view.getResources(), roundBitmap);
//                            param.setResult(null);
//                        }
//                    }
//                });
    }

    private boolean isAvatar(Context context, int id) {
        for (String avatarId : avatarIds) {
            if (id == getId(context, avatarId)) {
                return true;
            }
        }
        return false;
    }
}
