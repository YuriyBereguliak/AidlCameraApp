package com.bereguliak.aidlcameraapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Process
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bereguliak.aidlcameraapp.service.CameraInfoService


class MainActivity : AppCompatActivity() {
    private var cameraApi: ICameraApi? = null
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            cameraApi = ICameraApi.Stub.asInterface(service)
            loadServiceProcessInfo()
            loadCameraData()
        }

        override fun onServiceDisconnected(className: ComponentName) {
            cameraApi = null
        }
    }

    //region AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        bindService(
            Intent(this, CameraInfoService::class.java),
            mServiceConnection,
            BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        unbindService(mServiceConnection)
    }
    //endregion

    //region Utility API
    private fun loadServiceProcessInfo() {
        Log.d("UI", "SERVICE :: pid :: ${cameraApi?.pid}")
        Log.d("UI", "SERVICE :: app pid :: ${Process.myPid()}")
    }

    private fun loadCameraData() {
        Log.d("UI", "Sync :: camera method :: ${cameraApi?.loadMainCameraData()}")

        cameraApi?.loadCameraData(object : ICameraInfoServiceResponseListener.Stub() {
            override fun onResponse(cameras: MutableList<CameraData>?) {
                Log.d("UI", "Async :: ${cameras?.toString()}")
            }
        })
    }
    //endregion
}