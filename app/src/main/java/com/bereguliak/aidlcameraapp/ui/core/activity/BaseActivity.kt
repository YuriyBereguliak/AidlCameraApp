package com.bereguliak.aidlcameraapp.ui.core.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseActivity : AppCompatActivity(), FragmentNavigation {

    //region BaseActivity
    @UiThread
    @LayoutRes
    abstract fun getLayoutRes(): Int

    @UiThread
    abstract fun initView(savedInstanceState: Bundle?)
    //endregion

    //region AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        initView(savedInstanceState)
    }
    //endregion

    //region FragmentNavigation
    override fun replaceFragment(
        containerViewId: Int,
        fragment: Fragment,
        addToBackStack: Boolean
    ) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(containerViewId, fragment, fragment.javaClass.name)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.javaClass.name)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }
    //endregion
}