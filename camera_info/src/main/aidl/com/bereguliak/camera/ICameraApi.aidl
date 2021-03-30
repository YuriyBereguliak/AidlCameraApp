// ICameraApi.aidl
package com.bereguliak.camera;

// Declare any non-default types here with import statements
import com.bereguliak.camera.ICameraInfoServiceResponseListener;
import com.bereguliak.camera.CameraData;

interface ICameraApi {
    int getPid();

    // Sync
    CameraData loadMainCameraData();

    // Async
    oneway void loadCameraData(ICameraInfoServiceResponseListener listener);
}