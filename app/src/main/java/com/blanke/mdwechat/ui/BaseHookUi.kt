package com.blanke.mdwechat.ui

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blanke.mdwechat.R
import com.blanke.mdwechat.WeChatHelper
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.DrawableUtils
import com.blanke.mdwechat.util.LogUtil
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/8/1.
 */

abstract class BaseHookUi {
    private val TAG = javaClass.simpleName
    private var rippleDrawable: Drawable? = null

    abstract fun hook(lpparam: XC_LoadPackage.LoadPackageParam)

    //刷新设置
    protected fun refreshPrefs() {
        WeChatHelper.XMOD_PREFS!!.reload()
    }

    protected fun findViewByIdName(activity: Activity, idName: String): View {
        return activity.findViewById(getViewId(activity, idName))
    }

    protected fun findViewByIdName(view: View, idName: String): View {
        return view.findViewById(getViewId(view.context, idName))
    }

    @Deprecated("")
    protected fun getId(context: Context, idName: String): Int {
        return context.resources.getIdentifier(context.packageName + ":id/" + idName, null, null)
    }

    protected fun getViewId(context: Context, name: String): Int {
        return getResourceIdByName(context, "id", name)
    }

    protected fun getColorId(context: Context, idName: String): Int {
        return getResourceIdByName(context, "color", idName)
    }

    protected fun getDrawableIdByName(context: Context, name: String): Int {
        return getResourceIdByName(context, "drawable", name)
    }

    protected fun getResourceIdByName(context: Context, resourceName: String, name: String): Int {
        return context.resources.getIdentifier(
                context.packageName + ":" + resourceName + "/" + name, null, null)
    }

    protected val colorPrimaryDrawable: ColorDrawable
        get() {
            val colorDrawable = ColorDrawable()
            colorDrawable.color = colorPrimary
            return colorDrawable
        }

    protected val colorPrimary: Int
        get() = HookConfig.colorPrimary

    protected fun getDefaultRippleDrawable(context: Context): Drawable {
        if (rippleDrawable == null) {
            val attrs = intArrayOf(android.R.attr.selectableItemBackground)
            val ta = context.obtainStyledAttributes(attrs)
            rippleDrawable = ta.getDrawable(0)
            ta.recycle()
        } else {
            return rippleDrawable!!.constantState!!.newDrawable().mutate()
        }
        return rippleDrawable!!
    }

    protected fun getGreyRippleDrawable(context: Context): Drawable {
        return DrawableUtils.getPressedColorRippleDrawable(Color.WHITE,
                ContextCompat.getColor(context, R.color.colorPressGrey))
    }

    /**
     * 打印调试 view tree

     * @param rootView
     * *
     * @param level
     */
    protected fun printViewTree(rootView: View, level: Int) {
        logSpace(level, getViewMsg(rootView))
        if (rootView is ViewGroup) {
            val vg = rootView
            for (i in 0..vg.childCount - 1) {
                val v = vg.getChildAt(i)
                printViewTree(v, level + 1)
            }
        }
    }

    protected fun logViewTree(view: View) {
        printViewTree(view, 0)
    }

    protected fun printActivityViewTree(activity: Activity) {
        val contentView = activity.findViewById(android.R.id.content)
        printViewTree(contentView, 0)
    }

    protected fun printActivityWindowViewTree(activity: Activity) {
        log("-------start----------")
        printViewTree(activity.window.decorView, 0)
        log("-------end----------")
    }

    protected val transparentDrawable: Drawable
        get() = ColorDrawable(Color.TRANSPARENT)

    protected val whiteDrawable: Drawable
        get() = ColorDrawable(Color.WHITE)

    protected fun getViewMsg(view: View): String {
        val className = view.javaClass.name
        val id = view.id
        var text = ""
        if (view is TextView) {
            val textView = view
            text = textView.text.toString()
            text += "(" + textView.hint + ")"
        }
        return "$className,$id,$text,$view"
    }

    private fun logSpace(count: Int, msg: String) {
        val sb = StringBuilder()
        for (j in 0..count - 1) {
            sb.append("----")
        }
        sb.append(msg)
        log(sb.toString())
    }

    protected fun log(view: View) {
        log(getViewMsg(view))
    }

    protected fun log(msg: String) {
        LogUtil.log(TAG + ":" + msg)
    }

    protected fun log(e: Throwable) {
        LogUtil.log(e)
    }

    protected fun logSuperClass(clazz: Class<*>) {
        if (clazz == Any::class.java) {
            return
        }
        log(clazz.name)
        logSuperClass(clazz.superclass)
    }

    @JvmOverloads protected fun logStackTraces(methodCount: Int = 15, methodOffset: Int = 3) {
        val trace = Thread.currentThread().stackTrace
        var level = ""
        log("---------logStackTraces start----------")
        for (i in methodCount downTo 1) {
            val stackIndex = i + methodOffset
            if (stackIndex >= trace.size) {
                continue
            }
            val builder = StringBuilder()
            builder.append("|")
                    .append(' ')
                    .append(level)
                    .append(trace[stackIndex].className)
                    .append(".")
                    .append(trace[stackIndex].methodName)
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].fileName)
                    .append(":")
                    .append(trace[stackIndex].lineNumber)
                    .append(")")
            level += "   "
            log(builder.toString())
        }
        log("---------logStackTraces end----------")
    }
}
