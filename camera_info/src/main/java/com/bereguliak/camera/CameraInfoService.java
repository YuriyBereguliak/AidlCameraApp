package com.bereguliak.camera;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;

public class CameraInfoService extends Service {
    private final ICameraApi.Stub mBinder = new ICameraApi.Stub() {
        @Override
        public int getPid() {
            Log.d("CAMERA_INFO_SERVICE", "JNI :: " + mCameraInfoHelper.stringFromJNI());
            return Process.myPid();
        }

        @Override
        public void loadCameraData(ICameraInfoServiceResponseListener listener) {
            try {
                listener.onResponse(new ArrayList<CameraData>() {{
                    add(new CameraData(1,
                            "MainCamera",
                            new ArrayList<Resolution>() {{
                                add(new Resolution(1920, 1080));
                                add(new Resolution(1280, 720));
                                add(new Resolution(960, 576));
                            }}
                    ));
                    add(new CameraData(2,
                            "PortraitCamera",
                            new ArrayList<Resolution>() {{
                                add(new Resolution(1920, 1080));
                                add(new Resolution(1280, 720));
                                add(new Resolution(960, 576));
                            }}
                    ));
                    add(new CameraData(3,
                            "FrontCamera",
                            new ArrayList<Resolution>() {{
                                add(new Resolution(1280, 720));
                            }}
                    ));
                }});
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public CameraData loadMainCameraData() {
            return new CameraData(1, "MainCamera",
                    new ArrayList<Resolution>() {{
                        add(new Resolution(1920, 1080));
                        add(new Resolution(1280, 720));
                        add(new Resolution(960, 576));
                    }});
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