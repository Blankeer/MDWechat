package com.blanke.mdwechat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.blanke.mdwechat.util.ConvertUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;

public class MainActivity extends Activity {
    private FrameLayout contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentView = (FrameLayout) findViewById(R.id.main_content);
        addFab(contentView);
    }

    private void addFab(FrameLayout contentView) {
        int primaryColor = getResources().getColor(R.color.colorPrimary);
        Context context = this;
        FloatingActionMenu actionMenu = new FloatingActionMenu(context);
        actionMenu.setMenuButtonColorNormal(primaryColor);
        actionMenu.setMenuButtonColorPressed(primaryColor);
        actionMenu.setMenuIcon(ContextCompat.getDrawable(context,R.drawable.ic_add));
        actionMenu.initMenuButton();


        addFloatButton(actionMenu,context,"扫一扫",R.drawable.ic_scan,primaryColor);
        addFloatButton(actionMenu,context,"收付款",R.drawable.ic_money,primaryColor);
        addFloatButton(actionMenu,context,"群聊",R.drawable.ic_chat,primaryColor);
        addFloatButton(actionMenu,context,"添加好友",R.drawable.ic_friend_add,primaryColor);


        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = ConvertUtils.dp2px(context, 12);
        params.rightMargin = margin;
        params.bottomMargin = margin;
        params.gravity = Gravity.END | Gravity.BOTTOM;
        contentView.addView(actionMenu, params);
    }
    private void addFloatButton(FloatingActionMenu actionMenu, Context context, String label, int drawable, int primaryColor) {
        FloatingActionButton fab = new FloatingActionButton(context);
        fab.setImageDrawable(ContextCompat.getDrawable(context, drawable));
        fab.setColorNormal(primaryColor);
        fab.setColorPressed(primaryColor);
        fab.setButtonSize(SIZE_MINI);
        fab.setLabelText(label);
        actionMenu.addMenuButton(fab);
        fab.setLabelColors(primaryColor, primaryColor, primaryColor);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
