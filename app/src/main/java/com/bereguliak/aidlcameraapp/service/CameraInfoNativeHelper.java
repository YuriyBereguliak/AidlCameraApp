package com.bereguliak.aidlcameraapp.service;

public class CameraInfoNativeHelper {
    static {
        System.loadLibrary("native-lib");
    }

    public native String stringFromJNI();
}
