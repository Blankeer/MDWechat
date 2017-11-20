package com.blanke.mdwechat.config

import java.io.IOException

/**
 * Created by blanke on 2017/8/25.
 */

class WxVersionConfig {
    lateinit var version: String
    lateinit var classes: Classes
    lateinit var fields: Fields
    lateinit var methods: Methods

    class Classes {
        lateinit var LauncherUI: String
        lateinit var HomeUI: String
        lateinit var HomeUiTabHelper: String
        lateinit var MMFragmentActivity: String
        lateinit var AvatarUtils2: String
        lateinit var TouchImageView: String
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
        lateinit var ChatAudioViewHolder: String
        lateinit var XLogSetup: String
        lateinit var PluginHelper: String
        lateinit var SnsTimeLineUI: String
        lateinit var WalletOfflineCoinPurseUI: String
        lateinit var BaseScanUI: String
        lateinit var AddMoreFriendsUI: String
        lateinit var AppBrandLauncherUI: String
        lateinit var MallIndexUI: String
        lateinit var FavoriteIndexUI: String
        lateinit var SelectContactUI: String
    }

    class Fields {
        lateinit var ActionBarContainer_mBackground: String
        lateinit var LauncherUI_mHomeUi: String
        lateinit var HomeUI_mActionBar: String
        lateinit var HomeUi_mHomeUiTabHelper: String
        lateinit var HomeUiTabHelper_mViewPager: String
        lateinit var HomeUI_mMoreMenuItem: String
        lateinit var ConversationFragment_mListView: String
        lateinit var ContactFragment_mListView: String
        lateinit var PreferenceFragment_mListView: String
        lateinit var ChatViewHolder_mChatTextView: String
        lateinit var ChatAudioViewHolder_mChatTextView: String
        lateinit var TopInfo_isTop: String
        lateinit var CellTextView_mMsgView: String
        lateinit var ChatAudioViewHolder_mAudioAnimImageView: String
        lateinit var ChatAudioViewHolder_mAudioingImageView: String
    }

    class Methods {
        lateinit var LauncherUI_startMainUI: String
        lateinit var AvatarUtils2_getAvatarHDBitmap: String
        lateinit var AvatarUtils2_getAvatarBitmap: String
        lateinit var AvatarUtils2_getDefaultAvatarBitmap: String
        lateinit var TouchImageView_init: String
        lateinit var HomeUiViewPagerChangeListener_onPageScrolled: String
        lateinit var HomeUiViewPagerChangeListener_onPageSelected: String
        lateinit var WxViewPager_setCurrentItem: String
        lateinit var LauncherUIBottomTabView_setMainTabUnread: String
        lateinit var LauncherUIBottomTabView_setContactTabUnread: String
        lateinit var LauncherUIBottomTabView_setFriendTabUnread: String
        lateinit var LauncherUIBottomTabView_showFriendTabUnreadPoint: String
        lateinit var ActionBarSearchView_init: String
        lateinit var MainFragment_onTabCreate: String
        lateinit var MMPreferenceAdapter_setVisible: String
        lateinit var ChatViewHolder_loadView: String
        lateinit var ChatAudioViewHolder_loadView: String
        lateinit var XLogSetup_keep_setupXLog: String
        lateinit var PluginHelper_start: String
        lateinit var ConversationAdapter_getUserInfo: String
        lateinit var ConversationAdapter_getTopInfo: String
    }

    companion object {

        @Throws(IOException::class)
        fun loadConfig(wxVersion: String): WxVersionConfig {
            return AppCustomConfig.getWxVersionConfig(wxVersion)
        }
    }
}
