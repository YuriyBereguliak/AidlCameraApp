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

struct camera_resolution {
    int width;
    int height;
};

ACameraManager *cameraManager = nullptr;

void initCameraManager() {
    cameraManager = ACameraManager_create();
}

void deInitCameraManager() {
    ACameraManager_delete(cameraManager);
}

std::vector<std::string> getCamerasList() {
    ACameraIdList *cameraIds = nullptr;
    ACameraManager_getCameraIdList(cameraManager, &cameraIds);

    std::vector<std::string> result;
    for (int i = 0; i < cameraIds->numCameras; ++i) {
        const char *id = cameraIds->cameraIds[i];
        LOGD("id %s", id);
        result.emplace_back(id);
    }
    ACameraManager_deleteCameraIdList(cameraIds);

    return result;
}

std::vector<camera_resolution> getCameraResolution(const char *cameraId) {
    LOGD("camera id %s", cameraId);

    ///
    std::vector<camera_resolution> result;

    ///
    ACameraMetadata *metadataObj;
    ACameraManager_getCameraCharacteristics(cameraManager, cameraId, &metadataObj);

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

            struct camera_resolution resolution = {width, height};
            result.push_back(resolution);

            LOGD("w-h: width=%d - height=%d", resolution.width, resolution.height);
        }
    }

    return result;
}

//https://developer.android.com/ndk/reference/group/camera#group___camera_1gga49cf3e5a3deefe079ad036a8fac14627ab4ef4fabbbaaecf6f2fc74eaa9197b26
void initCam() {
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

    }

    ACameraManager_deleteCameraIdList(cameraIds);
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
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT void
JNICALL
Java_com_bereguliak_camera_CameraInfoNativeHelper_initCameraManager(
        JNIEnv *env,
        jobject) {
    initCameraManager();
}

JNIEXPORT void
JNICALL
Java_com_bereguliak_camera_CameraInfoNativeHelper_deInitCameraManager(
        JNIEnv *env,
        jobject) {
    deInitCameraManager();
}

JNIEXPORT jobjectArray
JNICALL
Java_com_bereguliak_camera_CameraInfoNativeHelper_loadCameraIds(
        JNIEnv *env,
        jobject) {
    std::vector<std::string> cameras = getCamerasList();

    jobjectArray result;
    result = (jobjectArray) env->NewObjectArray(cameras.size(),
                                                env->FindClass("java/lang/String"),
                                                env->NewStringUTF(""));
    for (int i = 0; i < cameras.size(); i++) {
        env->SetObjectArrayElement(result, i, env->NewStringUTF(cameras[i].c_str()));
    }
    return result;
}

JNIEXPORT jobjectArray
JNICALL
Java_com_bereguliak_camera_CameraInfoNativeHelper_loadCameraResolutions(
        JNIEnv *env,
        jobject,
        jstring cameraId) {
    // get resolutions
    const char *convertedString = env->GetStringUTFChars(cameraId, nullptr);
    std::vector<camera_resolution> resolutions = getCameraResolution(convertedString);

    // Class
    jclass employeeClass = env->FindClass("com/bereguliak/camera/Resolution");
    // get a reference to the constructor
    jmethodID constructor = env->GetMethodID(employeeClass, "<init>", "(II)V");

    jobjectArray result;
    result = (jobjectArray) env->NewObjectArray(resolutions.size(), employeeClass, nullptr);

    for (int i = 0; i < resolutions.size(); i++) {
        // Args
        jvalue args[2];
        // define arguments
        args[0].i = resolutions[i].width;
        args[1].i = resolutions[i].height;
        // create
        jobject resolutionObject = env->NewObjectA(employeeClass, constructor, args);
        env->SetObjectArrayElement(result, i, resolutionObject);
    }

    return result;
}
}
