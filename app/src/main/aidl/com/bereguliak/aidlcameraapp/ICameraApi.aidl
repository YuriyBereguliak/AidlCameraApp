// ICameraApi.aidl
package com.bereguliak.aidlcameraapp;

// Declare any non-default types here with import statements
import com.bereguliak.aidlcameraapp.ICameraInfoServiceResponseListener;
import com.bereguliak.aidlcameraapp.CameraData;

interface ICameraApi {
    int getPid();

    // Sync
    CameraData loadMainCameraData();

    // Async
    oneway void loadCameraData(ICameraInfoServiceResponseListener listener);
}