package com.bereguliak.aidlcameraapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import com.bereguliak.aidlcameraapp.CameraData;
import com.bereguliak.aidlcameraapp.ICameraApi;
import com.bereguliak.aidlcameraapp.ICameraInfoServiceResponseListener;

import java.util.ArrayList;
import java.util.List;

public class CameraInfoService extends Service {
    private final ICameraApi.Stub mBinder = new ICameraApi.Stub() {
        @Override
        public int getPid() {
            return Process.myPid();
        }

        @Override
        public void loadCameraData(ICameraInfoServiceResponseListener listener) {
            Log.d("REMOTE", "SERVICE :: load camera data");
            Log.d("REMOTE", "SERVICE :: thread :: " + Thread.currentThread().getName());

            try {

                Log.d("REMOTE", "JNI :: " + mCameraInfoHelper.stringFromJNI());

                List<CameraData> data = new ArrayList<CameraData>() {{
                    add(new CameraData(1, "MainCamera"));
                    add(new CameraData(2, "PortraitCamera"));
                    add(new CameraData(3, "FrontCamera"));
                }};

                Log.d("REMOTE", "SERVICE :: " + data);

                listener.onResponse(data);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public CameraData loadMainCameraData() {
            return new CameraData(1, "MainCamera");
        }
    };

    private CameraInfoNativeHelper mCameraInfoHelper;

    //region Service
    @Override
    public void onCreate() {
        super.onCreate();
        mCameraInfoHelper = new CameraInfoNativeHelper();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    //endregion
}