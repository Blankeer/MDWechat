package com.blanke.mdwechat.settings.view

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.widget.ListView
import android.widget.SimpleAdapter
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.R
import com.blanke.mdwechat.settings.api.APIManager
import com.blanke.mdwechat.settings.bean.WechatConfig
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


object DownloadWechatDialog {

    fun show(context: Activity) {
        val progressDialog = ProgressDialog.show(context, "加载中", "正在下载微信配置列表", true, false)
        APIManager().getWechatConfigs(
                object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        progressDialog.dismiss()
                        ToastUtils.showLong("下载微信配置列表失败," + e?.message)
                    }

                    override fun onResponse(call: Call?, response: Response) {
                        progressDialog.dismiss()
                        val data = Gson().fromJson<List<WechatConfig>>(response.body()?.string(), object : TypeToken<List<WechatConfig>>() {}.type)
                        val lists = mutableListOf<Map<String, String>>()
                        for (item in data) {
                            val map = mutableMapOf<String, String>()
                            map["name"] = item.name
                            map["desc"] = item.desc
                            lists.add(map)
                        }
                        val adapter = SimpleAdapter(
                                context,
                                lists,
                                R.layout.item_wechat_config,
                                arrayOf("name", "desc"),
                                intArrayOf(R.id.tv_name, R.id.tv_desc)
                        )
                        val listView = ListView(context)
                        listView.setPadding(10, 0, 10, 20)
                        listView.adapter = adapter
                        context.runOnUiThread {
                            val dialog = AlertDialog.Builder(context)
                                    .setTitle("在线微信配置文件列表")
                                    .setView(listView)
                                    .show()
                            listView.setOnItemClickListener { parent, view, position, id ->
                                dialog.dismiss()
                                val item = data[position]
                                APIManager().downloadWechatConfig(item.url, object : Callback {
                                    override fun onFailure(call: Call?, e: IOException?) {
                                        ToastUtils.showLong("下载微信配置文件失败," + e?.message)
                                    }

                                    override fun onResponse(call: Call?, response: Response) {
                                        val outputPath = Common.APP_DIR_PATH + Common.CONFIG_WECHAT_DIR
                                        val succ = FileIOUtils.writeFileFromString("$outputPath/${item.name}", response.body()?.string())
                                        ToastUtils.showLong("下载微信配置文件${item.name}${if (succ) "成功" else "失败"}")
                                    }
                                })
                            }
                        }

                    }
                }
        )

    }
}