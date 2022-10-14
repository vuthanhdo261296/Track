LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := libtennis-prebuilt
LOCAL_SRC_FILES := $(LOCAL_PATH)/jniLibs/$(TARGET_ARCH_ABI)/libtennis.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libSeetaAuthorize-prebuilt
LOCAL_SRC_FILES := $(LOCAL_PATH)/jniLibs/$(TARGET_ARCH_ABI)/libSeetaAuthorize.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := SeetaFaceDetector600-prebuilt
LOCAL_SRC_FILES := $(LOCAL_PATH)/jniLibs/$(TARGET_ARCH_ABI)/libSeetaFaceDetector600.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := openrolezoo-prebuilt
LOCAL_SRC_FILES := $(LOCAL_PATH)/jniLibs/$(TARGET_ARCH_ABI)/libORZ_static.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := SeetaFaceTracking600-prebuilt
LOCAL_SRC_FILES := $(LOCAL_PATH)/jniLibs/$(TARGET_ARCH_ABI)/libSeetaFaceTracking600.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := SeetaFaceLandmarker600-prebuilt
LOCAL_SRC_FILES := $(LOCAL_PATH)/jniLibs/$(TARGET_ARCH_ABI)/libSeetaFaceLandmarker600.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := SeetaFaceRecognizer610-prebuilt
LOCAL_SRC_FILES := $(LOCAL_PATH)/jniLibs/$(TARGET_ARCH_ABI)/libSeetaFaceRecognizer610.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := SeetaPoseEstimation600-prebuilt
LOCAL_SRC_FILES := $(LOCAL_PATH)/jniLibs/$(TARGET_ARCH_ABI)/libSeetaPoseEstimation600.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := SeetaFaceDetector600_java

# MY_CPP_LIST += $(wildcard $(LOCAL_PATH)/*.cpp)
# LOCAL_SRC_FILES := $(MY_CPP_LIST:$(LOCAL_PATH)/%=%)
LOCAL_SRC_FILES := JNIFaceDetector.cpp

LOCAL_C_INCLUDES += $(LOCAL_PATH)/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/seeta/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/seetanet/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/opencv/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/opencv2/

LOCAL_LDFLAGS += -L$(LOCAL_PATH)/lib -fuse-ld=bfd

LOCAL_LDLIBS += -llog -lz

LOCAL_CFLAGS += -mfpu=neon-vfpv4 -funsafe-math-optimizations -ftree-vectorize  -ffast-math

LOCAL_SHARED_LIBRARIES += libtennis-prebuilt
LOCAL_SHARED_LIBRARIES += libSeetaAuthorize-prebuilt
LOCAL_SHARED_LIBRARIES += SeetaFaceDetector600-prebuilt

include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := FaceTracker600_java

# MY_CPP_LIST += $(wildcard $(LOCAL_PATH)/*.cpp)
# LOCAL_SRC_FILES := $(MY_CPP_LIST:$(LOCAL_PATH)/%=%)
LOCAL_SRC_FILES := JNIFaceTracker.cpp

LOCAL_C_INCLUDES += $(LOCAL_PATH)/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/seeta/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/seetanet/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/opencv/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/opencv2/

LOCAL_LDFLAGS += -L$(LOCAL_PATH)/lib -fuse-ld=bfd

LOCAL_LDLIBS += -llog -lz

LOCAL_CFLAGS += -mfpu=neon-vfpv4 -funsafe-math-optimizations -ftree-vectorize  -ffast-math

LOCAL_SHARED_LIBRARIES += openrolezoo-prebuilt
LOCAL_SHARED_LIBRARIES += SeetaFaceDetector600-prebuilt
LOCAL_SHARED_LIBRARIES += SeetaFaceTracking600-prebuilt

include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := FaceLandmarker600_java

# MY_CPP_LIST += $(wildcard $(LOCAL_PATH)/*.cpp)
# LOCAL_SRC_FILES := $(MY_CPP_LIST:$(LOCAL_PATH)/%=%)
LOCAL_SRC_FILES := JNIFaceLandmarker.cpp

LOCAL_C_INCLUDES += $(LOCAL_PATH)/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/seeta/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/seetanet/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/opencv/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/opencv2/

LOCAL_LDFLAGS += -L$(LOCAL_PATH)/lib -fuse-ld=bfd

LOCAL_LDLIBS += -llog -lz

LOCAL_CFLAGS += -mfpu=neon-vfpv4 -funsafe-math-optimizations -ftree-vectorize  -ffast-math

LOCAL_SHARED_LIBRARIES += libtennis-prebuilt
LOCAL_SHARED_LIBRARIES += libSeetaAuthorize-prebuilt
LOCAL_SHARED_LIBRARIES += openrolezoo-prebuilt
LOCAL_SHARED_LIBRARIES += SeetaFaceLandmarker600-prebuilt

include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := FaceRecognizer600_java

MY_CPP_LIST := $(wildcard $(LOCAL_PATH)/JNIFaceRecognizer.cpp)
MY_CPP_LIST += $(wildcard $(LOCAL_PATH)/JNIFaceDatabase.cpp)
LOCAL_SRC_FILES := $(MY_CPP_LIST:$(LOCAL_PATH)/%=%)
# LOCAL_SRC_FILES := JNIFaceRecognizer.cpp

LOCAL_C_INCLUDES += $(LOCAL_PATH)/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/seeta/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/seetanet/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/opencv/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/opencv2/

LOCAL_LDFLAGS += -L$(LOCAL_PATH)/lib -fuse-ld=bfd

LOCAL_LDLIBS += -llog -lz

LOCAL_CFLAGS += -mfpu=neon-vfpv4 -funsafe-math-optimizations -ftree-vectorize  -ffast-math

LOCAL_SHARED_LIBRARIES += libtennis-prebuilt
LOCAL_SHARED_LIBRARIES += libSeetaAuthorize-prebuilt
LOCAL_SHARED_LIBRARIES += openrolezoo-prebuilt
LOCAL_SHARED_LIBRARIES += SeetaFaceRecognizer610-prebuilt

include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := PoseEstimation600_java

# MY_CPP_LIST += $(wildcard $(LOCAL_PATH)/*.cpp)
# LOCAL_SRC_FILES := $(MY_CPP_LIST:$(LOCAL_PATH)/%=%)
LOCAL_SRC_FILES := JNIPoseEstimator.cpp

LOCAL_C_INCLUDES += $(LOCAL_PATH)/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/seeta/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/seetanet/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/opencv/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/opencv2/

LOCAL_LDFLAGS += -L$(LOCAL_PATH)/lib -fuse-ld=bfd

LOCAL_LDLIBS += -llog -lz

LOCAL_CFLAGS += -mfpu=neon-vfpv4 -funsafe-math-optimizations -ftree-vectorize  -ffast-math

LOCAL_SHARED_LIBRARIES += libtennis-prebuilt
LOCAL_SHARED_LIBRARIES += libSeetaAuthorize-prebuilt
LOCAL_SHARED_LIBRARIES += openrolezoo-prebuilt
LOCAL_SHARED_LIBRARIES += SeetaPoseEstimation600-prebuilt

include $(BUILD_SHARED_LIBRARY)

