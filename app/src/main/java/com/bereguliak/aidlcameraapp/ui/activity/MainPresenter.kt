package com.bereguliak.aidlcameraapp.ui.activity

import android.util.Log
import com.bereguliak.camera.ICameraApi
import com.bereguliak.camera.ICameraInfoServiceResponseListener

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {
    private var cameraApi: ICameraApi? = null

    //region MainContract.Presenter
    override fun initCameraApi(api: ICameraApi) {
        cameraApi = api
    }

    override fun deInitCameraApi() {
        cameraApi = null
    }

    override fun loadServiceProcessInfo() {
        cameraApi?.pid?.let {
            view.showServiceProcessInfo(it)
        }
    }

    override fun loadCameraInfo() {
        // Sync
        Log.d("UI", "Sync :: camera method :: ${cameraApi?.loadMainCameraData()}")

        // Async
        cameraApi?.loadCameraData(object : ICameraInfoServiceResponseListener.Stub() {
            override fun onResponse(cameras: MutableList<com.bereguliak.camera.CameraData>?) {
                view.showCameraInfo(cameras ?: emptyList())
            }
        })
    }
    //endregion
}