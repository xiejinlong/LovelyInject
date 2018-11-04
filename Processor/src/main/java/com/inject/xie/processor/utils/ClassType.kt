package com.inject.xie.processor.utils

import com.squareup.javapoet.TypeName
import javax.lang.model.type.TypeMirror

class ClassType(private val jvmClassName: String) {

    companion object {
        val INTENT = ClassType("android.content.Intent")
        val CONTEXT = ClassType("android.content.Context")
        val KKACTIVITY = ClassType("android.app.Activity")
        val KKFRAGMENT = ClassType("android.support.v4.app.Fragment")
        val KKMODEL = ClassType("android.app.KBuilderModel")
        val BUNDLE = ClassType("android.os.Bundle")
        val URI = ClassType("android.net.Uri")
    }

    val typeMirror: TypeMirror by lazy {
        getTypeFromClassName(jvmClassName).erasure()
    }

    val java: TypeName by lazy {
        typeMirror.asJavaTypeName()
    }

    fun TypeMirror.asJavaTypeName() = com.squareup.javapoet.TypeName.get(this)

    fun getTypeFromClassName(className: String) = ProcessorEnv.elements.getTypeElement(className).asType()

    fun TypeMirror.erasure() = ProcessorEnv.types.erasure(this)

    override fun toString(): String {
        return typeMirror.toString()
    }
}