package com.inject.xie.processor.builder

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class KKModelBuilder(private val typeElement: TypeElement): BaseKKBuilder(typeElement) {


    override fun buildInternal(typeBuilder: TypeSpec.Builder) {
        val originClassName = ClassName.get(packageName, simpleName.toString())
        //通过builder方法返回实例
        val builderMethod = MethodSpec.methodBuilder("build")
                .returns(originClassName)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("\$T model = new \$T()", originClassName, originClassName)

        fields.forEach { field ->
            if (!field.isPrivate && !field.isProtected) {
                builderMethod.addStatement("model.\$L = this.\$L", field.name, field.name)
            }
        }
        builderMethod.addStatement("return model")
        typeBuilder.addMethod(builderMethod.build())
    }

}