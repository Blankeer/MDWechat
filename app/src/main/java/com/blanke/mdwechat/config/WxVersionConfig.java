package com.blanke.mdwechat.config;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by blanke on 2017/8/25.
 */

public class WxVersionConfig {
    public String version;
    public Classes classes;
    public Fields fields;
    public Methods methods;
    public Views views;

    public static WxVersionConfig loadConfig(Context context, String wxVersion) throws IOException {
        String configName = wxVersion + ".config";
        InputStream is = context.getAssets().open(configName);
        Gson gson = new Gson();
        return gson.fromJson(new InputStreamReader(is), WxVersionConfig.class);
    }

    public static class Classes {
        public String LauncherUI;
        public String HomeUI;
        public String MenuAdapterManager;
        public String MMFragmentActivity;
        public String AvatarUtils;
        public String TouchImageView;
        public String ChattingUIActivity;
        public String ChattingUIFragment;
        public String MenuItemViewHolderWrapper;
        public String MenuItemViewHolder;
        public String FTSMainUI;
        public String HomeUiViewPagerChangeListener;
        public String WxViewPager;
        public String LauncherUIBottomTabView;
        public String ActionBarContainer;
        public String ToolbarWidgetWrapper;
        public String ActionBarSearchView;
        public String ActionBarEditText;
    }

    public static class Fields {
        public String LauncherUI_mHomeUi;
        public String HomeUI_mMenuAdapterManager;
        public String HomeUI_mMoreMenuItem;
        public String MenuAdapterManager_mMenuArray;
        public String MenuAdapterManager_mMenuMapping;
        public String ActionBarContainer_mBackground;
        public String LauncherUIBottomTabView_mTabClickListener;
    }

    public static class Methods {
        public String LauncherUI_startMainUI;
        public String HomeUI_startSearch;
        public String AvatarUtils_getAvatarBitmap;
        public String TouchImageView_init;
        public String HomeUiViewPagerChangeListener_onPageScrolled;
        public String HomeUiViewPagerChangeListener_onPageSelected;
        public String WxViewPager_setCurrentItem;
        public String LauncherUIBottomTabView_setMainTabUnread;
        public String LauncherUIBottomTabView_setContactTabUnread;
        public String LauncherUIBottomTabView_setFriendTabUnread;
        public String LauncherUIBottomTabView_showFriendTabUnreadPoint;
        public String ActionBarSearchView_init;
        public String MainTabClickListener_onDoubleClick;
    }

    public static class Views {
        public String LauncherUI_MainViewPager;
        public String ActionBarContainer;
        public String ActionBar_Divider;
        public String ActionBar_BackImageView;
        public String SearchActionBar_Divider;
        public String SearchActionBar_BackImageView;
    }
}
