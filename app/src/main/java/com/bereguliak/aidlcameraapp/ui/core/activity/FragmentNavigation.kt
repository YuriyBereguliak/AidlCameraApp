package com.bereguliak.aidlcameraapp.ui.core.activity

import androidx.fragment.app.Fragment

interface FragmentNavigation {
    fun replaceFragment(
        containerViewId: Int,
        fragment: Fragment,
        addToBackStack: Boolean
    )
}