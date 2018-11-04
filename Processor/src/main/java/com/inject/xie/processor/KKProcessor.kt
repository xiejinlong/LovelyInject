package com.inject.xie.processor

import com.google.auto.common.SuperficialValidation
import com.inject.xie.annotation.BuilderActivity
import com.inject.xie.annotation.BuilderFragment
import com.inject.xie.annotation.BuilderModel
import com.inject.xie.annotation.Fields
import com.inject.xie.processor.builder.*
import com.inject.xie.processor.utils.ClassType
import com.inject.xie.processor.utils.ProcessorEnv
import com.inject.xie.processor.utils.LogUtils
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class KKProcessor : AbstractProcessor() {

    private val supportedAnnotations = setOf(BuilderActivity::class.java,
            BuilderFragment::class.java, BuilderModel::class.java, Fields::class.java)

    //需要需要添加支持的被注解的
    private val supportedClassType = setOf(ClassType.KKACTIVITY, ClassType.KKFRAGMENT, ClassType.KKMODEL)

    val classMap = HashMap<Element, BaseKKBuilder>()

    @Synchronized
    override fun init(p0: ProcessingEnvironment?) {
        super.init(p0)
        ProcessorEnv.init(p0!!)
        LogUtils.warn("KKProcessor init")
    }

    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        LogUtils.warn("KKProcessor process")
        parasClass(p1!!)
        parasFiled(p1)
        build()
        return true
    }

    //开始遍历生成对应的builder类
    fun build() {
        classMap.values.forEach { it.build() }
        RouterBuilder.generationActivityRouter(classMap)
    }


    private fun parasClass(env: RoundEnvironment) {
        env.getElementsAnnotatedWith(BuilderActivity::class.java)
                .asSequence()
                .filter(SuperficialValidation::validateElement)
                .filter { it.kind.isClass }
                .toList()
                .forEach { element ->
                    LogUtils.warn("KKProcessor parasClass ${element.simpleName} is Activity~")
                    if (ProcessorEnv.types.isSubtype(element.asType(), ClassType.KKACTIVITY.typeMirror)) {
                        classMap[element] = KKActivityBuilder(element as TypeElement)
                    }
                }

        env.getElementsAnnotatedWith(BuilderFragment::class.java)
                .asSequence()
                .filter(SuperficialValidation::validateElement)
                .filter { it.kind.isClass }
                .toList()
                .forEach { element ->
                    LogUtils.warn("KKProcessor parasClass ${element.simpleName} is Fragment~")
                    classMap[element] = KKFragmentBuilder(element as TypeElement)

                }

        env.getElementsAnnotatedWith(BuilderModel::class.java)
                .asSequence()
                .filter(SuperficialValidation::validateElement)
                .filter { it.kind.isClass }
                .toList()
                .forEach { element ->
                    LogUtils.warn("KKProcessor parasClass ${element.simpleName} is Model~")
                    classMap[element] = KKModelBuilder(element as TypeElement)
                }
    }

    private fun parasFiled(env: RoundEnvironment) {
        env.getElementsAnnotatedWith(Fields::class.java)
                .asSequence()
                .filter(SuperficialValidation::validateElement)
                .filter { it.kind.isField }
                .toList()
                .forEach { element ->
                    LogUtils.warn("KKProcessor parasFiled ${element.simpleName}")
                    classMap[element.enclosingElement]?.addFiled(element)
                }
    }


    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_7
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return supportedAnnotations.mapTo(HashSet<String>(), Class<*>::getCanonicalName)
    }
}