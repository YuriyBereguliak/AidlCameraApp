// ICameraInfoServiceResponseListener.aidl
package com.bereguliak.camera;

// Declare any non-default types here with import statements
import com.bereguliak.camera.CameraData;

interface ICameraInfoServiceResponseListener {
    void onResponse(in List<CameraData> cameras);
}