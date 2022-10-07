package com.example.track.seeta6

import android.content.Context
import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FaceLandmarker {
    private val TAG = FaceLandmarker::class.java.simpleName
    private var context: Context
    companion object {
        init {
            System.loadLibrary("FaceLandmarker600_java")
        }
    }

    var impl: Long = 0
    @Throws(Throwable::class)
    external fun construct(seeting: SeetaModelSetting)
    external fun construct1(model: String)

    external fun dispose()
    @Throws(Throwable::class)
    protected fun finalize() {
        //super.finalize()
        dispose()
    }

    external fun number(): Int
    external fun mark(
        imageData: SeetaImageData?,
        seetaRect: SeetaRect?,
        pointFS: Array<SeetaPointF>?
    )

    external fun mark1(
        imageData: SeetaImageData?,
        seetaRect: SeetaRect?,
        pointFS: Array<SeetaPointF?>?,
        masks: IntArray?
    )

    fun loadEngine(landmarkerModelFile: String?) {
        if (null == landmarkerModelFile || "" == landmarkerModelFile) {
            Log.w(
                TAG,
                "detectModelFile file path is invalid!"
            )
            return
        }
        this.construct1(landmarkerModelFile)
    }

    fun loadEngine() {
        if (null == context) {
            Log.w(
                TAG,
                "please call initial first!"
            )
        }
        Log.w("dovt1: ", "loadEngine: " + getPath("face_detector.csta", context))
        loadEngine(getPath("face_landmarker_pts5.csta", context))
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