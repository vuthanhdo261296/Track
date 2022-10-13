package com.example.track.seeta6

import android.content.Context
import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FaceRecognizer {
    private val TAG = FaceRecognizer::class.java.simpleName
    private var context: Context
    companion object {
        init {
            System.loadLibrary("FaceRecognizer600_java")
        }
    }

    enum class Property(val value: Int) {
        PROPERTY_NUMBER_THREADS(4), PROPERTY_ARM_CPU_MODE(5);

    }

    var impl: Long = 0
    private external fun construct(setting: SeetaModelSetting)
    private external fun construct1(model: String)

    external fun dispose()
    @Throws(Throwable::class)
    protected fun finalize() {
        //super.finalize()
        dispose()
    }

    //public static native int SetLogLevel(int level);
    //public static native void SetSingleCalculationThreads(int num);
    external fun GetCropFaceWidthV2(): Int
    external fun GetCropFaceHeightV2(): Int
    external fun GetCropFaceChannelsV2(): Int
    external fun GetExtractFeatureSize(): Int
    external fun CropFaceV2(
        image: SeetaImageData?,
        points: Array<SeetaPointF?>?,
        face: SeetaImageData?
    ): Boolean

    external fun ExtractCroppedFace(face: SeetaImageData?, features: FloatArray?): Boolean
    external fun Extract(
        image: SeetaImageData?,
        points: Array<SeetaPointF>,
        features: FloatArray?
    ): Boolean

    external fun CalculateSimilarity(features1: FloatArray?, features2: FloatArray?): Float
    external operator fun set(property: Property?, value: Double)
    external operator fun get(property: Property?): Double

    fun loadEngine(recognizerModelFile: String?) {
        if (null == recognizerModelFile || "" == recognizerModelFile) {
            Log.w(
                TAG,
                "detectModelFile file path is invalid!"
            )
            return
        }
        this.construct1(recognizerModelFile)
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
}