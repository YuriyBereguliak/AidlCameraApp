package com.bereguliak.aidlcameraapp.ui.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.bereguliak.aidlcameraapp.R

abstract class BaseFragment : Fragment() {

    //region BaseActivity
    @UiThread
    @LayoutRes
    abstract fun getLayoutRes(): Int

    @UiThread
    abstract fun bindView(view:View)
    //endregion

    //region Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutRes(), container, false)
        bindView(view)
        return view
    }
    //endregion
}