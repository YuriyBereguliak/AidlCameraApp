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
            Log.d("UI", "SERVICE CONNECTED")
            Log.d("UI", "SERVICE :: pid :: ${cameraApi?.pid}")
            Log.d("UI", "SERVICE :: app pid :: ${Process.myPid()}")
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

    override fun onResume() {
        super.onResume()
        super.bindService(
            Intent(this, CameraInfoService::class.java),
            mServiceConnection,
            BIND_AUTO_CREATE
        )
    }
    //endregion
}