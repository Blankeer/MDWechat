package com.blanke.mdwechat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
        actionMenu.initMenuButton();

        FloatingActionButton fab = new FloatingActionButton(context);
        fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_accessible_black_24dp));
        fab.setColorNormal(primaryColor);
        fab.setButtonSize(SIZE_MINI);
        fab.setLabelText("扫一扫");
        actionMenu.addMenuButton(fab);
        fab.setLabelColors(primaryColor, primaryColor, primaryColor);

        FloatingActionButton fab2 = new FloatingActionButton(context);
        fab2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_3d_rotation_red_800_24dp));
        fab2.setColorNormal(primaryColor);
        fab2.setButtonSize(SIZE_MINI);
        fab2.setLabelText("好友");
        actionMenu.addMenuButton(fab2);
        fab2.setLabelColors(primaryColor, primaryColor, primaryColor);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 8;
        params.bottomMargin = 8;
        params.gravity = Gravity.END | Gravity.BOTTOM;
        contentView.addView(actionMenu, params);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
