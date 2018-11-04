package com.inject.xie.myinject

import com.inject.xie.annotation.BuilderModel
import com.inject.xie.annotation.Fields

@BuilderModel
class TestModel {
    @Fields
    var name: String? = null
    @Fields
    var age: Int = 0
    @Fields
    var msg: String? = null
}