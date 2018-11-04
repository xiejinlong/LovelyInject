package com.inject.xie.processor.builder

import com.inject.xie.processor.utils.ClassType
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class KKFragmentBuilder(private val typeElement: TypeElement): BaseKKBuilder(typeElement) {


    override fun buildInternal(typeBuilder: TypeSpec.Builder) {
        //fragment也需要fillIntent
        val intentMethod = MethodSpec.methodBuilder("fillIntent")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ClassType.INTENT.java, "intent")
        fields.forEach { field ->
            //给fillIntent方法添加元素
            intentMethod.addStatement("intent.putExtra(\$S, \$L)", field.name, field.name)
        }
        typeBuilder.addMethod(intentMethod.build())


        val originClassName = ClassName.get(packageName, simpleName.toString())
        //通过builder方法返回实例
        typeBuilder.addMethod(MethodSpec.methodBuilder("build")
                .returns(originClassName)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("\$T fragment = new \$T()", originClassName, originClassName)
                .addStatement("Intent intent = new Intent()")
                .addStatement("fillIntent(intent)")
                .addStatement("fragment.setArguments(intent.getExtras())")
                .addStatement("return fragment")
                .build())
    }

}