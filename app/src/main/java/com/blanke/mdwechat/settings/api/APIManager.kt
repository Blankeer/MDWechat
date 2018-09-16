package com.blanke.mdwechat.settings.api

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

class APIManager {
    private val host = "https://raw.githubusercontent.com/Blankeer/MDWechat/v3.0/"

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    fun getWechatConfigs(callback: Callback) {
        val req = Request.Builder().url("${host}data/config/wechat/data.json")
                .get()
                .build()
        client.newCall(req).enqueue(callback)
    }

    fun downloadWechatConfig(url: String, callback: Callback) {
        get(url, callback)
    }

    fun get(url: String, callback: Callback) {
        val req = Request.Builder().url(url)
                .get()
                .build()
        client.newCall(req).enqueue(callback)
    }
}