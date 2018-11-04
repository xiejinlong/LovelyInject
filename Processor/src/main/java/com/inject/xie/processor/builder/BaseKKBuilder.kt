package com.inject.xie.processor.builder

import com.inject.xie.processor.entity.Field
import com.inject.xie.processor.utils.LogUtils
import com.inject.xie.processor.utils.ProcessorEnv
import com.squareup.javapoet.*
import java.io.IOException
import javax.lang.model.element.*

abstract class BaseKKBuilder(private val baseElement: TypeElement) {

    open val fields = ArrayList<Field>()

    //类型
    val simpleName = baseElement.simpleName!!

    //生成文件的名称,需要解析是否是内部类
    val builderClassName: String by lazy {
        val list = ArrayList<String>()
        list.add(baseElement.simpleName.toString())
        var element = baseElement.enclosingElement
        while (element != null && element.kind != ElementKind.PACKAGE){
            list.add(element.simpleName.toString())
            element = element.enclosingElement
        }
        list.reversed().joinToString("_") + KKActivityBuilder.POSIX
    }

    //包名
    val packageName: String = baseElement.packageName()

    fun TypeElement.packageName(): String {
        var element = this.enclosingElement
        while (element != null && element.kind != ElementKind.PACKAGE) {
            element = element.enclosingElement
        }
        return element?.asType()?.toString() ?: throw IllegalArgumentException("$this does not have an enclosing element of package.")
    }

    //是否是抽象类
    val isAbstract = baseElement.modifiers.contains(Modifier.ABSTRACT)

    //基类做一些需要的创建
    fun build() {

        LogUtils.warn("ClassBuilder", "start builder")
        if (isAbstract) return
        //创建class
        val classFileBuilder = TypeSpec.classBuilder(builderClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)


        val builderClassTypeName = ClassName.get(packageName, builderClassName)

        //构造主builder
        classFileBuilder.addMethod(MethodSpec.methodBuilder("builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(builderClassTypeName)
                .addStatement("\$T builder = new \$T()", builderClassTypeName, builderClassTypeName)
                .addStatement("return builder").build())

        fields.forEach { field ->
            LogUtils.warn("fieldBuilder ${field.name}")
            //构造临时变量
            classFileBuilder.addField(FieldSpec.builder(field.asTypeName(), field.name, Modifier.PRIVATE).build())

            //构造变量相关的静态变量
            classFileBuilder.addField(FieldSpec.builder(String::class.java, KKActivityBuilder.CONST_POSIX + field.name.toUpperCase())
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("\$S", field.name)
                    .build())



            //构造相关变量的builder方法
            classFileBuilder.addMethod(MethodSpec.methodBuilder(field.name)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(field.asTypeName(), field.name)
                    .addStatement("this.${field.name} = ${field.name}")
                    .addStatement("return this")
                    .returns(builderClassTypeName)
                    .build())
        }
        buildInternal(classFileBuilder)
        writeJavaToFile(classFileBuilder.build())
    }

    abstract fun buildInternal(typeBuilder: TypeSpec.Builder)

    private fun writeJavaToFile(typeSpec: TypeSpec) {
        try {
            val file = JavaFile.builder(packageName, typeSpec).build()
            file.writeTo(ProcessorEnv.filer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun addFiled(element: Element) {
        fields.add(Field(element as VariableElement))
    }
}