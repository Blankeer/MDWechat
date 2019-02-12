package com.blanke.mdwechat.auto_search

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import android.view.animation.Animation
import android.widget.*
import com.blanke.mdwechat.CC
import com.blanke.mdwechat.CC.voidd
import com.blanke.mdwechat.Version
import com.blanke.mdwechat.auto_search.WechatGlobal.wxLoader
import com.blanke.mdwechat.auto_search.WechatGlobal.wxPackageName
import com.blanke.mdwechat.util.ReflectionUtil
import com.blanke.mdwechat.util.ReflectionUtil.findClassIfExists

object Classes {
    val LauncherUI: Class<*>?
        get() {
            return findClassIfExists("$wxPackageName.ui.LauncherUI", wxLoader!!)
        }

    val HomeUI: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.HomeUI", WechatGlobal.wxLoader)
        }

    val WxViewPager: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.mogic.WxViewPager", WechatGlobal.wxLoader)
        }

    val CustomViewPager: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.base.CustomViewPager", WechatGlobal.wxLoader)
        }

    val MMFragmentActivity: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.MMFragmentActivity", WechatGlobal.wxLoader)
        }

    val MainTabUI: Class<*>?
        get() {
            return ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader, WechatGlobal.wxClasses, "${WechatGlobal.wxPackageName}.ui")
                    .filterByField(CustomViewPager!!.name)
                    .filterByField(MMFragmentActivity!!.name)
                    .firstOrNull()
        }

    val MainTabUIPageAdapter: Class<*>?
        get() {
            return ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader, WechatGlobal.wxClasses, "${WechatGlobal.wxPackageName}.ui")
                    .filterByField(MainTabUI!!.name)
                    .filterByField(WxViewPager!!.name)
                    .filterByMethod(CC.Int, "getCount")
                    .firstOrNull()
        }

    val LauncherUIBottomTabView: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.LauncherUIBottomTabView", WechatGlobal.wxLoader)
        }

    val TabIconView: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.TabIconView", WechatGlobal.wxLoader)
        }

    val ThreadExecutor: Class<*>?
        get() {
            return ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader, WechatGlobal.wxClasses, "${WechatGlobal.wxPackageName}.sdk.platformtools")
                    .filterByMethod(voidd, "run")
                    .filterByField(Thread::class.java.name)
                    .filterByField(Handler::class.java.name)
                    .filterByField(Runnable::class.java.name)
                    .filterByField(Long::class.java.name)
                    .firstOrNull()
        }

    val LauncherUIBottomTabViewItem: Class<*>?
        get() {
            return ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader, WechatGlobal.wxClasses, "${WechatGlobal.wxPackageName}.ui")
                    .filterByField(TabIconView!!.name)
                    .filterByField(CC.View.name)
                    .filterByField(TextView::class.java.name)
                    .filterByField(ImageView::class.java.name)
                    .firstOrNull()
        }

    val ActionBarContainer: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("android.support.v7.widget.ActionBarContainer", WechatGlobal.wxLoader)
        }

    val ScrollingTabContainerView: Class<*>?
        get() {
            return ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader, WechatGlobal.wxClasses, "android.support.v7.widget")
                    .filterBySuper(HorizontalScrollView::class.java)
                    .firstOrNull()
        }

    val PhoneWindow: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("com.android.internal.policy.PhoneWindow", WechatGlobal.wxLoader)
        }

    val AvatarUtils: Class<*>?
        get() {
            return ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader, WechatGlobal.wxClasses, "${WechatGlobal.wxPackageName}.app")
                    .filterByField(Bitmap::class.java.name)
                    .filterByMethod(Bitmap::class.java)
                    .filterByMethod(Bitmap::class.java, CC.String)
                    .filterByMethod(Bitmap::class.java, CC.String, CC.Int, CC.Int, CC.Int)
                    .firstOrNull()
        }

    val NoDrawingCacheLinearLayout: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.NoDrawingCacheLinearLayout", WechatGlobal.wxLoader)
        }

    val ConversationWithAppBrandListView: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.conversation.ConversationWithAppBrandListView", WechatGlobal.wxLoader)
        }

    val ConversationListView: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.conversation.ConversationListView", WechatGlobal.wxLoader)
        }

    val ConversationFragment: Class<*>?
        get() {
            if (WechatGlobal.wxVersion!! < Version("7.0.3")) {
                return ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader, WechatGlobal.wxClasses, "${WechatGlobal.wxPackageName}.ui.conversation")
                        .filterByField(ConversationWithAppBrandListView!!.name)
                        .filterByField(TextView::class.java.name)
                        .filterByMethod(voidd, "onResume")
                        .firstOrNull()
            }
            return ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader, WechatGlobal.wxClasses, "${WechatGlobal.wxPackageName}.ui.conversation")
                    .filterByField(ConversationListView!!.name)
                    .filterByField(TextView::class.java.name)
                    .filterByMethod(voidd, "onResume")
                    .firstOrNull()
        }

    val ContactFragment: Class<*>?
        get() {
            return ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader, WechatGlobal.wxClasses, "${WechatGlobal.wxPackageName}.ui.contact")
                    .filterByField(ProgressDialog::class.java.name)
                    .filterByField(TextView::class.java.name)
                    .filterByField(Animation::class.java.name)
                    .filterByField(ListView::class.java.name)
                    .filterByField(LinearLayout::class.java.name)
                    .filterByMethod(CC.Int, "getLayoutId")
                    .filterByMethod(CC.Boolean, "noActionBar")
                    .firstOrNull()
        }

    private val FragmentActivity: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("android.support.v4.app.FragmentActivity", WechatGlobal.wxLoader)
        }

    private val Fragment: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("android.support.v4.app.Fragment", WechatGlobal.wxLoader)
        }

    val DiscoverFragment: Class<*>?
        get() {
            return ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader, WechatGlobal.wxClasses, "${WechatGlobal.wxPackageName}.ui")
                    .filterByField(CheckBox::class.java.name)
                    .filterByField(TextView::class.java.name)
                    .filterByField(View::class.java.name)
                    .filterByField(CC.Int.name)
                    .filterByField(CC.String.name)
                    .filterByField(CC.Boolean.name)
                    .filterByField(CC.Long.name)
                    .filterByMethod(voidd, "onActivityCreated", CC.Bundle)
                    .filterByMethod(CC.Boolean, "supportNavigationSwipeBack")
                    .filterByMethod(CC.Boolean, "noActionBar")
                    .firstOrNull()
        }

    val SettingsFragment: Class<*>?
        get() {
            return ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader, WechatGlobal.wxClasses, "${WechatGlobal.wxPackageName}.ui")
                    .filterByMethod(voidd, "onCreate", CC.Bundle)
                    .filterByMethod(voidd, "onActivityCreated", CC.Bundle)
                    .filterByMethod(voidd, "onDestroy")
                    .filterByMethod(CC.Boolean, "supportNavigationSwipeBack")
                    .filterByMethod(CC.Boolean, "noActionBar")
                    .firstOrNull()
        }

    val PreferenceFragment: Class<*>?
        get() {
            return ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader, WechatGlobal.wxClasses, "${WechatGlobal.wxPackageName}.ui.base.preference")
                    .filterByField(SharedPreferences::class.java.name)
                    .filterByField(CC.ListView.name)
                    .filterByField(CC.Boolean.name)
                    .filterByMethod(CC.Int, "getLayoutId")
                    .filterByMethod(CC.View, "getLayoutView")
                    .filterByMethod(voidd, "onResume")
                    .filterByMethod(voidd, "onActivityCreated", CC.Bundle)
                    .filterByMethod(CC.Boolean, "onContextItemSelected", MenuItem::class.java)
                    .firstOrNull()
        }

    val NoMeasuredTextView: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("com.tencent.mm.ui.base.NoMeasuredTextView", WechatGlobal.wxLoader)
        }

    val ActionMenuView: Class<*>?
        get() {
            return ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader, WechatGlobal.wxClasses, "android.support.v7.view.menu")
                    .filterByField(CharSequence::class.java.name)
                    .filterByField(Drawable::class.java.name)
                    .filterByMethod(MenuItem::class.java, "add", CharSequence::class.java)
                    .filterByMethod(MenuItem::class.java, "add", Int::class.java, Int::class.java, Int::class.java, Int::class.java)
                    .filterByMethod(SubMenu::class.java, "addSubMenu", CharSequence::class.java)
                    .firstOrNull()
        }

    val WXCustomSchemeEntryActivity: Class<*>?
        get() {
            return ReflectionUtil.findClassIfExists("com.tencent.mm.plugin.base.stub.WXCustomSchemeEntryActivity", WechatGlobal.wxLoader)
        }
}