package com.inject.xie.myinject.builder

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.inject.xie.annotation.BuilderActivity
import com.inject.xie.annotation.Fields
import java.lang.Exception
import java.lang.reflect.Field

object KKActivityBuilder {

    fun register(application: Application) {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                if (activity == null) {
                    return
                }
                if (!activity.javaClass.isAnnotationPresent(BuilderActivity::class.java)) {
                    //该activity没有被Builder标注，跳过
                    return
                }
                Log.d("KKActivityBuilder", "onActivitySaveInstanceState~")
                val extra: Bundle = outState ?: activity.intent.extras?:return
                var fields = activity.javaClass.declaredFields
                inject(activity, fields, extra)
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                if (activity == null) {
                    return
                }
                if (!activity.javaClass.isAnnotationPresent(BuilderActivity::class.java)) {
                    //该activity没有被Builder标注，跳过
                    return
                }
                Log.d("KKActivityBuilder", "onActivityCreated~")
                val intent = activity.intent ?: return
                var fields = activity.javaClass.declaredFields
                inject(activity, fields, intent.extras)
            }

        })
    }

    fun inject(activity: Activity?, fields: Array<Field>?, extras: Bundle?) {
        if (fields == null) {
            Log.d("KKActivityBuilder", "declaredFields is null, should return~")
            return
        }
        fields.forEach { field ->
            if (field.isAnnotationPresent(Fields::class.java)) {
                val name = field.name
                try {
                    val access = field.isAccessible
                    if (!access) field.isAccessible = true
                    val value = getIntentExtra(extras, name)
                    if (value != null) {
                        field.set(activity, getIntentExtra(extras, name))
                    } else {
                        Log.d("KKActivityBuilder", "get value is null, continue~")
                    }

                    if (!access) field.isAccessible = false

                } catch (e: Exception) {
                    Log.e("KKActivityBuilder", "error in -> ${e.message}")
                }
            }
        }
    }

    fun  getIntentExtra(extras: Bundle?, name: String): Any? {
        return extras?.get(name)
    }

}