package com.bereguliak.camera;

public class CameraInfoNativeHelper {
    static {
        System.loadLibrary("native-lib");
    }

    public native String stringFromJNI();

    public native void initCameraManager();

    public native void deInitCameraManager();

    public native String[] loadCameraIds();
}
