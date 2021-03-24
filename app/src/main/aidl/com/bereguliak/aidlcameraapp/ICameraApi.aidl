// ICameraApi.aidl
package com.bereguliak.aidlcameraapp;

interface ICameraApi {
    int getPid();

    // Async
    oneway void loadCameraData();
}