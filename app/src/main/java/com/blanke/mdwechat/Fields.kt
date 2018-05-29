package com.blanke.mdwechat

import android.widget.ListView
import android.widget.TextView
import com.blanke.mdwechat.Classes.ContactFragment
import com.blanke.mdwechat.Classes.ConversationFragment
import com.blanke.mdwechat.Classes.ConversationWithAppBrandListView
import com.blanke.mdwechat.Classes.CustomViewPager
import com.blanke.mdwechat.Classes.HomeUI
import com.blanke.mdwechat.Classes.LauncherUIBottomTabViewItem
import com.blanke.mdwechat.Classes.MainTabUI
import com.gh0u1l5.wechatmagician.spellbook.WechatGlobal.wxLazy
import com.gh0u1l5.wechatmagician.spellbook.mirror.com.tencent.mm.ui.Classes.LauncherUI
import com.gh0u1l5.wechatmagician.spellbook.util.ReflectionUtil.findFieldsWithType
import java.lang.reflect.Field

object Fields {

    val LauncherUI_mHomeUI: Field by wxLazy("LauncherUI_mHomeUI") {
        findFieldsWithType(
                LauncherUI, HomeUI.name)
                .firstOrNull()?.apply { isAccessible = true }
    }

    val HomeUI_mMainTabUI: Field by wxLazy("HomeUI_mMainTabUI") {
        findFieldsWithType(
                HomeUI, MainTabUI.name)
                .firstOrNull()?.apply { isAccessible = true }
    }

    val MainTabUI_mCustomViewPager: Field by wxLazy("MainTabUI_mCustomViewPager") {
        findFieldsWithType(
                MainTabUI, CustomViewPager.name)
                .firstOrNull()?.apply { isAccessible = true }
    }

    val HomeUI_mActionBar: Field by wxLazy("HomeUI_mActionBar") {
        findFieldsWithType(
                HomeUI, "android.support.v7.app.ActionBar")
                .firstOrNull()?.apply { isAccessible = true }
    }

    val LauncherUIBottomTabViewItem_mTextViews: List<Field> by wxLazy("LauncherUIBottomTabViewItem_mTextViews") {
        findFieldsWithType(LauncherUIBottomTabViewItem, TextView::class.java.name)
    }

    val ConversationFragment_mListView: Field by wxLazy("ConversationFragment_mListView") {
        findFieldsWithType(ConversationFragment, ConversationWithAppBrandListView.name)
                .firstOrNull()?.apply { isAccessible = true }
    }

    val ContactFragment_mListView: Field by wxLazy("ContactFragment_mListView") {
        findFieldsWithType(ContactFragment, ListView::class.java.name)
                .firstOrNull()?.apply { isAccessible = true }
    }
}