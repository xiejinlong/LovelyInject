package com.inject.xie.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 标志需要依赖注入的field，
 * 可以修饰Activity和Fragment，目前仅支持Intent支持的类型，基本数据类型+parcel类型
 * 可以修饰KBuilderModel的子类，自动生成Builder类
 * 如果是model类，field必须是public且是java
 *
 *
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Fields {
}
