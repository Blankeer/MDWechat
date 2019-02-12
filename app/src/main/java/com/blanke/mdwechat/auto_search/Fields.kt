package com.blanke.mdwechat.auto_search

import com.blanke.mdwechat.CC
import com.blanke.mdwechat.Version
import com.blanke.mdwechat.auto_search.Classes.ContactFragment
import com.blanke.mdwechat.auto_search.Classes.ConversationFragment
import com.blanke.mdwechat.auto_search.Classes.ConversationListView
import com.blanke.mdwechat.auto_search.Classes.ConversationWithAppBrandListView
import com.blanke.mdwechat.auto_search.Classes.CustomViewPager
import com.blanke.mdwechat.auto_search.Classes.HomeUI
import com.blanke.mdwechat.auto_search.Classes.LauncherUI
import com.blanke.mdwechat.auto_search.Classes.LauncherUIBottomTabViewItem
import com.blanke.mdwechat.auto_search.Classes.MainTabUI
import com.blanke.mdwechat.auto_search.Classes.PreferenceFragment
import com.blanke.mdwechat.util.ReflectionUtil.findFieldsWithType
import java.lang.reflect.Field

object Fields {
    val LauncherUI_mHomeUI: Field?
        get() {
            return findFieldsWithType(LauncherUI!!, HomeUI!!.name)
                    .firstOrNull()?.apply { isAccessible = true }
        }

    val HomeUI_mMainTabUI: Field?
        get() {
            return findFieldsWithType(HomeUI!!, MainTabUI!!.name)
                    .firstOrNull()?.apply { isAccessible = true }
        }

    val MainTabUI_mCustomViewPager: Field?
        get() {
            return findFieldsWithType(
                    MainTabUI!!, CustomViewPager!!.name)
                    .firstOrNull()?.apply { isAccessible = true }
        }

    val HomeUI_mActionBar: Field?
        get() {
            return findFieldsWithType(
                    HomeUI!!, "android.support.v7.app.ActionBar")
                    .firstOrNull()?.apply { isAccessible = true }
        }

    val LauncherUIBottomTabViewItem_mTextViews: List<Field>?
        get() {
            return findFieldsWithType(LauncherUIBottomTabViewItem!!, CC.TextView.name)
        }

    val ConversationFragment_mListView: Field?
        get() {
            if (WechatGlobal.wxVersion!! < Version("7.0.3")) {
                return findFieldsWithType(ConversationFragment!!, ConversationWithAppBrandListView!!.name)
                        .firstOrNull()?.apply { isAccessible = true }
            }
            return findFieldsWithType(ConversationFragment!!, ConversationListView!!.name)
                    .firstOrNull()?.apply { isAccessible = true }
        }

    val ContactFragment_mListView: Field?
        get() {
            return findFieldsWithType(ContactFragment!!, CC.ListView.name)
                    .firstOrNull()?.apply { isAccessible = true }
        }

    val PreferenceFragment_mListView: Field?
        get() {
            return findFieldsWithType(PreferenceFragment!!, CC.ListView.name)
                    .firstOrNull()?.apply { isAccessible = true }
        }
}