package com.bereguliak.camera

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CameraData(
    val id: Int,
    val name: String,
    val resolutions: List<Resolution>
) : Parcelable

@Parcelize
data class Resolution(val width: Int, val height: Int) : Parcelable