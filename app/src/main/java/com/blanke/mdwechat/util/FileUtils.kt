package com.blanke.mdwechat.util

import android.content.Context
import java.io.*

/**
 * Created by blanke on 2017/10/11.
 */
object FileUtils {
    fun copyAssets(context: Context, appDir: String, dir: String, cover: Boolean = false) {
        val assetManager = context.getAssets()
        var files: Array<String>? = null
        try {
            files = assetManager.list(dir)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (files != null) {
            File(appDir + File.separator + dir + File.separator).mkdirs()
            for (filename in files) {
                var `in`: InputStream? = null
                var out: OutputStream? = null
                try {
                    `in` = assetManager.open(dir + File.separator + filename)
                    val outFile = File(appDir + File.separator + dir + File.separator + filename)
                    if (outFile.exists()) {
                        if (!cover) {
                            continue
                        } else {
                            outFile.delete()
                        }
                    }
                    out = FileOutputStream(outFile)
                    copyFile(`in`, out)
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    if (`in` != null) {
                        try {
                            `in`!!.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                    if (out != null) {
                        try {
                            out!!.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    fun copyFile(`in`: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        do {
            read = `in`.read(buffer)
            if (read == -1) {
                break
            }
            out.write(buffer, 0, read)
        } while (true)
    }
}