package com.blanke.mdwechat.markdown

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.blanke.mdwechat.R
import com.blanke.mdwechat.settings.api.APIManager
import com.blankj.utilcode.util.ToastUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import ru.noties.markwon.Markwon
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.il.AsyncDrawableLoader
import ru.noties.markwon.spans.SpannableTheme
import java.io.IOException

class MarkDownActivity : Activity() {

    companion object {
        val URL = "url"
        val TITLE = "title"
        fun start(act: Activity, title: String, url: String) {
            val intent = Intent(act, MarkDownActivity::class.java)
            intent.putExtra(URL, url)
            intent.putExtra(TITLE, title)
            act.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_down)
        actionBar.setDisplayHomeAsUpEnabled(true)
        val url = intent.getStringExtra(URL)
        val title = intent.getStringExtra(TITLE)
        actionBar.title = title
        val markdownView = findViewById<TextView>(R.id.tv_markdown)
        val pb = findViewById<View>(R.id.pb_loading)
        APIManager().get(url, object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showShort("加载失败：" + e?.message)
                finish()
            }

            override fun onResponse(call: Call?, response: Response?) {
                val content = response?.body()?.string() ?: ""
                runOnUiThread {
                    pb.visibility = View.GONE
                    val config = SpannableConfiguration
                            .builder(this@MarkDownActivity)
                            .asyncDrawableLoader(AsyncDrawableLoader.create())
                            .theme(SpannableTheme.builder().build())
                            .build()
                    Markwon.setMarkdown(markdownView, config, content)
                }
            }
        })
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }
}
