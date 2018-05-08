package com.blanke.mdwechat

import android.os.Handler
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
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
}