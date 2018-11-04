package com.inject.xie.myinject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        switchAc.setOnClickListener {
            TestActivityBuilder.builder().age(12)
                    .name("xie")
                    .msg("我是从mainAc过来的参数")
                    .start(this)
        }
    }
}
