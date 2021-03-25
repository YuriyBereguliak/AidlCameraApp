// ICameraInfoServiceResponseListener.aidl
package com.bereguliak.aidlcameraapp;

// Declare any non-default types here with import statements
import com.bereguliak.aidlcameraapp.CameraData;

interface ICameraInfoServiceResponseListener {
    void onResponse(in List<CameraData> cameras);
}