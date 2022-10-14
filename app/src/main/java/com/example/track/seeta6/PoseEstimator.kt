package com.example.track.seeta6

import android.content.Context
import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PoseEstimator {
    private val TAG = FaceRecognizer::class.java.simpleName
    private var context: Context
    companion object {
        init {
            System.loadLibrary("PoseEstimation600_java")
        }
    }

    var impl: Long = 0
    private external fun construct(model: String)
    //private external fun construct(model: String, device: String, id: Int)

    external fun dispose()
    @Throws(Throwable::class)
    protected fun finalize() {
        //super.finalize()
        dispose()
    }

    external fun Estimate(
        image: SeetaImageData?,
        face: SeetaRect?,
        yaw: FloatArray?,
        pitch: FloatArray?,
        roll: FloatArray?
    )

    fun loadEngine(PoseModelFile: String?) {
        if (null == PoseModelFile || "" == PoseModelFile) {
            Log.w(
                TAG,
                "detectModelFile file path is invalid!"
            )
            return
        }
        this.construct(PoseModelFile)
    }

    fun loadEngine() {
        if (null == context) {
            Log.w(
                TAG,
                "please call initial first!"
            )
        }
        Log.w("dovt1: ", "loadEngine: " + getPath("pose_estimation.csta", context))
        loadEngine(getPath("pose_estimation.csta", context))
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

//    constructor(model: String, device: String, id: Int) {
//        this.construct(model, device, id)
//    }

    constructor(context: Context) {
        this.context = context
        this.loadEngine()
    }
}