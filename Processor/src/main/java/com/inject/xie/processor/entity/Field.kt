package com.inject.xie.processor.entity

import com.squareup.javapoet.ClassName
import com.sun.tools.javac.code.Symbol
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement

class Field(val ve: VariableElement) {

    //filed的名称
    val name = ve.simpleName.toString()

    val isPrivate = (ve as Symbol.VarSymbol).isPrivate

    val isProtected = ve.modifiers.contains(Modifier.PROTECTED)

    //获取变量的type名称
    fun asTypeName() = ClassName.get(ve.asType())



}