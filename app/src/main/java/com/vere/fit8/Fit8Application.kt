package com.vere.fit8

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 燃力8周 - 应用程序入口
 * 初始化Hilt依赖注入框架
 */
@HiltAndroidApp
class Fit8Application : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // 初始化ThreeTenABP用于日期时间处理
        com.jakewharton.threetenabp.AndroidThreeTen.init(this)
    }
}
