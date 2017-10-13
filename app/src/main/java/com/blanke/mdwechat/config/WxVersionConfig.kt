package com.blanke.mdwechat.config

import com.blanke.mdwechat.Common
import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by blanke on 2017/8/25.
 */

class WxVersionConfig {
    lateinit var version: String
    lateinit var classes: Classes
    lateinit var fields: Fields
    lateinit var methods: Methods
    lateinit var views: Views

    class Classes {
        lateinit var LauncherUI: String
        lateinit var HomeUI: String
        lateinit var HomeUiTabHelper: String
        lateinit var MenuAdapterManager: String
        lateinit var MMFragmentActivity: String
        lateinit var AvatarUtils: String
        lateinit var AvatarUtils2: String
        lateinit var TouchImageView: String
        lateinit var ChattingUIActivity: String
        lateinit var ChattingUIFragment: String
        lateinit var MenuItemViewHolderWrapper: String
        lateinit var MenuItemViewHolder: String
        lateinit var FTSMainUI: String
        lateinit var HomeUiViewPagerChangeListener: String
        lateinit var WxViewPager: String
        lateinit var LauncherUIBottomTabView: String
        lateinit var ActionBarContainer: String
        lateinit var ToolbarWidgetWrapper: String
        lateinit var ActionBarSearchView: String
        lateinit var ActionBarEditText: String
        lateinit var ConversationFragment: String
        lateinit var ConversationAdapter: String
        lateinit var ContactFragment: String
        lateinit var ContactAdapter: String
        lateinit var DiscoverFragment: String
        lateinit var MMPreferenceAdapter: String
        lateinit var SettingsFragment: String
        lateinit var ChatViewHolder: String
        override fun toString(): String {
            return "Classes(LauncherUI='$LauncherUI', HomeUI='$HomeUI', MenuAdapterManager='$MenuAdapterManager', MMFragmentActivity='$MMFragmentActivity', AvatarUtils='$AvatarUtils', AvatarUtils2='$AvatarUtils2', TouchImageView='$TouchImageView', ChattingUIActivity='$ChattingUIActivity', ChattingUIFragment='$ChattingUIFragment', MenuItemViewHolderWrapper='$MenuItemViewHolderWrapper', MenuItemViewHolder='$MenuItemViewHolder', FTSMainUI='$FTSMainUI', HomeUiViewPagerChangeListener='$HomeUiViewPagerChangeListener', WxViewPager='$WxViewPager', LauncherUIBottomTabView='$LauncherUIBottomTabView', ActionBarContainer='$ActionBarContainer', ToolbarWidgetWrapper='$ToolbarWidgetWrapper', ActionBarSearchView='$ActionBarSearchView', ActionBarEditText='$ActionBarEditText', ConversationFragment='$ConversationFragment', ConversationAdapter='$ConversationAdapter', ContactFragment='$ContactFragment', ContactAdapter='$ContactAdapter', DiscoverFragment='$DiscoverFragment', MMPreferenceAdapter='$MMPreferenceAdapter', SettingsFragment='$SettingsFragment', ChatViewHolder='$ChatViewHolder')"
        }

    }

    class Fields {
        lateinit var ActionBarContainer_mBackground: String
        lateinit var LauncherUI_mHomeUi: String
        lateinit var HomeUi_mHomeUiTabHelper: String
        lateinit var HomeUiTabHelper_mViewPager: String
        lateinit var HomeUI_mMenuAdapterManager: String
        lateinit var HomeUI_mMoreMenuItem: String
        lateinit var MenuAdapterManager_mMenuArray: String
        lateinit var MenuAdapterManager_mMenuMapping: String
        lateinit var LauncherUIBottomTabView_mTabClickListener: String
        lateinit var ConversationFragment_mListView: String
        lateinit var ContactFragment_mListView: String
        lateinit var PreferenceFragment_mListView: String
        lateinit var ChatViewHolder_mChatTextView: String
        override fun toString(): String {
            return "Fields(LauncherUI_mHomeUi='$LauncherUI_mHomeUi', HomeUI_mMenuAdapterManager='$HomeUI_mMenuAdapterManager', HomeUI_mMoreMenuItem='$HomeUI_mMoreMenuItem', MenuAdapterManager_mMenuArray='$MenuAdapterManager_mMenuArray', MenuAdapterManager_mMenuMapping='$MenuAdapterManager_mMenuMapping', ActionBarContainer_mBackground='$ActionBarContainer_mBackground', LauncherUIBottomTabView_mTabClickListener='$LauncherUIBottomTabView_mTabClickListener', ConversationFragment_mListView='$ConversationFragment_mListView', ContactFragment_mListView='$ContactFragment_mListView', PreferenceFragment_mListView='$PreferenceFragment_mListView', ChatViewHolder_mChatTextView='$ChatViewHolder_mChatTextView')"
        }

    }

    class Methods {
        lateinit var LauncherUI_startMainUI: String
        lateinit var HomeUI_startSearch: String
        lateinit var AvatarUtils_getAvatarBitmap: String
        lateinit var AvatarUtils2_getAvatarBitmap: String
        lateinit var TouchImageView_init: String
        lateinit var HomeUiViewPagerChangeListener_onPageScrolled: String
        lateinit var HomeUiViewPagerChangeListener_onPageSelected: String
        lateinit var WxViewPager_setCurrentItem: String
        lateinit var LauncherUIBottomTabView_setMainTabUnread: String
        lateinit var LauncherUIBottomTabView_setContactTabUnread: String
        lateinit var LauncherUIBottomTabView_setFriendTabUnread: String
        lateinit var LauncherUIBottomTabView_showFriendTabUnreadPoint: String
        lateinit var ActionBarSearchView_init: String
        lateinit var MainTabClickListener_onDoubleClick: String
        lateinit var MainFragment_onTabCreate: String
        lateinit var MMPreferenceAdapter_setVisible: String
        lateinit var ChatViewHolder_loadView: String
        override fun toString(): String {
            return "Methods(LauncherUI_startMainUI='$LauncherUI_startMainUI', HomeUI_startSearch='$HomeUI_startSearch', AvatarUtils_getAvatarBitmap='$AvatarUtils_getAvatarBitmap', AvatarUtils2_getAvatarBitmap='$AvatarUtils2_getAvatarBitmap', TouchImageView_init='$TouchImageView_init', HomeUiViewPagerChangeListener_onPageScrolled='$HomeUiViewPagerChangeListener_onPageScrolled', HomeUiViewPagerChangeListener_onPageSelected='$HomeUiViewPagerChangeListener_onPageSelected', WxViewPager_setCurrentItem='$WxViewPager_setCurrentItem', LauncherUIBottomTabView_setMainTabUnread='$LauncherUIBottomTabView_setMainTabUnread', LauncherUIBottomTabView_setContactTabUnread='$LauncherUIBottomTabView_setContactTabUnread', LauncherUIBottomTabView_setFriendTabUnread='$LauncherUIBottomTabView_setFriendTabUnread', LauncherUIBottomTabView_showFriendTabUnreadPoint='$LauncherUIBottomTabView_showFriendTabUnreadPoint', ActionBarSearchView_init='$ActionBarSearchView_init', MainTabClickListener_onDoubleClick='$MainTabClickListener_onDoubleClick', MainFragment_onTabCreate='$MainFragment_onTabCreate', MMPreferenceAdapter_setVisible='$MMPreferenceAdapter_setVisible', ChatViewHolder_loadView='$ChatViewHolder_loadView')"
        }

    }

    class Views {
        lateinit var LauncherUI_MainViewPager: String
        lateinit var ActionBarContainer: String
        lateinit var ActionBar_Divider: String
        lateinit var ActionBar_BackImageView: String
        lateinit var SearchActionBar_Divider: String
        lateinit var SearchActionBar_BackImageView: String
        override fun toString(): String {
            return "Views(LauncherUI_MainViewPager='$LauncherUI_MainViewPager', ActionBarContainer='$ActionBarContainer', ActionBar_Divider='$ActionBar_Divider', ActionBar_BackImageView='$ActionBar_BackImageView', SearchActionBar_Divider='$SearchActionBar_Divider', SearchActionBar_BackImageView='$SearchActionBar_BackImageView')"
        }

    }

    companion object {

        @Throws(IOException::class)
        fun loadConfig(wxVersion: String): WxVersionConfig {
            var configName = wxVersion + ".config"
            if (HookConfig.isPlay) {
                configName = wxVersion + "-play.config"
            }
            val `is` = FileInputStream(Common.APP_DIR_PATH + File.separator + Common.CONFIG_DIR + File.separator + configName)
            return Gson().fromJson(InputStreamReader(`is`), WxVersionConfig::class.java)
        }
    }

    override fun toString(): String {
        return "WxVersionConfig(version='$version', classes=$classes, fields=$fields, methods=$methods, views=$views)"
    }
}
