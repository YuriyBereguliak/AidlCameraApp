package com.bereguliak.camera;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

                List<CameraData> result = new ArrayList<>();

                String[] cameras = mCameraInfoHelper.loadCameraIds();

                for (String id : cameras) {

                    Resolution[] resolutions = mCameraInfoHelper.loadCameraResolutions(id);
                    Iso iso = mCameraInfoHelper.loadCameraIso(id);
                    float aperture = mCameraInfoHelper.loadCameraAperture(id);

                    result.add(new CameraData(
                            Integer.parseInt(id),
                            id,
                            Arrays.asList(resolutions),
                            iso,
                            aperture));
                }

                listener.onResponse(result);
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
                    }},
                    new Iso(100, 1000),
                    1.5f);
        }
    };

    private CameraInfoNativeHelper mCameraInfoHelper;

    //region Service
    @Override
    public void onCreate() {
        super.onCreate();
        mCameraInfoHelper = new CameraInfoNativeHelper();
        mCameraInfoHelper.initCameraManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCameraInfoHelper.deInitCameraManager();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    //endregion
}