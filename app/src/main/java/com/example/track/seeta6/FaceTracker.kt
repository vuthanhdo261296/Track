package com.example.track.seeta6

import android.content.Context
import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FaceTracker {

    private val TAG = FaceTracker::class.java.simpleName
    private var context: Context

    companion object {
        init {
            System.loadLibrary("FaceTracker600_java")
        }
    }

    var impl: Long = 0
    private external fun construct(seetaModel: String, videoWidth: Int, videoHeight: Int)
    external fun dispose()

    @Throws(Throwable::class)
    protected fun finalize() {
        //super.finalize()
        dispose()
    }

    external fun SetSingleCalculationThreads(num: Int)
    external fun Track(image: SeetaImageData?): Array<SeetaTrackingFaceInfo?>?
    external fun Track1(image: SeetaImageData?, frame_no: Int): Array<SeetaTrackingFaceInfo?>?
    external fun SetMinFaceSize(size: Int)
    external fun GetMinFaceSize(): Int
    external fun SetVideoStable(stable: Boolean)
    external fun GetVideoStable(): Boolean

    fun loadEngine(detectModelFile: String?, videoWidth: Int, videoHeight: Int) {
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
        this.construct(detectModelFile, videoWidth, videoHeight)
    }

    fun loadEngine(videoWidth: Int, videoHeight: Int) {
        if (null == context) {
            Log.w(
                TAG,
                "please call initial first!"
            )
        }
        Log.w("dovt1: ", "loadEngine: " + getPath("face_detector.csta", context))
        loadEngine(getPath("face_detector.csta", context), videoWidth, videoHeight)
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

    constructor(context: Context, videoWidth: Int, videoHeight: Int) {
        this.context = context
        this.loadEngine(videoWidth, videoHeight)
    }
}