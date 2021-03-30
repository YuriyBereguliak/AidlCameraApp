package com.bereguliak.camera;

public class CameraInfoNativeHelper {
    static {
        System.loadLibrary("native-lib");
    }

    public native String stringFromJNI();

    public native String getCamerasInfo();
}
