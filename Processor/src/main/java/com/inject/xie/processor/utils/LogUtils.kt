package com.inject.xie.processor.utils

import com.inject.xie.processor.utils.ProcessorEnv.messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic

object LogUtils {

    fun warn(element: Element, message: String, vararg args: Any) {
        printMessage(Diagnostic.Kind.WARNING, element, message, *args)
    }

    fun warn(message: String, vararg args: Any) {
        printMessage(Diagnostic.Kind.WARNING, null, message, *args)
    }

    fun error(element: Element, message: String, vararg args: Any) {
        printMessage(Diagnostic.Kind.ERROR, element, message, *args)
    }

    private fun printMessage(kind: Diagnostic.Kind, element: Element?, message: String, vararg args: Any) {
        if(element == null){
            messager.printMessage(Diagnostic.Kind.WARNING, if (args.isNotEmpty()) { String.format(message, *args) } else message)
        } else {
            messager.printMessage(kind,
                    if (args.isNotEmpty()) {
                        String.format(message, *args)
                    } else message
                    , element)
        }
    }
}