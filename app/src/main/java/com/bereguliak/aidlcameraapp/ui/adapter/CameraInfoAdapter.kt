package com.bereguliak.aidlcameraapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bereguliak.camera.CameraData
import com.bereguliak.aidlcameraapp.R
import com.bereguliak.aidlcameraapp.ui.core.adapter.BaseRecyclerViewAdapter

class CameraInfoAdapter : BaseRecyclerViewAdapter<com.bereguliak.camera.CameraData>() {
    //region RecyclerView.Adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CameraInfoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_camera_info, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? CameraInfoViewHolder)?.bindData(getItemByIndex(position))
    }
    //endregion
}