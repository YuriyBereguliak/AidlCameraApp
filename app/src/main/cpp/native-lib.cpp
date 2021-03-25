#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_bereguliak_aidlcameraapp_service_CameraInfoNativeHelper_stringFromJNI(
        JNIEnv* env,
        jobject ) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}