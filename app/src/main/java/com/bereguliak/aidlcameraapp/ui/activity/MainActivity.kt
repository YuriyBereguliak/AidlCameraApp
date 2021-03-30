package com.bereguliak.aidlcameraapp.ui.activity

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import androidx.annotation.LayoutRes
import androidx.annotation.WorkerThread
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bereguliak.aidlcameraapp.R
import com.bereguliak.aidlcameraapp.ui.core.activity.BaseActivity
import com.bereguliak.aidlcameraapp.ui.fragment.MainFragment
import com.bereguliak.camera.CameraInfoService
import com.bereguliak.camera.ICameraApi

class MainActivity : BaseActivity(), MainContract.View, MainActivityNavigation {

    //region Variables :: Camera API
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            with(presenter) {
                initCameraApi(ICameraApi.Stub.asInterface(service))
                loadServiceProcessInfo()
                loadCameraInfo()
            }
        }

        override fun onServiceDisconnected(className: ComponentName) {
            presenter.deInitCameraApi()
        }
    }
    //endregion

    private val presenter: MainContract.Presenter by lazy { MainPresenter(this) }
    private lateinit var handler: Handler

    //region AppCompatActivity
    @LayoutRes
    override fun getLayoutRes() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        handler = Handler(mainLooper)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                10001
            )
            return
        }
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

    //region MainContract.View
    override fun showServiceProcessInfo(processId: Int) {

    }

    @WorkerThread
    override fun showCameraInfo(cameras: List<com.bereguliak.camera.CameraData>) {
        handler.post {
            navigateToCamerasInfo(cameras)
        }
    }
    //endregion

    //region MainActivityNavigation
    override fun navigateToCamerasInfo(info: List<com.bereguliak.camera.CameraData>) {
        replaceFragment(
            R.id.frame_layout_activity_main_container,
            MainFragment.newInstance(info),
            false
        )
    }
    //endregion
}