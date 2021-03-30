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

//https://developer.android.com/ndk/reference/group/camera#group___camera_1gga49cf3e5a3deefe079ad036a8fac14627ab4ef4fabbbaaecf6f2fc74eaa9197b26
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

        ACameraMetadata *metadataObj;
        ACameraManager_getCameraCharacteristics(cameraManager, id, &metadataObj);

        ACameraMetadata_const_entry entry = {0};

        /// ACAMERA_SCALER_AVAILABLE_STREAM_CONFIGURATIONS
        /// The available stream configurations that this camera device supports
        /// The configurations are listed as (format, width, height, input?) tuples.
        ACameraMetadata_getConstEntry(metadataObj,
                                      ACAMERA_SCALER_AVAILABLE_STREAM_CONFIGURATIONS,
                                      &entry);
        for (int y = 0; y < entry.count; y += 4) {
            int32_t input = entry.data.i32[y + 3];
            if (input) continue;

            int32_t format = entry.data.i32[y + 0];
            /// enum
            /// This format is always supported as an output format for the android Camera2 NDK API.
            if (format == AIMAGE_FORMAT_JPEG) {
                int32_t width = entry.data.i32[y + 1];
                int32_t height = entry.data.i32[y + 2];

                LOGD("w-h: width=%d - height=%d", width, height);
            }
        }

        /// Camera aperture
        /// mm
        ACameraMetadata_getConstEntry(metadataObj,
                                      ACAMERA_LENS_INFO_AVAILABLE_APERTURES,
                                      &entry);
        for (int y = 0; y < entry.count; y += 1)
            LOGD("APERTURES: %f", entry.data.f[y]);

        /// ISO
        ACameraMetadata_getConstEntry(metadataObj,
                                      ACAMERA_SENSOR_INFO_SENSITIVITY_RANGE,
                                      &entry);
        for (int y = 0; y < entry.count; y += 2)
            LOGD("iso: min %d  max %d", entry.data.i32[y + 0], entry.data.i32[y + 1]);


        metadataObj = nullptr;
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