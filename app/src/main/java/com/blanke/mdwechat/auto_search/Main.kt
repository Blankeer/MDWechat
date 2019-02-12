package com.blanke.mdwechat.auto_search

import android.content.Context
import com.blanke.mdwechat.Version
import com.blanke.mdwechat.auto_search.bean.OutputJson
import com.blankj.utilcode.util.FileIOUtils
import com.google.gson.Gson
import dalvik.system.DexClassLoader
import net.dongliu.apk.parser.ApkFile
import net.dongliu.apk.parser.bean.DexClass
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.concurrent.thread


/**
 * Created by blanke on 2018/7/14.
 */
class Main {
    private var versionName = ""
    private var versionCode = 0L
    private lateinit var wxClasses: List<DexClass>

    fun main(context: Context, path: String, outputPath: String) {
        thread {
            execute(context, path, outputPath)
        }
    }

    private fun execute(context: Context, path: String, outputPath: String) {
        Logs.i("开始解析微信安装包:$path")
        val wechatApkFile = ApkFile(path)
        val apkMeta = wechatApkFile.apkMeta
        versionName = apkMeta.versionName
        versionCode = apkMeta.versionCode
        val wechatInfo = "versionName:$versionName,versionCode:$versionCode"
        Logs.i("版本信息 $wechatInfo")

        wxClasses = wechatApkFile.dexClasses.toList()
        Logs.i("class 总数= ${wxClasses.size}")

        val optimizedDirectoryFile = context.getDir("dex", 0)
        val classLoader = DexClassLoader(path, optimizedDirectoryFile.absolutePath, null, ClassLoader.getSystemClassLoader())
        WechatGlobal.wxPackageName = apkMeta.packageName
        WechatGlobal.wxVersion = Version(versionName)
        WechatGlobal.wxLoader = classLoader
        WechatGlobal.wxClasses = wxClasses.map { it.classType }

        Logs.i("开始解析 classes")
        var errorCount = 0
        val classesMap = mutableMapOf<String, String>()
        Classes::class.java.methods.forEach {
            var methodName = it.name
            if (!methodName.startsWith("get") || methodName == "getClass") {
                return@forEach
            }
            methodName = methodName.substring("get".length)
            try {
                val res = it.invoke(Classes)
                if (res is Class<*>) {
                    val result = (res as Class<*>?)?.name
                            ?: throw  NullPointerException("$methodName = null")
                    Logs.i("$methodName = $result")
                    classesMap.put(methodName, result)
                } else {
                    throw  NullPointerException("$methodName != class")
                }
            } catch (e: Exception) {
                Logs.i("$methodName 解析失败,${e}")
                e.printStackTrace()
                errorCount++
            }
        }
        if (errorCount > 0) {
            Logs.i("有 class 解析失败")
            return
        }
        Logs.i("解析完成，class 数量 = ${classesMap.size}")

        Logs.i("开始解析 fields")
        errorCount = 0
        val fieldMap = mutableMapOf<String, Any>()
        Fields::class.java.methods.forEach {
            var fieldName = it.name
            if (!fieldName.startsWith("get") || fieldName == "getClass") {
                return@forEach
            }
            fieldName = fieldName.substring("get".length)
            try {
                val res = it.invoke(Fields)
                if (res is Field) {
                    val result = res.name ?: throw  NullPointerException("$fieldName = null")
                    Logs.i("$fieldName = $result")
                    fieldMap.put(fieldName, result)
                } else if (res is List<*>) {
                    val result = res.filter { it is Field }
                            .map {
                                (it as Field).name
                                        ?: throw  NullPointerException("$fieldName = null")
                            }
                    Logs.i("$fieldName = $result")
                    fieldMap.put(fieldName, result)
                } else {
                    throw  NullPointerException("$fieldName != Field")
                }
            } catch (e: Exception) {
                Logs.i("$fieldName 解析失败,${e}")
                e.printStackTrace()
                errorCount++
            }
        }
        if (errorCount > 0) {
            Logs.i("有 field 解析失败")
            return
        }
        Logs.i("解析完成，field 数量 = ${fieldMap.size}")

        Logs.i("开始解析 method")
        errorCount = 0
        val methodMap = mutableMapOf<String, Any>()
        Methods::class.java.methods.forEach {
            var methodName = it.name
            if (!methodName.startsWith("get") || methodName == "getClass") {
                return@forEach
            }
            methodName = methodName.substring("get".length)
            try {
                val res = it.invoke(Methods)
                if (res is Method) {
                    val result = res.name ?: throw  NullPointerException("$methodName = null")
                    Logs.i("$methodName = $result")
                    methodMap.put(methodName, result)
                } else if (res is List<*>) {
                    val result = res.filter { it is Method }
                            .map {
                                (it as Method).name
                                        ?: throw  NullPointerException("$methodName = null")
                            }
                    Logs.i("$methodName = $result")
                    methodMap.put(methodName, result)
                } else {
                    throw  NullPointerException("$methodName != Method,$res")
                }
            } catch (e: Exception) {
                Logs.i("$methodName 解析失败,${e}")
                e.printStackTrace()
                errorCount++
            }
        }
        if (errorCount > 0) {
            Logs.i("有 method 解析失败")
            return
        }
        Logs.i("解析完成，method 数量 = ${methodMap.size}")

        val outputJson = OutputJson(classesMap, methodMap, fieldMap)
        val json = Gson().toJson(outputJson)
        val op = outputPath + "/${versionName}.config"
        val succ = FileIOUtils.writeFileFromString(op, json)
        Logs.i("保存到文件状态:${succ}，$op")

    }
}
