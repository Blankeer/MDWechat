package com.blanke.mdwechat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blanke.mdwechat.util.ConvertUtils;
import com.blanke.mdwechat.widget.MaterialSearchView;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;

public class MainActivity extends Activity {
    private FrameLayout contentView;
    private MaterialSearchView materialSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentView = (FrameLayout) findViewById(R.id.main_content);
        materialSearchView = (MaterialSearchView) findViewById(R.id.searchView);
        addFab(contentView);
        addTabLayout(contentView);
    }

    private void addFab(FrameLayout contentView) {
        int primaryColor = getResources().getColor(R.color.colorPrimary);
        Context context = this;
        FloatingActionMenu actionMenu = new FloatingActionMenu(context);
        actionMenu.setMenuButtonColorNormal(primaryColor);
        actionMenu.setMenuButtonColorPressed(primaryColor);
        actionMenu.setMenuIcon(ContextCompat.getDrawable(context, R.drawable.ic_add));
        actionMenu.initMenuButton();

        actionMenu.setFloatButtonClickListener(new FloatingActionMenu.FloatButtonClickListener() {
            @Override
            public void onClick(FloatingActionButton fab, int index) {
                Log.d("click ", "fab=" + fab + ",index=" + index + ",label" + fab.getLabelText());
            }
        });

        addFloatButton(actionMenu, context, "扫一扫", R.drawable.ic_scan, primaryColor);
        addFloatButton(actionMenu, context, "收付款", R.drawable.ic_money, primaryColor);
        addFloatButton(actionMenu, context, "群聊", R.drawable.ic_chat, primaryColor);
        addFloatButton(actionMenu, context, "添加好友", R.drawable.ic_friend_add, primaryColor);


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

    private void addTabLayout(ViewGroup contentLayout) {
        Context context = this;
        final CommonTabLayout tabLayout = new CommonTabLayout(context);
        int color = getResources().getColor(R.color.colorPrimary);
        tabLayout.setBackgroundColor(color);
        tabLayout.setTextSelectColor(Color.WHITE);
        tabLayout.setTextUnselectColor(0x1acccccc);
        tabLayout.setIndicatorHeight(ConvertUtils.dp2px(this, 1F));
        tabLayout.setIndicatorColor(Color.WHITE);
        tabLayout.setIndicatorCornerRadius(ConvertUtils.dp2px(this, 2));
        tabLayout.setIndicatorAnimDuration(200);
        tabLayout.setElevation(5);
        tabLayout.setTextsize(context.getResources().getDimension(R.dimen.tabTextSize));
        tabLayout.setUnreadBackground(Color.WHITE);
        tabLayout.setUnreadTextColor(color);
        tabLayout.setSelectIconColor(Color.WHITE);
        tabLayout.setUnSelectIconColor(0x1acccccc);

        String[] titles = {"消息", "通讯录", "朋友圈", "设置"};
        final int[] tabIcons = {R.drawable.ic_chat_tab, R.drawable.ic_contact_tab,
                R.drawable.ic_explore_tab, R.drawable.ic_person_tab};
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        for (int i = 0; i < tabIcons.length; i++) {
            final int finalI = i;
            mTabEntities.add(new CustomTabEntity() {
                @Override
                public String getTabTitle() {
                    return null;
                }

                @Override
                public int getTabSelectedIcon() {
                    return tabIcons[finalI];
                }

                @Override
                public int getTabUnselectedIcon() {
                    return R.drawable.ic_scan;
                }
            });
        }
        tabLayout.setTabData(mTabEntities);

        tabLayout.showMsg(0, 5);
        tabLayout.showMsg(1, -1);
        tabLayout.showMsg(2, 0);
        tabLayout.showMsg(3, 88);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = ConvertUtils.dp2px(this, 48);
        params.topMargin = 400;
        contentLayout.addView(tabLayout, params);
    }

    public void clickButton(View view) {
        materialSearchView.show();
    }
}
