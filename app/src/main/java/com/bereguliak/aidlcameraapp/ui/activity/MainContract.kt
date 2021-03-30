package com.bereguliak.aidlcameraapp.ui.activity

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import com.bereguliak.camera.CameraData
import com.bereguliak.camera.ICameraApi

interface MainContract {
    interface View {
        @UiThread
        fun showServiceProcessInfo(processId: Int)

        @WorkerThread
        fun showCameraInfo(cameras: List<CameraData>)
    }

    interface Presenter : CameraApiHandler {
        @UiThread
        fun loadServiceProcessInfo()

        @UiThread
        fun loadCameraInfo()
    }

    interface CameraApiHandler {
        fun initCameraApi(api: ICameraApi)
        fun deInitCameraApi()
    }
}