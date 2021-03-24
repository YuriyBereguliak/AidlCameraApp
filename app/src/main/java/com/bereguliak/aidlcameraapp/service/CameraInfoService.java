package com.bereguliak.aidlcameraapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import com.bereguliak.aidlcameraapp.ICameraApi;

public class CameraInfoService extends Service {
    private final ICameraApi.Stub mBinder = new ICameraApi.Stub() {
        @Override
        public int getPid() {
            return Process.myPid();
        }

        @Override
        public void loadCameraData() throws RemoteException {
            Log.d("CMP", "Async method :: load camera data");
        }
    };

    //region Service
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    //endregion
}