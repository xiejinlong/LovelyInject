package com.inject.xie.myinject

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.inject.xie.annotation.BuilderActivity
import com.inject.xie.annotation.Fields
import kotlinx.android.synthetic.main.activity_test.*

@BuilderActivity(routerValue = "to_test_scheme")
class TestActivity: Activity() {

    @Fields
    var name: String? = null
    @Fields
    var age: Int = 0
    @Fields
    var msg: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        Toast.makeText(this,
                "我是 $name，今年 $age, $msg", Toast.LENGTH_LONG).show()
        switchFragment.setOnClickListener {
            fragmentManager.beginTransaction()
                    .replace(R.id.mainLayout,
                            TestFragmentBuilder.builder()
                                    .name("lovely")
                                    .age(13)
                                    .msg("我是从testAc过来的参数").build())
                    .commitAllowingStateLoss()
        }
    }
}