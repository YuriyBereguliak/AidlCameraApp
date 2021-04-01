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
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

struct camera_resolution {
    int width;
    int height;
};

struct camera_iso {
    int min;
    int max;
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
        }
    }

    return result;
}

camera_iso getCameraIso(const char *cameraId) {

    ACameraMetadata *metadataObj;
    ACameraManager_getCameraCharacteristics(cameraManager, cameraId, &metadataObj);

    ACameraMetadata_const_entry entry = {0};

    camera_status_t status = ACameraMetadata_getConstEntry(metadataObj,
                                                           ACAMERA_SENSOR_INFO_SENSITIVITY_RANGE,
                                                           &entry);
    if (status != ACAMERA_OK) {
        return {0, 0};
    }

    struct camera_iso iso = {entry.data.i32[0], entry.data.i32[1]};

    return iso;
}

float getCameraAperture(const char *cameraId){
    ACameraMetadata *metadataObj;
    ACameraManager_getCameraCharacteristics(cameraManager, cameraId, &metadataObj);

    ACameraMetadata_const_entry entry = {0};

    ACameraMetadata_getConstEntry(metadataObj,
                                  ACAMERA_LENS_INFO_AVAILABLE_APERTURES,
                                  &entry);
    return entry.data.f[0];
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
    jclass resolutionClass = env->FindClass("com/bereguliak/camera/Resolution");
    // get a reference to the constructor
    jmethodID constructor = env->GetMethodID(resolutionClass, "<init>", "(II)V");

    jobjectArray result;
    result = (jobjectArray) env->NewObjectArray(resolutions.size(), resolutionClass, nullptr);

    for (int i = 0; i < resolutions.size(); i++) {
        // Args
        jvalue args[2];
        // define arguments
        args[0].i = resolutions[i].width;
        args[1].i = resolutions[i].height;
        // create
        jobject resolutionObject = env->NewObjectA(resolutionClass, constructor, args);
        env->SetObjectArrayElement(result, i, resolutionObject);
    }

    return result;
}

JNIEXPORT jobject
JNICALL
Java_com_bereguliak_camera_CameraInfoNativeHelper_loadCameraIso(
        JNIEnv *env,
        jobject,
        jstring cameraId) {
    const char *convertedString = env->GetStringUTFChars(cameraId, nullptr);
    camera_iso iso = getCameraIso(convertedString);

    jclass isoClass = env->FindClass("com/bereguliak/camera/Iso");
    jmethodID constructor = env->GetMethodID(isoClass, "<init>", "(II)V");
    jobject isoObject = env->NewObject(isoClass, constructor, iso.min, iso.max);
    return isoObject;
}

JNIEXPORT jfloat
JNICALL
Java_com_bereguliak_camera_CameraInfoNativeHelper_loadCameraAperture(
        JNIEnv *env,
        jobject,
        jstring cameraId) {
    const char *convertedString = env->GetStringUTFChars(cameraId, nullptr);
    return getCameraAperture(convertedString);
}
}
