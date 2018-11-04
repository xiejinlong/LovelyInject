package com.inject.xie.processor.builder

import com.inject.xie.annotation.BuilderActivity
import com.inject.xie.processor.utils.ClassType
import com.inject.xie.processor.utils.LogUtils
import com.inject.xie.processor.utils.ProcessorEnv
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import java.io.IOException
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier

class RouterBuilder {

    companion object {
        fun generationActivityRouter(classMap: HashMap<Element, BaseKKBuilder>) {
            val routerClassBuilder = TypeSpec.classBuilder("ActivityRouter").addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            LogUtils.warn("generationActivityRouter")
            classMap.keys.forEach { element ->
                if (!ProcessorEnv.types.isSubtype(element.asType(), ClassType.KKACTIVITY.typeMirror)) {
                    return@forEach
                }

                val scheme = element.getAnnotation(BuilderActivity::class.java).routerValue
                if (scheme.isEmpty()) {
                    return@forEach
                }
                val startAcMethod = MethodSpec.methodBuilder("startAc${element.simpleName}")
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ClassType.CONTEXT.java, "context")
                        .addStatement("\$T intent = new \$T(Intent.ACTION_VIEW, \$T.parse(\$S))", ClassType.INTENT.java, ClassType.INTENT.java, ClassType.URI.java,scheme)
                classMap[element]?.fields?.forEach { field ->
                    startAcMethod
                            .addParameter(field.asTypeName(), field.name)
                            .addStatement("intent.putExtra(\$S, \$L)", field.name, field.name)
                }

                startAcMethod.addStatement("context.startActivity(intent)")
                routerClassBuilder.addMethod(startAcMethod.build())
                LogUtils.warn("generationActivityRouter builder write")

            }
            writeJavaToFile(routerClassBuilder.build())
        }

        private fun writeJavaToFile(typeSpec: TypeSpec) {
            try {
                val file = JavaFile.builder("com.kuaikan.framework", typeSpec).build()
                file.writeTo(ProcessorEnv.filer)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}