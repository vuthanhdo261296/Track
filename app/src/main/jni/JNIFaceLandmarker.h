/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_seeta_sdk_FaceLandmarker */

#ifndef _Included_com_seeta_sdk_FaceLandmarker
#define _Included_com_seeta_sdk_FaceLandmarker
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_seeta_sdk_FaceLandmarker
 * Method:    construct
 * Signature: (Lcom/seeta/sdk/SeetaModelSetting;)V
 */
JNIEXPORT void JNICALL Java_com_example_track_seeta6_FaceLandmarker_construct__Lcom_seeta_sdk_SeetaModelSetting_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     com_seeta_sdk_FaceLandmarker
 * Method:    construct
 * Signature: (Ljava/lang/String;Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_com_example_track_seeta6_FaceLandmarker_construct1
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_seeta_sdk_FaceLandmarker
 * Method:    dispose
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_track_seeta6_FaceLandmarker_dispose
  (JNIEnv *, jobject);

/*
 * Class:     com_seeta_sdk_FaceLandmarker
 * Method:    number
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_example_track_seeta6_FaceLandmarker_number
  (JNIEnv *, jobject);

/*
 * Class:     com_seeta_sdk_FaceLandmarker
 * Method:    mark
 * Signature: (Lcom/seeta/sdk/SeetaImageData;Lcom/seeta/sdk/SeetaRect;[Lcom/seeta/sdk/SeetaPointF;)V
 */
JNIEXPORT void JNICALL Java_com_example_track_seeta6_FaceLandmarker_mark
  (JNIEnv *, jobject, jobject, jobject, jobjectArray);

/*
 * Class:     com_seeta_sdk_FaceLandmarker
 * Method:    mark
 * Signature: (Lcom/seeta/sdk/SeetaImageData;Lcom/seeta/sdk/SeetaRect;[Lcom/seeta/sdk/SeetaPointF;[I)V
 */
JNIEXPORT void JNICALL Java_com_example_track_seeta6_FaceLandmarker_mark1
  (JNIEnv *, jobject, jobject, jobject, jobjectArray, jintArray);

#ifdef __cplusplus
}
#endif
#endif
