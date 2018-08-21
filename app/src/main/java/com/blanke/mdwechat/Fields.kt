package com.blanke.mdwechat

import com.blanke.mdwechat.Classes.ContactFragment
import com.blanke.mdwechat.Classes.ConversationFragment
import com.blanke.mdwechat.Classes.HomeUI
import com.blanke.mdwechat.Classes.LauncherUI
import com.blanke.mdwechat.Classes.LauncherUIBottomTabViewItem
import com.blanke.mdwechat.Classes.MainTabUI
import com.blanke.mdwechat.Classes.PreferenceFragment
import com.blanke.mdwechat.WechatGlobal.wxLazy
import java.lang.reflect.Field

object Fields {
    private fun findFieldsWithType(clazz: Class<*>?, typeName: String?): List<Field> {
        clazz ?: return listOf()
        return clazz.declaredFields.filter {
            it.type.name == typeName
        }.map {
            it.isAccessible = true
            it
        }
    }

    private fun findFieldsWithName(clazz: Class<*>?, name: String?): Field? {
        clazz ?: return null
        return clazz.declaredFields.find { it.name == name }?.apply { isAccessible = true }
    }

    private fun findFieldsWithName(clazz: Class<*>?, names: List<String>?): List<Field> {
        clazz ?: return listOf()
        names ?: return listOf()
        return clazz.declaredFields.filter { names.contains(it.name) }.map {
            it.isAccessible = true
            it
        }
    }

    val LauncherUI_mHomeUI: Field by wxLazy("LauncherUI_mHomeUI") {
        findFieldsWithName(LauncherUI, WechatGlobal.wxVersionConfig.fields.LauncherUI_mHomeUI)
    }

    val HomeUI_mMainTabUI: Field by wxLazy("HomeUI_mMainTabUI") {
        findFieldsWithName(HomeUI, WechatGlobal.wxVersionConfig.fields.HomeUI_mMainTabUI)
    }

    val MainTabUI_mCustomViewPager: Field by wxLazy("MainTabUI_mCustomViewPager") {
        findFieldsWithName(MainTabUI, WechatGlobal.wxVersionConfig.fields.MainTabUI_mCustomViewPager)
    }

    val HomeUI_mActionBar: Field by wxLazy("HomeUI_mActionBar") {
        findFieldsWithName(HomeUI, WechatGlobal.wxVersionConfig.fields.HomeUI_mActionBar)
    }

    val LauncherUIBottomTabViewItem_mTextViews: List<Field> by wxLazy("LauncherUIBottomTabViewItem_mTextViews") {
        findFieldsWithName(LauncherUIBottomTabViewItem, WechatGlobal.wxVersionConfig.fields.LauncherUIBottomTabViewItem_mTextViews)
    }

    val ConversationFragment_mListView: Field by wxLazy("ConversationFragment_mListView") {
        findFieldsWithName(ConversationFragment, WechatGlobal.wxVersionConfig.fields.ConversationFragment_mListView)
    }

    val ContactFragment_mListView: Field by wxLazy("ContactFragment_mListView") {
        findFieldsWithName(ContactFragment, WechatGlobal.wxVersionConfig.fields.ContactFragment_mListView)
    }

    val PreferenceFragment_mListView: Field by wxLazy("PreferenceFragment_mListView") {
        findFieldsWithName(PreferenceFragment, WechatGlobal.wxVersionConfig.fields.PreferenceFragment_mListView)
    }
}