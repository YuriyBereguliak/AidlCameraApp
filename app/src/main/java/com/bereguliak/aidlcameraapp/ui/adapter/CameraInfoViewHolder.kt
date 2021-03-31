package com.bereguliak.aidlcameraapp.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bereguliak.aidlcameraapp.R
import com.bereguliak.camera.CameraData

class CameraInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val cameraNameTextView: TextView =
        itemView.findViewById(R.id.text_view_item_camera_info_name)
    private val apertureTextView: TextView =
        itemView.findViewById(R.id.text_view_item_camera_aperture)
    private val isoTextView: TextView =
        itemView.findViewById(R.id.text_view_item_camera_iso)
    private val resolutionsTextView: TextView =
        itemView.findViewById(R.id.text_view_item_camera_resolutions)

    //region CameraInfoViewHolder
    fun bindData(data: CameraData) {
        cameraNameTextView.text =
            itemView.context.getString(R.string.text_item_camera_info_title, data.name)

        isoTextView.text = itemView.context.getString(
            R.string.text_item_camera_info_iso,
            data.iso.min,
            data.iso.max
        )

        // Only for test
        val resolutionBuilder = StringBuilder()
        data.resolutions.forEachIndexed { index, resolution ->
            resolutionBuilder.append(resolution.width)
            resolutionBuilder.append("x")
            resolutionBuilder.append(resolution.height)
            if (index != data.resolutions.size - 1) {
                resolutionBuilder.append("\n")
            }
        }
        resolutionsTextView.text = resolutionBuilder.toString()
    }
    //endregion
}