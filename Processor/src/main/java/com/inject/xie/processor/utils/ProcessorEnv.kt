package com.inject.xie.processor.utils

import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

object ProcessorEnv {
    //java类型工具
    lateinit var types: Types
    //注解获取出来的元素
    lateinit var elements: Elements
    //消息输出
    lateinit var messager: Messager
    //文件写入
    lateinit var filer: Filer

    fun init(env: ProcessingEnvironment){
        elements = env.elementUtils
        types = env.typeUtils
        messager = env.messager
        filer = env.filer
    }
}
