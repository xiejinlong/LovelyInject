package com.inject.xie.myinject

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.inject.xie.annotation.BuilderFragment
import com.inject.xie.annotation.Fields
import com.inject.xie.myinject.builder.KKFragmentBuilder
import kotlinx.android.synthetic.main.fragment_test.*

@BuilderFragment
class TestFragment: Fragment() {

    @Fields
    var name: String? = null
    @Fields
    var age: Int = 0
    @Fields
    var msg: String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        KKFragmentBuilder.inject(this, savedInstanceState)
        Toast.makeText(context,
                "我是 $name，今年 $age, $msg", Toast.LENGTH_LONG).show()
        return view
    }
}