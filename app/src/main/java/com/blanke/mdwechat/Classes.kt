package com.blanke.mdwechat

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.widget.*
import com.blanke.mdwechat.CC.voidd
import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.WechatGlobal
import com.gh0u1l5.wechatmagician.spellbook.mirror.com.tencent.mm.ui.Classes.MMFragmentActivity
import com.gh0u1l5.wechatmagician.spellbook.util.ReflectionUtil

object Classes {
    val HomeUI: Class<*> by WechatGlobal.wxLazy("HomeUI") {
        ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.HomeUI", WechatGlobal.wxLoader!!)
    }

    val WxViewPager: Class<*> by WechatGlobal.wxLazy("WxViewPager") {
        ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.mogic.WxViewPager", WechatGlobal.wxLoader!!)
    }

    val CustomViewPager: Class<*> by WechatGlobal.wxLazy("CustomViewPager") {
        ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.base.CustomViewPager", WechatGlobal.wxLoader!!)
    }

    val MainTabUI: Class<*> by WechatGlobal.wxLazy("MainTabUI") {
        ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader!!, WechatGlobal.wxClasses!!, "${WechatGlobal.wxPackageName}.ui")
                .filterByField(CustomViewPager.name)
                .filterByField(MMFragmentActivity.name)
                .firstOrNull()
    }

    val MainTabUIPageAdapter: Class<*> by WechatGlobal.wxLazy("MainTabUIPageAdapter") {
        ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader!!, WechatGlobal.wxClasses!!, "${WechatGlobal.wxPackageName}.ui")
                .filterByField(MainTabUI.name)
                .filterByField(WxViewPager.name)
                .filterByMethod(C.Int, "getCount")
                .firstOrNull()
    }

    val LauncherUIBottomTabView: Class<*> by WechatGlobal.wxLazy("LauncherUIBottomTabView") {
        ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.LauncherUIBottomTabView", WechatGlobal.wxLoader!!)
    }

    val TabIconView: Class<*> by WechatGlobal.wxLazy("TabIconView") {
        ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.TabIconView", WechatGlobal.wxLoader!!)
    }

    val ThreadExecutor: Class<*> by WechatGlobal.wxLazy("ThreadExecutor") {
        ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader!!, WechatGlobal.wxClasses!!, "${WechatGlobal.wxPackageName}.sdk.platformtools")
                .filterByMethod(voidd, "run")
                .filterByField(Thread::class.java.name)
                .filterByField(Handler::class.java.name)
                .filterByField(Runnable::class.java.name)
                .filterByField(Long::class.java.name)
                .firstOrNull()
    }

    val LauncherUIBottomTabViewItem: Class<*> by WechatGlobal.wxLazy("LauncherUIBottomTabViewItem") {
        ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader!!, WechatGlobal.wxClasses!!, "${WechatGlobal.wxPackageName}.ui")
                .filterByField(TabIconView.name)
                .filterByField(C.View.name)
                .filterByField(TextView::class.java.name)
                .filterByField(ImageView::class.java.name)
                .firstOrNull()
    }

    val ActionBarContainer: Class<*> by WechatGlobal.wxLazy("ActionBarContainer") {
        ReflectionUtil.findClassIfExists("android.support.v7.widget.ActionBarContainer", WechatGlobal.wxLoader!!)
    }

    val ScrollingTabContainerView: Class<*> by WechatGlobal.wxLazy("ScrollingTabContainerView") {
        ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader!!, WechatGlobal.wxClasses!!, "android.support.v7.widget")
                .filterBySuper(HorizontalScrollView::class.java)
                .firstOrNull()
    }

    val PhoneWindow: Class<*> by WechatGlobal.wxLazy("PhoneWindow") {
        ReflectionUtil.findClassIfExists("com.android.internal.policy.PhoneWindow", WechatGlobal.wxLoader!!)
    }

    val AvatarUtils: Class<*> by WechatGlobal.wxLazy("AvatarUtils") {
        ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader!!, WechatGlobal.wxClasses!!, "${WechatGlobal.wxPackageName}.app")
                .filterByField(Bitmap::class.java.name)
                .filterByMethod(Bitmap::class.java)
                .filterByMethod(Bitmap::class.java, C.String)
                .filterByMethod(Bitmap::class.java, C.String, C.Int, C.Int, C.Int)
                .firstOrNull()
    }

    val NoDrawingCacheLinearLayout: Class<*> by WechatGlobal.wxLazy("NoDrawingCacheLinearLayout") {
        ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.NoDrawingCacheLinearLayout", WechatGlobal.wxLoader!!)
    }

    val ConversationWithAppBrandListView: Class<*> by WechatGlobal.wxLazy("ConversationWithAppBrandListView") {
        ReflectionUtil.findClassIfExists("${WechatGlobal.wxPackageName}.ui.conversation.ConversationWithAppBrandListView", WechatGlobal.wxLoader!!)
    }

    val ConversationFragment: Class<*> by WechatGlobal.wxLazy("ConversationFragment") {
        ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader!!, WechatGlobal.wxClasses!!, "${WechatGlobal.wxPackageName}.ui.conversation")
                .filterByField(ConversationWithAppBrandListView.name)
                .filterByField(TextView::class.java.name)
                .filterByMethod(voidd, "onResume")
                .firstOrNull()
    }

    val ContactFragment: Class<*> by WechatGlobal.wxLazy("ContactFragment") {
        ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader!!, WechatGlobal.wxClasses!!, "${WechatGlobal.wxPackageName}.ui.contact")
                .filterByField(ProgressDialog::class.java.name)
                .filterByField(TextView::class.java.name)
                .filterByField(Animation::class.java.name)
                .filterByField(ListView::class.java.name)
                .filterByField(LinearLayout::class.java.name)
                .filterByMethod(C.Int, "getLayoutId")
                .filterByMethod(C.Boolean, "noActionBar")
                .firstOrNull()
    }

    val FragmentActivity: Class<*> by WechatGlobal.wxLazy("FragmentActivity") {
        ReflectionUtil.findClassIfExists("android.support.v4.app.FragmentActivity", WechatGlobal.wxLoader!!)
    }

    val Fragment: Class<*> by WechatGlobal.wxLazy("Fragment") {
        ReflectionUtil.findClassIfExists("android.support.v4.app.Fragment", WechatGlobal.wxLoader!!)
    }

    val DiscoverFragment: Class<*> by WechatGlobal.wxLazy("DiscoverFragment") {
        ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader!!, WechatGlobal.wxClasses!!, "${WechatGlobal.wxPackageName}.ui")
                .filterByField(CheckBox::class.java.name)
                .filterByField(TextView::class.java.name)
                .filterByField(View::class.java.name)
                .filterByField(C.Int.name)
                .filterByField(C.String.name)
                .filterByField(C.Boolean.name)
                .filterByField(C.Long.name)
                .filterByMethod(voidd, "onActivityCreated", C.Bundle)
                .filterByMethod(C.Boolean, "supportNavigationSwipeBack")
                .filterByMethod(C.Boolean, "noActionBar")
                .firstOrNull()
    }

    val PreferenceFragment: Class<*> by WechatGlobal.wxLazy("PreferenceFragment") {
        ReflectionUtil.findClassesFromPackage(WechatGlobal.wxLoader!!, WechatGlobal.wxClasses!!, "${WechatGlobal.wxPackageName}.ui.base.preference")
                .filterByField(SharedPreferences::class.java.name)
                .filterByField(C.ListView.name)
                .filterByField(C.Boolean.name)
                .filterByField(C.Long.name)
                .filterByMethod(C.Int, "getLayoutId")
                .filterByMethod(C.View, "getLayoutView")
                .filterByMethod(voidd, "onResume")
                .filterByMethod(voidd, "onActivityCreated", C.Bundle)
                .filterByMethod(C.Boolean, "onContextItemSelected", MenuItem::class.java)
                .firstOrNull()
    }
}