package com.blanke.mdwechat.config

import java.io.IOException

class WxVersionConfig {
    lateinit var classes: Classes
    lateinit var fields: Fields
    lateinit var methods: Methods

    class Classes {
        var ActionBarContainer: String? = null
        var ActionMenuView: String? = null
        var AvatarUtils: String? = null
        var ContactFragment: String? = null
        var ConversationFragment: String? = null
        var ConversationWithAppBrandListView: String? = null
        var ConversationListView: String? = null
        var CustomViewPager: String? = null
        var DiscoverFragment: String? = null
        var HomeUI: String? = null
        var LauncherUI: String? = null
        var LauncherUIBottomTabView: String? = null
        var LauncherUIBottomTabViewItem: String? = null
        var MMFragmentActivity: String? = null
        var MainTabUI: String? = null
        var MainTabUIPageAdapter: String? = null
        var NoDrawingCacheLinearLayout: String? = null
        var NoMeasuredTextView: String? = null
        var PhoneWindow: String? = null
        var PreferenceFragment: String? = null
        var ScrollingTabContainerView: String? = null
        var SettingsFragment: String? = null
        var TabIconView: String? = null
        var ThreadExecutor: String? = null
        var WxViewPager: String? = null
        var WXCustomSchemeEntryActivity: String? = null
    }

    class Fields {
        var ContactFragment_mListView: String? = null
        var ConversationFragment_mListView: String? = null
        var HomeUI_mActionBar: String? = null
        var HomeUI_mMainTabUI: String? = null
        var LauncherUIBottomTabViewItem_mTextViews: List<String>? = null
        var LauncherUI_mHomeUI: String? = null
        var MainTabUI_mCustomViewPager: String? = null
        var PreferenceFragment_mListView: String? = null
    }

    class Methods {
        var AvatarUtils_getAvatarBitmaps: List<String>? = null
        var AvatarUtils_getDefaultAvatarBitmap: String? = null
        var ConversationWithAppBrandListView_isAppBrandHeaderEnable: String? = null
        var HomeFragment_lifecycles: List<String>? = null
        var LauncherUIBottomTabView_getTabItemView: String? = null
        var MainTabUIPageAdapter_getCount: String? = null
        var MainTabUIPageAdapter_onPageScrolled: String? = null
        var WxViewPager_selectedPage: String? = null
        var WXCustomSchemeEntryActivity_entry: String? = null
    }

    companion object {

        @Throws(IOException::class)
        fun loadConfig(wxVersion: String): WxVersionConfig {
            return AppCustomConfig.getWxVersionConfig(wxVersion)
        }
    }
}