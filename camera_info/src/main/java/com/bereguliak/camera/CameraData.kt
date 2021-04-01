package com.bereguliak.camera

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CameraData(
    val id: Int,
    val name: String,
    val resolutions: List<Resolution>,
    val iso: Iso,
    val aperture: Float
) : Parcelable

@Parcelize
data class Resolution(val width: Int, val height: Int) : Parcelable

@Parcelize
data class Iso(val min: Int, val max: Int) : Parcelable