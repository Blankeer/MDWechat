package com.blanke.mdwechat.config

import android.content.Context
import java.lang.ref.WeakReference

/**
 * Created by blanke on 2017/10/12.
 */
object WxObjects {
    var Application: WeakReference<Context>? = null
    var MdContext: WeakReference<Context>? = null
    var LauncherUI: WeakReference<Any>? = null
    var HomeUI: WeakReference<Any>? = null
    var HomeUI_ViewPager: WeakReference<Any>? = null
    var MenuAdapterManager: WeakReference<Any>? = null
    var MMFragmentActivity: WeakReference<Any>? = null
    var AvatarUtils: WeakReference<Any>? = null
    var AvatarUtils2: WeakReference<Any>? = null
    var TouchImageView: WeakReference<Any>? = null
    var ChattingUIActivity: WeakReference<Any>? = null
    var ChattingUIFragment: WeakReference<Any>? = null
    var MenuItemViewHolderWrapper: WeakReference<Any>? = null
    var MenuItemViewHolder: WeakReference<Any>? = null
    var FTSMainUI: WeakReference<Any>? = null
    var HomeUiViewPagerChangeListener: WeakReference<Any>? = null
    var WxViewPager: WeakReference<Any>? = null
    var LauncherUIBottomTabView: WeakReference<Any>? = null
    var ActionBarContainer: WeakReference<Any>? = null
    var ToolbarWidgetWrapper: WeakReference<Any>? = null
    var ActionBarSearchView: WeakReference<Any>? = null
    var ActionBarEditText: WeakReference<Any>? = null
    var ConversationFragment: WeakReference<Any>? = null
    var ConversationAdapter: WeakReference<Any>? = null
    var ContactFragment: WeakReference<Any>? = null
    var ContactAdapter: WeakReference<Any>? = null
    var DiscoverFragment: WeakReference<Any>? = null
    var MMPreferenceAdapter: WeakReference<Any>? = null
    var SettingsFragment: WeakReference<Any>? = null
    var ChatViewHolder: WeakReference<Any>? = null
    var HomeUiTabHelper: WeakReference<Any>? = null
}