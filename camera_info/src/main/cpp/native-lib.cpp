#include <jni.h>
#include <android/log.h>
#include <string>
#include <vector>

#include <camera/NdkCameraCaptureSession.h>
#include <camera/NdkCameraDevice.h>
#include <camera/NdkCameraError.h>
#include <camera/NdkCameraManager.h>
#include <camera/NdkCameraMetadata.h>
#include <camera/NdkCameraMetadataTags.h>
#include <camera/NdkCameraWindowType.h>
#include <camera/NdkCaptureRequest.h>

#include <media/NdkImageReader.h>

#define TAG "camera_info"
#define LOGD(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

static void initCam() {
    ACameraManager *cameraManager = ACameraManager_create();
    ACameraIdList *cameraIds = nullptr;
    ACameraManager_getCameraIdList(cameraManager, &cameraIds);

    LOGD("Init CAM");
    LOGD("Number of cameras :: %d", cameraIds->numCameras);

    if (cameraIds->numCameras < 1) {
        LOGD("No camera device detected");
        return;
    }

    for (int i = 0; i < cameraIds->numCameras; ++i) {
        const char *id = cameraIds->cameraIds[i];
        LOGD("camera id %s", id);
//        printCamProps(cameraManager, id);
    }

    ACameraManager_deleteCameraIdList(cameraIds);
    ACameraManager_delete(cameraManager);
}

//
// JNI
//
extern "C" {
JNIEXPORT jstring
JNICALL
Java_com_bereguliak_camera_CameraInfoNativeHelper_stringFromJNI(
        JNIEnv *env,
        jobject) {
    initCam();
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jstring
JNICALL
Java_com_bereguliak_camera_CameraInfoNativeHelper_cameraInfo(
        JNIEnv *env,
        jobject) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
}