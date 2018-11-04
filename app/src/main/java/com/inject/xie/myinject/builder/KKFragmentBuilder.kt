package com.inject.xie.myinject.builder

import android.os.Bundle
import android.app.Fragment
import android.util.Log
import com.inject.xie.annotation.BuilderFragment
import com.inject.xie.annotation.Fields
import java.lang.Exception

object KKFragmentBuilder {

    fun inject(fragment: Fragment?, savedInstanceState: Bundle?) {
        if (fragment == null) {
            return
        }
        if (!fragment.javaClass.isAnnotationPresent(BuilderFragment::class.java)) {
            //该fragment没有被Builder标注，跳过
            return
        }
        val extras: Bundle? = savedInstanceState ?: fragment.arguments
        var fields = fragment.javaClass.declaredFields
        fields.forEach { field ->
            if (field.isAnnotationPresent(Fields::class.java)) {
                val name = field.name
                try {
                    val access = field.isAccessible
                    if (!access) field.isAccessible = true
                    val value = KKActivityBuilder.getIntentExtra(extras, name)
                    if (value != null) {
                        field.set(fragment, KKActivityBuilder.getIntentExtra(extras, name))
                    } else {
                        Log.d("KKFragmentBuilder", "get value is null, continue~")
                    }

                    if (!access) field.isAccessible = false

                } catch (e: Exception) {
                    Log.e("KKFragmentBuilder", "error in -> ${e.message}")
                }
            }
        }
    }
 }