package com.blanke.mdwechat.hookers

import android.graphics.drawable.BitmapDrawable
import android.view.View
import com.blanke.mdwechat.Classes
import com.blanke.mdwechat.Fields.ContactFragment_mListView
import com.blanke.mdwechat.Methods.HomeFragment_lifecycles
import com.blanke.mdwechat.WeChatHelper.whiteDrawable
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.util.LogUtil
import com.gh0u1l5.wechatmagician.spellbook.base.Hooker
import com.gh0u1l5.wechatmagician.spellbook.base.HookerProvider
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object ContactHooker : HookerProvider {
    const val keyInit = "key_init"

    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(resumeHook)
    }

    private val resumeHook = Hooker {
        HomeFragment_lifecycles.forEach {
            XposedHelpers.findAndHookMethod(Classes.ContactFragment, it.name, object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    val fragment = param?.thisObject ?: return
//                    LogUtil.log("ContactFragment fragment=$fragment,${Classes.ContactFragment.name}")
                    val isInit = XposedHelpers.getAdditionalInstanceField(fragment, keyInit)
                    if (isInit != null) {
                        LogUtil.log("ContactFragment 已经hook过")
                        return
                    }
                    XposedHelpers.setAdditionalInstanceField(fragment, keyInit, true)
                    init(fragment)
                }

                private fun init(fragment: Any) {
                    val listView = ContactFragment_mListView.get(fragment)
                    if (listView != null && listView is View) {
                        val background = AppCustomConfig.getTabBg(1)
                        listView.background = if (background != null) BitmapDrawable(background) else whiteDrawable
                    }
                }
            })
        }
    }
}