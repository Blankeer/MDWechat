package com.blanke.mdwechat.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.blanke.mdwechat.R
import com.blanke.mdwechat.WeChatHelper.startActivity
import com.blanke.mdwechat.WeChatHelper.startPluginActivity
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.bean.FLoatButtonConfigItem
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.C
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.config.WxObjects
import com.blanke.mdwechat.util.ConvertUtils
import com.blanke.mdwechat.widget.MaterialSearchView
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionButton.SIZE_MINI
import com.github.clans.fab.FloatingActionMenu
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by blanke on 2017/8/1.
 */

class MainHook : BaseHookUi() {
    private var searchView: WeakReference<MaterialSearchView>? = null
    private var searchKey: String? = null
    private var actionMenu: WeakReference<FloatingActionMenu>? = null
    private var contentFrameLayout: WeakReference<ViewGroup>? = null
    private var actionBar: WeakReference<Any>? = null
    private var tabLayout: WeakReference<CommonTabLayout>? = null

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        hookWechatMain(lpparam)
    }

    private fun hookWechatMain(lpparam: XC_LoadPackage.LoadPackageParam) {
        xMethod(wxConfig.classes.LauncherUI,
                wxConfig.methods.LauncherUI_startMainUI,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                        val activity = param.thisObject as Activity
                        log("LauncherUI_startMainUI LauncherUI=" + activity)
                        val isMainInit = XposedHelpers.getAdditionalInstanceField(activity, KEY_ISMAININIT)
                        if (isMainInit != null && isMainInit as Boolean) {
                            return
                        }
                        log("LauncherUI_startMainUI addView")
                        refreshPrefs()
                        //fix soft input ui move up
//                        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                        WxObjects.LauncherUI = WeakReference(activity)
                        val mHomeUi = getObjectField(activity, wxConfig.fields.LauncherUI_mHomeUi)
                        WxObjects.HomeUI = WeakReference(mHomeUi)

                        val mHomeUiTabHelper = XposedHelpers.getObjectField(mHomeUi, wxConfig.fields.HomeUi_mHomeUiTabHelper)
                        WxObjects.HomeUiTabHelper = WeakReference(mHomeUiTabHelper)

                        val viewPager = XposedHelpers.getObjectField(mHomeUiTabHelper, wxConfig.fields.HomeUiTabHelper_mViewPager)
                        WxObjects.HomeUI_ViewPager = WeakReference(viewPager)

                        if (viewPager !is View) {
                            log("HomeUI_ViewPager not is View,MainHook fail! viewPager=$viewPager")
                            return
                        }
                        val linearLayoutContent = viewPager.parent as ViewGroup
                        val contentFrameLayout = linearLayoutContent.parent as ViewGroup
                        this@MainHook.contentFrameLayout = WeakReference(contentFrameLayout)
                        //移除底部 tabview
                        val tabView = linearLayoutContent.getChildAt(1)
                        if (tabView == null || tabView !is RelativeLayout) {
                            log("HomeUI_tabView not found,MainHook fail! tabView=$tabView")
                            return
                        }
                        WxObjects.LauncherUIBottomTabView = WeakReference(tabView)
                        if (HookConfig.is_hook_hide_wx_tab) {
                            linearLayoutContent.removeView(tabView)
                        }
                        //ActionBar hide
                        val actionBar = XposedHelpers.getObjectField(mHomeUi, wxConfig.fields.HomeUI_mActionBar)
                        this@MainHook.actionBar = WeakReference(actionBar)
                        if (actionBar != null && HookConfig.is_hook_hide_actionbar) {
                            XposedHelpers.callMethod(actionBar, "hide")
                        }

                        //add searchView
                        if (HookConfig.is_hook_search) {
                            if (actionBar != null) {
                                val actionView = XposedHelpers.callMethod(actionBar, "getCustomView") as View
                                val actionLayout = actionView.parent?.parent
                                if (actionLayout != null) {
                                    addSearchView(actionLayout as FrameLayout, lpparam)
                                }
                            }
                        }

                        //add tabLayout
                        if (HookConfig.is_hook_tab) {
                            addTabLayout(linearLayoutContent, viewPager)
                        }

                        //add float Button
                        if (HookConfig.is_hook_float_button) {
                            addFloatButton(contentFrameLayout)
                        }

                        XposedHelpers.setAdditionalInstanceField(activity, KEY_ISMAININIT, true)
                    }
                })
        //hide main actionBar, change paddingTop = 0
        xMethod(View::class.java, "setPadding", C.Int, C.Int, C.Int
                , C.Int, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                val view = param?.thisObject as View
                if (view == contentFrameLayout?.get() && HookConfig.is_hook_hide_actionbar) {
//                  log("contentFrameLayout padding=${param.args[1]}")
                    param.args[1] = 0
                }
            }
        })

        //search hook when click menuItem
        xMethod(wxConfig.classes.LauncherUI,
                "onOptionsItemSelected", C.MenuItem, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                if (!HookConfig.is_hook_search) {
                    return
                }
                val menuItem = param!!.args[0] as MenuItem
                if (menuItem.itemId == 1) {//search
                    //log("hook launcherUi click action search icon success");
                    val sview = searchView?.get()
                    if (sview != null) {
                        sview.show()
                        param.result = true
                    }
                }
            }
        })
        // search searchKey hook
        XposedHelpers.findAndHookConstructor(wxConfig.classes.ActionBarEditText,
                lpparam.classLoader, C.Context, C.AttributeSet,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                        if (!HookConfig.is_hook_search) {
                            return
                        }
                        if (!TextUtils.isEmpty(searchKey)) {
                            val editText = param.thisObject as EditText
//                                log("ActionBarEditText context=${editText.context}")
                            editText.postDelayed({
                                editText.setText(searchKey)
                                editText.setSelection(searchKey!!.length)
                                searchKey = null
                            }, 300)
                        }
                    }
                })

        //hook viewPager OnPageChangeListener
        xMethod(wxConfig.classes.HomeUiViewPagerChangeListener,
                wxConfig.methods.HomeUiViewPagerChangeListener_onPageScrolled,
                C.Int, C.Float, C.Int, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                if (!HookConfig.is_hook_tab) {
                    return
                }
                val tabLayout = this@MainHook.tabLayout?.get() ?: return
                val positionOffset = param!!.args[1] as Float
                val position = param.args[0] as Int
                tabLayout.startScrollPosition = position
                tabLayout.indicatorOffset = positionOffset
            }
        })
        xMethod(wxConfig.classes.HomeUiViewPagerChangeListener,
                wxConfig.methods.HomeUiViewPagerChangeListener_onPageSelected,
                C.Int, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                if (!HookConfig.is_hook_tab) {
                    return
                }
                val tabLayout = this@MainHook.tabLayout?.get() ?: return
                val position = param!!.args[0] as Int
                tabLayout.currentTab = position
            }
        })
        // hook tab unread msg
        xMethod(wxConfig.classes.LauncherUIBottomTabView,
                wxConfig.methods.LauncherUIBottomTabView_setMainTabUnread,
                C.Int, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                if (!HookConfig.is_hook_tab) {
                    return
                }
                val tabLayout = this@MainHook.tabLayout?.get() ?: return
                //log("msg unread=" + param.args[0]);
                tabLayout.showMsg(0, param!!.args[0] as Int)
            }
        })
        xMethod(wxConfig.classes.LauncherUIBottomTabView,
                wxConfig.methods.LauncherUIBottomTabView_setContactTabUnread,
                C.Int, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                if (!HookConfig.is_hook_tab) {
                    return
                }
                val tabLayout = this@MainHook.tabLayout?.get() ?: return
                //log("contact unread=" + param.args[0]);
                tabLayout.showMsg(1, param!!.args[0] as Int)
            }
        })
        xMethod(wxConfig.classes.LauncherUIBottomTabView,
                wxConfig.methods.LauncherUIBottomTabView_setFriendTabUnread,
                C.Int, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                if (!HookConfig.is_hook_tab) {
                    return
                }
                val tabLayout = this@MainHook.tabLayout?.get() ?: return
                //log("friend unread=" + param.args[0]);
                tabLayout.showMsg(2, param!!.args[0] as Int)
            }
        })
        xMethod(wxConfig.classes.LauncherUIBottomTabView,
                wxConfig.methods.LauncherUIBottomTabView_showFriendTabUnreadPoint,
                C.Boolean, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                if (!HookConfig.is_hook_tab) {
                    return
                }
                val tabLayout = this@MainHook.tabLayout?.get() ?: return
                val show = param!!.args[0] as Boolean
                tabLayout.showMsg(2, if (show) -1 else 0)
            }
        })

        //hide more icon in actionBar
        xMethod(wxConfig.classes.LauncherUI, "onCreateOptionsMenu", C.Menu, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                if (!HookConfig.is_hook_float_button) {
                    return
                }
                //                log("homeUI=" + mHomeUi);
                val mHomeUi = WxObjects.HomeUI?.get()
                if (mHomeUi != null) {
                    //hide actionbar more add icon
                    val moreMenuItem = getObjectField(mHomeUi, wxConfig.fields.HomeUI_mMoreMenuItem) as MenuItem
                    //log("moreMenuItem=" + moreMenuItem);
                    moreMenuItem.isVisible = false
                }
            }
        })

        //hook onKeyEvent
        xMethod(wxConfig.classes.LauncherUI, "dispatchKeyEvent", C.KeyEvent, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
                if (!HookConfig.is_hook_search && !HookConfig.is_hook_float_button) {
                    return
                }
                val keyEvent = param.args[0] as KeyEvent
                if (keyEvent.keyCode == KeyEvent.KEYCODE_MENU && HookConfig.is_hook_search) {//hide menu
                    param.result = true
                    return
                }
                if (keyEvent.keyCode == KeyEvent.KEYCODE_BACK) {//hook back
                    if (searchView?.get() == null) {
                        return
                    }
                    if (searchView!!.get()!!.isSearchViewVisible) {
                        searchView?.get()?.hide()
                        param.result = true
                        return
                    }
                    if (actionMenu?.get() == null) {
                        return
                    }
                    if (actionMenu!!.get()!!.isOpened) {
                        actionMenu?.get()?.close(true)
                        param.result = true
                        return
                    }
                }
            }
        })
    }

    private fun onFloatButtonClick(item: FLoatButtonConfigItem, index: Int) {
        when (item.type) {
            "search" -> startPluginActivity(wxConfig.classes.FTSMainUI)
            "timeline" -> startPluginActivity(wxConfig.classes.SnsTimeLineUI)
            "walletcoin" -> startPluginActivity(wxConfig.classes.WalletOfflineCoinPurseUI)
            "scan" -> startPluginActivity(wxConfig.classes.BaseScanUI)
            "addfriend" -> startPluginActivity(wxConfig.classes.AddMoreFriendsUI)
            "appbrand" -> startPluginActivity(wxConfig.classes.AppBrandLauncherUI)
            "mywallet" -> startPluginActivity(wxConfig.classes.MallIndexUI)
            "favorite" -> startPluginActivity(wxConfig.classes.FavoriteIndexUI)
            "chatgroup" -> {
                val intent = Intent()
                intent.putExtra("titile", "")
                intent.putExtra("list_type", 0)
                intent.putExtra("scene", 7)
                intent.putExtra("list_attr", 4951)
                startActivity(intent, wxConfig.classes.SelectContactUI)
            }
            else -> startPluginActivity(item.type)
        }
    }

    private fun gotoSearchActivity() {
        startPluginActivity(wxConfig.classes.FTSMainUI)
    }

    private fun addFloatButton(frameLayout: ViewGroup) {
        val context = WxObjects.MdContext?.get() ?: return
        val floatConfig = AppCustomConfig.getFloatButtonConfig()
//        log("floatconfig=$floatConfig")
        if (floatConfig == null || floatConfig.items == null || floatConfig.menu?.icon == null) {
            log("floatButton 主 icon 为空")
            return
        }
        val primaryColor = colorPrimary
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
                    addFloatButton(actionMenu, context, it.text,
                            BitmapDrawable(context.resources,
                                    AppCustomConfig.getScaleBitmap(drawable2)), primaryColor)
                }

        actionMenu.setFloatButtonClickListener { fab, index ->
            //log("click fab,index=" + index + ",label" + fab.getLabelText());
            onFloatButtonClick(floatItems[index], index)
        }

        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val margin = ConvertUtils.dp2px(frameLayout.context, 12f)
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
        frameLayout.addView(backgroundView, params2)
        frameLayout.addView(actionMenu, params)
        this.actionMenu = WeakReference(actionMenu)
    }

    private fun addFloatButton(actionMenu: FloatingActionMenu, context: Context,
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

    private fun addSearchView(frameLayout: ViewGroup, lpparam: XC_LoadPackage.LoadPackageParam) {
        if (WxObjects.LauncherUI?.get() == null) {
            return
        }
        val context = WxObjects.MdContext?.get() as Context
        val searchView = MaterialSearchView(context)
        searchView.setOnSearchListener(object : MaterialSearchView.SimpleonSearchListener() {
            override fun onSearch(query: String) {
                searchKey = query
                searchView.hide()
                gotoSearchActivity()
            }
        })
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        frameLayout.addView(searchView, params)
        this.searchView = WeakReference(searchView)
    }

    private fun addTabLayout(viewPagerLinearLayout: ViewGroup, pager: View) {
        if (WxObjects.LauncherUI?.get() == null) {
            return
        }
        val context = WxObjects.MdContext?.get() as Context
        val tabLayout = CommonTabLayout(context)
        val primaryColor = colorPrimary
        tabLayout.setBackgroundColor(primaryColor)
        tabLayout.textSelectColor = Color.WHITE
        tabLayout.textUnselectColor = 0x1acccccc
        val dp2 = ConvertUtils.dp2px(pager.context, 1f)
        tabLayout.indicatorHeight = dp2.toFloat()
        tabLayout.indicatorColor = Color.WHITE
        tabLayout.indicatorCornerRadius = dp2.toFloat()
        tabLayout.indicatorAnimDuration = 200
        tabLayout.elevation = if (HookConfig.is_hook_tab_elevation) 5F else 0F
        tabLayout.textsize = context.getResources().getDimension(R.dimen.tabTextSize)
        tabLayout.unreadBackground = Color.WHITE
        tabLayout.unreadTextColor = primaryColor
        tabLayout.selectIconColor = Color.WHITE
        tabLayout.unSelectIconColor = 0x1aaaaaaa

        val mTabEntities = intArrayOf(0, 1, 2, 3)
                .map { it }
                .mapTo(ArrayList<CustomTabEntity>()) {
                    object : CustomTabEntity.TabCustomData() {
                        override fun getTabIcon(): Drawable {
                            return BitmapDrawable(context.resources, AppCustomConfig.getTabIcon(it))
                        }
                    }
                }
        tabLayout.setTabData(mTabEntities)
        tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                //log("tab click position=" + position);
                XposedHelpers.callMethod(pager, wxConfig.methods.WxViewPager_setCurrentItem, position)
            }

            override fun onTabReselect(position: Int) {
            }
        })
        // hook touch to LauncherUIBottomTabView
        val tabView = WxObjects.LauncherUIBottomTabView?.get()
        if (tabView != null) {
            val tabBottomView = tabView as ViewGroup
            val tabBottomContentView = tabBottomView.getChildAt(0) as ViewGroup?
            val tabViewTouchListener = View.OnTouchListener { view: View, motionEvent: MotionEvent ->
                //                log("tabView touch $view , $motionEvent")
                val index = view.tag as Int?
                tabBottomContentView?.getChildAt(index ?: 0)?.onTouchEvent(motionEvent)
                return@OnTouchListener false
            }
            tabLayout.tabViews.forEach { it.setOnTouchListener(tabViewTouchListener) }
        }

        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.height = ConvertUtils.dp2px(viewPagerLinearLayout.context, 48f)
        viewPagerLinearLayout.addView(tabLayout, 0, params)
        this.tabLayout = WeakReference(tabLayout)
    }

    companion object {
        private val KEY_ISMAININIT = "KEY_ISMAININIT"
    }
}
