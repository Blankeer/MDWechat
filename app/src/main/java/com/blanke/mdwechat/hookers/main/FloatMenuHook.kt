package com.blanke.mdwechat.hookers.main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.bean.FLoatButtonConfigItem
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.ConvertUtils
import com.blanke.mdwechat.util.LogUtil.log
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionButton.SIZE_MINI
import com.github.clans.fab.FloatingActionMenu

object FloatMenuHook {

    fun addFloatMenu(contentLayout: ViewGroup) {
        val context = contentLayout.context.createPackageContext(Common.MY_APPLICATION_PACKAGE, Context.CONTEXT_IGNORE_SECURITY)
        val floatConfig = AppCustomConfig.getFloatButtonConfig()
        if (floatConfig?.items == null || floatConfig.menu?.icon == null) {
            log("floatButton 主 icon 为空")
            return
        }
        val primaryColor = HookConfig.get_color_primary
        val actionMenu = FloatingActionMenu(context)
        actionMenu.menuButtonColorNormal = primaryColor
        actionMenu.menuButtonColorPressed = primaryColor
        val drawable: Bitmap? = AppCustomConfig.getIcon(floatConfig.menu!!.icon)
        if (drawable == null) {
            log("floatButton 主 icon 为空")
            return
        }
        actionMenu.setMenuIcon(BitmapDrawable(context.resources, drawable))
        actionMenu.initMenuButton()

        val floatItems = arrayListOf<FLoatButtonConfigItem>()
        floatConfig.items?.sortedBy { it.order }
                ?.forEach {
                    val drawable2: Bitmap? = AppCustomConfig.getIcon(it.icon)
                    if (drawable2 == null) {
                        log("${it.icon}不存在,忽略~")
                        return@forEach
                    }
                    floatItems.add(it)
                    getFloatButton(actionMenu, context, it.text,
                            BitmapDrawable(context.resources,
                                    AppCustomConfig.getScaleBitmap(drawable2)), primaryColor)
                }

        actionMenu.setFloatButtonClickListener { fab, index ->
            //log("click fab,index=" + index + ",label" + fab.getLabelText());
            onFloatButtonClick(floatItems[index], index)
        }

        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val margin = ConvertUtils.dp2px(contentLayout.context, 12f)
        params.rightMargin = margin
        params.bottomMargin = margin
        params.gravity = Gravity.END or Gravity.BOTTOM
        if (HookConfig.is_hook_float_button_move) {
            actionMenu.menuButton.setOnTouchListener(object : View.OnTouchListener {
                internal var lastX: Float = 0.toFloat()
                internal var lastY: Float = 0.toFloat()
                internal var downTime: Long = 0

                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        lastX = event.rawX
                        lastY = event.rawY
                        downTime = System.currentTimeMillis()
                    } else if (event.action == MotionEvent.ACTION_MOVE) {
                        val nowX = event.rawX
                        val nowY = event.rawY
                        val dx = (nowX - lastX).toInt().toFloat()
                        val dy = (nowY - lastY).toInt().toFloat()
                        lastX = nowX
                        lastY = nowY
                        actionMenu.x = actionMenu.x + dx
                        actionMenu.y = actionMenu.y + dy
                    } else if (event.action == MotionEvent.ACTION_UP) {
                        val nowTime = System.currentTimeMillis()
                        if (nowTime - downTime > 300) {
                            actionMenu.menuButton.isPressed = false
                            return true
                        }
                    }
                    return false
                }
            })
        }

        val backgroundView = View(context)
//        backgroundView.setBackgroundColor(Color.parseColor("#88000000"))
        val params2 = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        backgroundView.visibility = View.GONE
        backgroundView.setOnClickListener { view ->
            actionMenu.close(true)
            view.visibility = View.GONE
        }
        actionMenu.setOnMenuToggleListener { opened ->
            backgroundView.visibility = if (opened) View.VISIBLE else View.GONE
        }
        contentLayout.addView(backgroundView, params2)
        contentLayout.addView(actionMenu, params)
    }

    private fun getFloatButton(actionMenu: FloatingActionMenu, context: Context,
                               label: String, drawable: Drawable, primaryColor: Int): FloatingActionButton {
        val fab = FloatingActionButton(context)
        fab.setImageDrawable(drawable)
        fab.colorNormal = primaryColor
        fab.colorPressed = primaryColor
        fab.buttonSize = SIZE_MINI
        fab.labelText = label
        actionMenu.addMenuButton(fab)
        fab.setLabelColors(primaryColor, primaryColor, primaryColor)
        return fab
    }


    private fun onFloatButtonClick(item: FLoatButtonConfigItem, index: Int) {
    }

}