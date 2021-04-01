package com.bereguliak.aidlcameraapp.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bereguliak.aidlcameraapp.R
import com.bereguliak.aidlcameraapp.ui.adapter.CameraInfoAdapter
import com.bereguliak.aidlcameraapp.ui.core.fragment.BaseFragment
import com.bereguliak.camera.CameraData
import kotlinx.android.parcel.Parcelize

class MainFragment : BaseFragment() {

    //region Variables :: UI
    private lateinit var recyclerView: RecyclerView
    //endregion

    private lateinit var adapter: CameraInfoAdapter

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CameraInfoAdapter()
        arguments?.let {
            it.getParcelable<ListParcelableWrapper>(KEY_CAMERA_INFO_LIST)
                ?.let { info ->
                    adapter.addAll(
                        info.list
                    )
                }
        }
    }
    //endregion

    //region BaseFragment
    @LayoutRes
    override fun getLayoutRes() = R.layout.fragment_main

    override fun bindView(view: View) {
        initRecyclerView(view)
    }
    //endregion

    //region Utility API
    private fun initRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view_activity_main_camera_info)
        recyclerView.adapter = adapter
    }
    //endregion

    //region Utility structure
    companion object {
        private const val KEY_CAMERA_INFO_LIST =
            "com.bereguliak.aidlcameraapp.ui.fragment.KEY_CAMERA_INFO_LIST"

        fun newInstance(info: List<CameraData>) = MainFragment().apply {
            arguments = Bundle(1).apply {
                putParcelable(KEY_CAMERA_INFO_LIST, ListParcelableWrapper(info))
            }
        }
    }
    //endregion
}

//region Utility structure
@Parcelize
class ListParcelableWrapper(val list: List<CameraData>) : Parcelable
//endregion