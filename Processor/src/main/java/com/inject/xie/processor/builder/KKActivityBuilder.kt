package com.inject.xie.processor.builder

import com.inject.xie.processor.utils.ClassType.Companion.CONTEXT
import com.inject.xie.processor.utils.ClassType.Companion.INTENT
import com.squareup.javapoet.*
import javax.lang.model.element.*

/**
 *
 * 主要用来解析生成对应的类
 *
 */
class KKActivityBuilder(typeElement: TypeElement): BaseKKBuilder(typeElement){

    companion object {
        const val POSIX = "Builder"
        const val CONST_POSIX = "FIELD_"
    }

    override fun buildInternal(typeBuilder: TypeSpec.Builder) {
        //对于Activity，需要创建fillIntent和start方法
        val intentMethod = MethodSpec.methodBuilder("fillIntent")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(INTENT.java, "intent")
        fields.forEach { field ->
            //给fillIntent方法添加元素
            intentMethod.addStatement("intent.putExtra(\$S, \$L)", field.name, field.name)
        }
        typeBuilder.addMethod(intentMethod.build())


        //start
        typeBuilder.addMethod(MethodSpec.methodBuilder("start")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CONTEXT.java, "context")
                .addStatement("Intent intent = new Intent(context, \$L.class)", simpleName)
                .addStatement("fillIntent(intent)")
                .addStatement("context.startActivity(intent)")
                .build())
    }

}