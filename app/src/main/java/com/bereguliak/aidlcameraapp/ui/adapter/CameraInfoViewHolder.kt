package com.bereguliak.aidlcameraapp.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bereguliak.camera.CameraData
import com.bereguliak.aidlcameraapp.R

class CameraInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val cameraNameTextView: TextView =
        itemView.findViewById(R.id.text_view_item_camera_info_name)

    //region CameraInfoViewHolder
    fun bindData(data: com.bereguliak.camera.CameraData) {
        cameraNameTextView.text = data.name
    }
    //endregion
}