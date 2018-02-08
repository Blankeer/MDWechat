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
        var LauncherUI: String? = null
        var HomeUI: String? = null
        var HomeUiTabHelper: String? = null
        var MMFragmentActivity: String? = null
        var AvatarUtils2: String? = null
        var TouchImageView: String? = null
        var FTSMainUI: String? = null
        var HomeUiViewPagerChangeListener: String? = null
        var WxViewPager: String? = null
        var LauncherUIBottomTabView: String? = null
        var ActionBarContainer: String? = null
        var ToolbarWidgetWrapper: String? = null
        var ActionBarSearchView: String? = null
        var ActionBarEditText: String? = null
        var ConversationFragment: String? = null
        var ConversationWithAppBrandListView: String? = null
        var ConversationAdapter: String? = null
        var ContactFragment: String? = null
        var ContactAdapter: String? = null
        var DiscoverFragment: String? = null
        var MMPreferenceAdapter: String? = null
        var SettingsFragment: String? = null
        var ChatViewHolder: String? = null
        var ChatAudioViewHolder: String? = null
        var XLogSetup: String? = null
        var PluginHelper: String? = null
        var SnsTimeLineUI: String? = null
        var WalletOfflineCoinPurseUI: String? = null
        var BaseScanUI: String? = null
        var AddMoreFriendsUI: String? = null
        var AppBrandLauncherUI: String? = null
        var MallIndexUI: String? = null
        var FavoriteIndexUI: String? = null
        var SelectContactUI: String? = null
    }

    class Fields {
        var ActionBarContainer_mBackground: String? = null
        var LauncherUI_mHomeUi: String? = null
        var HomeUI_mActionBar: String? = null
        var HomeUi_mHomeUiTabHelper: String? = null
        var HomeUiTabHelper_mViewPager: String? = null
        var HomeUI_mMoreMenuItem: String? = null
        var ConversationFragment_mListView: String? = null
        var ContactFragment_mListView: String? = null
        var PreferenceFragment_mListView: String? = null
        var ChatViewHolder_mChatTextView: String? = null
        var ChatViewHolder_mTextColor: String? = null
        var ChatAudioViewHolder_mChatTextView: String? = null
        var TopInfo_isTop: String? = null
        var CellTextView_mMsgView: String? = null
        var ChatAudioViewHolder_mAudioAnimImageView: String? = null
        var ChatAudioViewHolder_mAudioSendingImageView: String? = null
    }

    class Methods {
        var LauncherUI_startMainUI: String? = null
        var AvatarUtils2_getAvatarHDBitmap: String? = null
        var AvatarUtils2_getAvatarBitmap: String? = null
        var AvatarUtils2_getDefaultAvatarBitmap: String? = null
        var TouchImageView_init: String? = null
        var HomeUiViewPagerChangeListener_onPageScrolled: String? = null
        var HomeUiViewPagerChangeListener_onPageSelected: String? = null
        var HomeUiViewPagerChangeListener_getCount: String? = null
        var HomeUiViewPagerChangeListener_getFragment: String? = null
        var HomeUiViewPagerChangeListener_visibleFragment: String? = null
        var WxViewPager_setCurrentItem: String? = null
        var LauncherUIBottomTabView_setMainTabUnread: String? = null
        var LauncherUIBottomTabView_setContactTabUnread: String? = null
        var LauncherUIBottomTabView_setFriendTabUnread: String? = null
        var LauncherUIBottomTabView_showFriendTabUnreadPoint: String? = null
        var ActionBarSearchView_init: String? = null
        var MainFragment_onTabCreate: String? = null
        var MMPreferenceAdapter_setVisible: String? = null
        var ChatViewHolder_loadView: String? = null
        var ChatAudioViewHolder_loadView: String? = null
        var XLogSetup_keep_setupXLog: String? = null
        var PluginHelper_start: String? = null
        var ConversationAdapter_getUserInfo: String? = null
        var ConversationAdapter_getTopInfo: String? = null
        var ConversationWithAppBrandListView_isAppBrandHeaderEnable: String? = null
    }

    companion object {

        @Throws(IOException::class)
        fun loadConfig(wxVersion: String): WxVersionConfig {
            return AppCustomConfig.getWxVersionConfig(wxVersion)
        }
    }
}
