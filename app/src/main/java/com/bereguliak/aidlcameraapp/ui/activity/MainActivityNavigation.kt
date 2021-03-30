package com.bereguliak.aidlcameraapp.ui.activity

import com.bereguliak.camera.CameraData

interface MainActivityNavigation {
    fun navigateToCamerasInfo(info: List<com.bereguliak.camera.CameraData>)
}