package com.inject.xie.processor.builder

import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.TypeElement

class EmptyBuilder(private val typeElement: TypeElement): BaseKKBuilder(typeElement) {
    override fun buildInternal(typeBuilder: TypeSpec.Builder) {
    }

}