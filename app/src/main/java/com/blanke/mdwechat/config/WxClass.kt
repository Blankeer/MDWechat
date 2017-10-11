package com.blanke.mdwechat.config

import de.robv.android.xposed.XposedHelpers

/**
 * Created by blanke on 2017/10/11.
 */
object WxClass {
    var LauncherUI: Class<*>? = null
    var HomeUI: Class<*>? = null
    var MenuAdapterManager: Class<*>? = null
    var MMFragmentActivity: Class<*>? = null
    var AvatarUtils: Class<*>? = null
    var AvatarUtils2: Class<*>? = null
    var TouchImageView: Class<*>? = null
    var ChattingUIActivity: Class<*>? = null
    var ChattingUIFragment: Class<*>? = null
    var MenuItemViewHolderWrapper: Class<*>? = null
    var MenuItemViewHolder: Class<*>? = null
    var FTSMainUI: Class<*>? = null
    var HomeUiViewPagerChangeListener: Class<*>? = null
    var WxViewPager: Class<*>? = null
    var LauncherUIBottomTabView: Class<*>? = null
    var ActionBarContainer: Class<*>? = null
    var ToolbarWidgetWrapper: Class<*>? = null
    var ActionBarSearchView: Class<*>? = null
    var ActionBarEditText: Class<*>? = null
    var ConversationFragment: Class<*>? = null
    var ConversationAdapter: Class<*>? = null
    var ContactFragment: Class<*>? = null
    var ContactAdapter: Class<*>? = null
    var DiscoverFragment: Class<*>? = null
    var MMPreferenceAdapter: Class<*>? = null
    var SettingsFragment: Class<*>? = null
    var ChatViewHolder: Class<*>? = null

    fun init(config: WxVersionConfig, classLoader: ClassLoader) {
        LauncherUI = XposedHelpers.findClassIfExists(config.classes.LauncherUI, classLoader)
        HomeUI = XposedHelpers.findClassIfExists(config.classes.HomeUI, classLoader)
        MenuAdapterManager = XposedHelpers.findClassIfExists(config.classes.MenuAdapterManager, classLoader)
        MMFragmentActivity = XposedHelpers.findClassIfExists(config.classes.MMFragmentActivity, classLoader)
        AvatarUtils = XposedHelpers.findClassIfExists(config.classes.AvatarUtils, classLoader)
        AvatarUtils2 = XposedHelpers.findClassIfExists(config.classes.AvatarUtils2, classLoader)
        TouchImageView = XposedHelpers.findClassIfExists(config.classes.TouchImageView, classLoader)
        ChattingUIActivity = XposedHelpers.findClassIfExists(config.classes.ChattingUIActivity, classLoader)
        ChattingUIFragment = XposedHelpers.findClassIfExists(config.classes.ChattingUIFragment, classLoader)
        MenuItemViewHolderWrapper = XposedHelpers.findClassIfExists(config.classes.MenuItemViewHolderWrapper, classLoader)
        MenuItemViewHolder = XposedHelpers.findClassIfExists(config.classes.MenuItemViewHolder, classLoader)
        FTSMainUI = XposedHelpers.findClassIfExists(config.classes.FTSMainUI, classLoader)
        HomeUiViewPagerChangeListener = XposedHelpers.findClassIfExists(config.classes.HomeUiViewPagerChangeListener, classLoader)
        WxViewPager = XposedHelpers.findClassIfExists(config.classes.WxViewPager, classLoader)
        LauncherUIBottomTabView = XposedHelpers.findClassIfExists(config.classes.LauncherUIBottomTabView, classLoader)
        ActionBarContainer = XposedHelpers.findClassIfExists(config.classes.ActionBarContainer, classLoader)
        ToolbarWidgetWrapper = XposedHelpers.findClassIfExists(config.classes.ToolbarWidgetWrapper, classLoader)
        ActionBarSearchView = XposedHelpers.findClassIfExists(config.classes.ActionBarSearchView, classLoader)
        ActionBarEditText = XposedHelpers.findClassIfExists(config.classes.ActionBarEditText, classLoader)
        ConversationFragment = XposedHelpers.findClassIfExists(config.classes.ConversationFragment, classLoader)
        ConversationAdapter = XposedHelpers.findClassIfExists(config.classes.ConversationAdapter, classLoader)
        ContactFragment = XposedHelpers.findClassIfExists(config.classes.ContactFragment, classLoader)
        ContactAdapter = XposedHelpers.findClassIfExists(config.classes.ContactAdapter, classLoader)
        DiscoverFragment = XposedHelpers.findClassIfExists(config.classes.DiscoverFragment, classLoader)
        MMPreferenceAdapter = XposedHelpers.findClassIfExists(config.classes.MMPreferenceAdapter, classLoader)
        SettingsFragment = XposedHelpers.findClassIfExists(config.classes.SettingsFragment, classLoader)
        ChatViewHolder = XposedHelpers.findClassIfExists(config.classes.ChatViewHolder, classLoader)
    }
}