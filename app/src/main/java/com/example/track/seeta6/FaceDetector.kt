package com.example.track.seeta6

import android.content.Context
import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class FaceDetector {

    private val TAG = FaceDetector::class.java.simpleName
    private var context: Context

    companion object {
        init {
            System.loadLibrary("SeetaFaceDetector600_java")
        }
    }

    enum class Property(val value: Int) {
        PROPERTY_MIN_FACE_SIZE(0),
        PROPERTY_THRESHOLD(1),
        PROPERTY_MAX_IMAGE_WIDTH(2),
        PROPERTY_MAX_IMAGE_HEIGHT(3),
        PROPERTY_NUMBER_THREADS(4),
        PROPERTY_ARM_CPU_MODE(0x101);
    }

    external fun helloWorld()

    var impl: Long = 0

    @Throws(Exception::class)
    external fun construct(setting: SeetaModelSetting)
    external fun construct1(detectModelFile: String)
    external fun dispose()

    @Throws(Throwable::class)
    protected fun finalize() {
        //super.finalize()
        dispose()
    }

    external fun Detect(image: SeetaImageData?): Array<SeetaRect?>?
    external operator fun set(property: Property?, value: Double)
    external operator fun get(property: Property?): Double

    fun loadEngine(detectModelFile: String?) {
        if (null == detectModelFile || "" == detectModelFile) {
            Log.w(
                TAG,
                "detectModelFile file path is invalid!"
            )
            return
        }
        var model = detectModelFile.split("").toTypedArray()
        var setting = SeetaModelSetting(0, model, SeetaDevice.SEETA_DEVICE_AUTO)
        Log.w("dovt1: ", "detectModelFile: " + detectModelFile)
        this.construct1(detectModelFile)
    }

    fun loadEngine() {
        if (null == context) {
            Log.w(
                TAG,
                "please call initial first!"
            )
        }
        Log.w("dovt1: ", "loadEngine: " + getPath("face_detector.csta", context))
        loadEngine(getPath("face_detector.csta", context))
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

    fun releaseEngine() {
        finalize()
    }

    constructor(context: Context) {
        this.context = context
        this.loadEngine()
    }
}