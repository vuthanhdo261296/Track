package com.example.track.seeta6

import android.content.Context
import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FaceDatabase {
    private val TAG = FaceDatabase::class.java.simpleName
    private var context: Context
    companion object {
        external fun SetLogLevel(level: Int): Int
        external fun GetCropFaceWidthV2(): Int
        external fun GetCropFaceHeightV2(): Int
        external fun GetCropFaceChannelsV2(): Int
        external fun CropFaceV2(
            image: SeetaImageData?,
            points: Array<SeetaPointF?>?,
            face: SeetaImageData?
        ): Boolean

        init {
            System.loadLibrary("FaceRecognizer600_java")
        }
    }

    var impl: Long = 0
    private external fun construct(seetaModel: String)
    private external fun construct1(
        setting: SeetaModelSetting,
        extractionCoreNumber: Int,
        comparationCoreNumber: Int
    )

    external fun dispose()
    @Throws(Throwable::class)
    protected fun finalize() {
        //super.finalize()
        dispose()
    }

    external fun Compare(
        image1: SeetaImageData?, points1: Array<SeetaPointF?>?,
        image2: SeetaImageData?, points2: Array<SeetaPointF?>?
    ): Float

    external fun CompareByCroppedFace(
        croppedFaceImage1: SeetaImageData?,
        croppedFaceImage2: SeetaImageData?
    ): Float

    external fun Register(image: SeetaImageData?, points: Array<SeetaPointF?>?): Long
    external fun RegisterByCroppedFace(croppedFaceImage: SeetaImageData?): Long
    external fun Delete(index: Long): Int
    external fun Clear()
    external fun Count(): Long
    external fun Query(image: SeetaImageData?, points: Array<SeetaPointF?>?): Long
    external fun Query(
        image: SeetaImageData?,
        points: Array<SeetaPointF?>?,
        similarity: FloatArray?
    ): Long

    external fun QueryByCroppedFace(croppedFaceImage: SeetaImageData?): Long
    external fun QueryByCroppedFace(
        croppedFaceImage: SeetaImageData?,
        similarity: FloatArray?
    ): Long

    external fun QueryTop(
        image: SeetaImageData?,
        points: Array<SeetaPointF?>?,
        N: Long,
        index: LongArray?,
        similarity: FloatArray?
    ): Long

    external fun QueryTopByCroppedFace(
        croppedFaceImage: SeetaImageData?,
        N: Long,
        index: LongArray?,
        similarity: FloatArray?
    ): Long

    external fun QueryAbove(
        image: SeetaImageData?,
        points: Array<SeetaPointF?>?,
        threshold: Float,
        N: Long,
        index: LongArray?,
        similarity: FloatArray?
    ): Long

    external fun QueryAboveByCroppedFace(
        croppedFaceImage: SeetaImageData?,
        threshold: Float,
        N: Long,
        index: LongArray?,
        similarity: FloatArray?
    ): Long

    external fun RegisterParallel(
        image: SeetaImageData?,
        points: Array<SeetaPointF?>?,
        index: LongArray?
    )

    external fun RegisterByCroppedFaceParallel(croppedFaceImage: SeetaImageData?, index: LongArray?)
    external fun Join()
    external fun Save(path: String?): Boolean
    external fun Load(path: String?): Boolean

    fun loadEngine(ModelFile: String?) {
        if (null == ModelFile || "" == ModelFile) {
            Log.w(
                TAG,
                "detectModelFile file path is invalid!"
            )
            return
        }
        this.construct(ModelFile)
    }

    fun loadEngine() {
        if (null == context) {
            Log.w(
                TAG,
                "please call initial first!"
            )
        }
        Log.w("dovt1: ", "loadEngine: " + getPath("face_recognizer.csta", context))
        loadEngine(getPath("face_recognizer.csta", context))
    }

    fun getPath(file: String?, context: Context): String? {
        val assetManager = context.assets
        var inputStream: BufferedInputStream? = null
        try {
            inputStream = BufferedInputStream(assetManager.open(file!!))
            val data = ByteArray(inputStream.available())
            inputStream.read(data)
            inputStream.close()
            val outFile = File(context.filesDir, file)
            val os = FileOutputStream(outFile)
            os.write(data)
            os.close()
            return outFile.absolutePath
        } catch (ex: IOException) {
            Log.i("FileUtil", "Failed to upload a file")
        }
        return ""
    }

    constructor(context: Context) {
        this.context = context
        this.loadEngine()
    }
//    constructor(
//        seetaModel: SeetaModelSetting,
//        extractionCoreNumber: Int,
//        comparationCoreNumber: Int
//    ) {
//        this.construct(seetaModel, extractionCoreNumber, comparationCoreNumber)
//    }
}