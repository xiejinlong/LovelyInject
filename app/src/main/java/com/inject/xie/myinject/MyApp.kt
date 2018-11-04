package com.inject.xie.myinject

import android.app.Application
import com.inject.xie.myinject.builder.KKActivityBuilder

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        KKActivityBuilder.register(this)
    }
}