package com.inject.xie.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 用来修饰Class，会生成对应的Builder类
 * 目前支持Activity，Fragment，Model
 * 其中，Fragment需要继承于@BaseMvpFragment
 *
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface BuilderModel {
}
