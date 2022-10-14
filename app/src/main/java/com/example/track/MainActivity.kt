package com.example.track

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.*
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.track.database.AppDatabase
import com.example.track.database.Feature
import com.example.track.seeta6.*
import com.google.gson.Gson
import java.io.IOException
import java.util.*
import kotlin.system.measureTimeMillis


class MainActivity : AppCompatActivity() {

    var surfaceholder: SurfaceHolder? = null
    var camera: Camera? = null
    var surfaceView: SurfaceView? = null
    var surfaceView_view: SurfaceView? = null
    var btnCapture: Button? = null
    var container: FrameLayout? = null
    var faceTracker: FaceTracker? = null
    var faceLandmarker: FaceLandmarker? = null
    var poseEstimator: PoseEstimator? = null
    //var faceDetector: FaceDetector? = null
    var faceRecognizer: FaceRecognizer? = null
    var db: AppDatabase? = null
    var canvas: Canvas? = null
    var cameraWidth = 640
    var cameraHeight = 480

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCapture = findViewById(R.id.btnCapture)
        container = findViewById(R.id.container)
        surfaceView = findViewById(R.id.surfaceView)
        surfaceView_view = findViewById(R.id.surfaceView_view)
        surfaceView_view?.setZOrderMediaOverlay(true)
        surfaceView_view?.getHolder()?.setFormat(PixelFormat.TRANSLUCENT)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //has permision
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 999)
        }

        //faceDetector = FaceDetector(this@MainActivity)
        faceTracker = FaceTracker(this@MainActivity, cameraWidth, cameraHeight)
        faceLandmarker = FaceLandmarker(this@MainActivity)
        faceRecognizer = FaceRecognizer(this@MainActivity)
        poseEstimator = PoseEstimator(this@MainActivity)

        surfaceholder = surfaceView?.holder
        surfaceholder?.addCallback(callback)
        surfaceholder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        db = db?.getDatabase(this@MainActivity)

    }

    var callback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {

        override fun surfaceCreated(holder: SurfaceHolder) {
            try {
                camera = Camera.open(1)
                var parameters = camera?.getParameters()
                if(resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE){
                    parameters?.set("orientation", "portrait")
                    camera?.setDisplayOrientation(90)
                    parameters?.setRotation(90)
                }else{
                    parameters?.set("orientation", "landscape")
                    camera?.setDisplayOrientation(0)
                    parameters?.setRotation(0)
                }
                parameters?.setPictureSize(cameraWidth, cameraHeight)
                parameters?.setPreviewSize(cameraWidth, cameraHeight)
                camera?.setParameters(parameters)
                camera?.setPreviewDisplay(holder)

                var feature = FloatArray(faceRecognizer!!.GetExtractFeatureSize())
                var featureCapture = FloatArray(faceRecognizer!!.GetExtractFeatureSize())

                var yaw = FloatArray(1)
                var pitch = FloatArray(1)
                var roll = FloatArray(1)

                camera?.setPreviewCallback { bytes, camera ->

                    var newBytes = yuv2rgb(bytes, cameraWidth, cameraHeight)
                    var seetaImageData = SeetaImageData(cameraWidth, cameraHeight, 3)
                    seetaImageData!!.data = newBytes

                    var seetaTrackingFaceInfos = faceTracker?.Track(seetaImageData)

                    if (seetaTrackingFaceInfos?.size!! > 0) {
                        var seetaRect = SeetaRect()
                        var seetaPointFs = Array(5) {SeetaPointF()}

                        for (stTFInfo in seetaTrackingFaceInfos!!) {
                            seetaRect.x = stTFInfo!!.x
                            seetaRect.y = stTFInfo.y
                            seetaRect.width = stTFInfo.width
                            seetaRect.height = stTFInfo.height

                            faceLandmarker?.mark(seetaImageData, seetaRect, seetaPointFs)
                            poseEstimator?.Estimate(seetaImageData, seetaRect, yaw, pitch, roll)
                            for(i in pitch){
                                Log.d("dovt7", "yaw: " + i)
                            }
                            faceRecognizer?.Extract(seetaImageData, seetaPointFs, feature)
                            var sim = faceRecognizer?.CalculateSimilarity( featureCapture, feature)
                            Log.d("dovt", "sim: " + sim)
                            if (sim != null && sim >=0.74) {

                            }
                            val maxRect = Rect(
                                stTFInfo!!.x,
                                stTFInfo.y,
                                stTFInfo.width + stTFInfo.x,
                                stTFInfo.height + stTFInfo.y
                            )
                            if (surfaceView_view != null) {
                                canvas = null
                                surfaceView_view?.setVisibility(SurfaceView.VISIBLE)
                                try {
                                    canvas = surfaceView_view?.getHolder()?.lockCanvas()
                                    if (canvas != null) {
                                        synchronized(surfaceView_view!!.holder) {
                                            canvas!!.drawColor(0, PorterDuff.Mode.CLEAR)

                                            if(seetaPointFs.size > 0){
                                                for(seetaPointF in seetaPointFs){

                                                    var adjustedPoint: SeetaPointF? = DrawUtils().adjustPoint(
                                                        seetaPointF,
                                                        cameraWidth,
                                                        cameraHeight,
                                                        canvas!!.getWidth(),
                                                        canvas!!.getHeight(),
                                                        90,
                                                        1
                                                    )

                                                    DrawUtils().drawSeetaPointF(
                                                        canvas,
                                                        adjustedPoint,
                                                        Color.RED,
                                                        10
                                                    )
                                                }
                                            }

                                            val rect = Rect(
                                                maxRect.left,
                                                maxRect.top,
                                                maxRect.right,
                                                maxRect.bottom
                                            )

                                            if (rect != null) {
                                                val adjustedRect: Rect? = DrawUtils().adjustRect(
                                                    rect,
                                                    cameraWidth,
                                                    cameraHeight,
                                                    canvas!!.getWidth(),
                                                    canvas!!.getHeight(),
                                                    90,
                                                    1
                                                )
                                                DrawUtils().drawFaceRect(
                                                    canvas,
                                                    adjustedRect,
                                                    Color.WHITE,
                                                    5
                                                )
                                            }
                                        }
                                    }
                                } catch (ex: Exception) {
                                } finally {
                                    if (canvas != null) {
                                        surfaceView_view?.getHolder()?.unlockCanvasAndPost(canvas)
                                    }
                                }
                            }
                        }
                    } else {
                        if (surfaceView_view != null) {
                            surfaceView_view!!.setVisibility(SurfaceView.INVISIBLE)
                        }
                    }
                }
                camera?.startPreview()
                btnCapture?.setOnClickListener{
                    featureCapture = takePictureInternal(camera!!)
                }
            } catch (e: IOException) {
                Log.d("TAG", "Error setting camera preview: " + e.message)
            }
        }
        fun takePictureInternal(camera: Camera) : FloatArray {

            var features = FloatArray(faceRecognizer!!.GetExtractFeatureSize())

            camera?.takePicture(null, null, Camera.PictureCallback { data, camera ->
                val gson = Gson()
                var bmp = jpegToBitmap(data)
                var face1Bytes = getNV21(bmp.width, bmp.height, bmp)
                var newBytes = yuv2rgb(face1Bytes, cameraWidth, cameraHeight)

                var seetaImageData = SeetaImageData(cameraWidth, cameraHeight, 3)
                seetaImageData!!.data = newBytes

                var seetaTrackingFaceInfos = faceTracker?.Track(seetaImageData)
                Log.d("dovt4", "register: " + seetaTrackingFaceInfos?.size)

                if (seetaTrackingFaceInfos?.size!! > 0) {

                    var seetaRect = SeetaRect()
                    var seetaPointFs = Array(5) { SeetaPointF() }


                    for (stTFInfo in seetaTrackingFaceInfos!!) {
                        seetaRect.x = stTFInfo!!.x
                        seetaRect.y = stTFInfo.y
                        seetaRect.width = stTFInfo.width
                        seetaRect.height = stTFInfo.height
                        faceLandmarker?.mark(seetaImageData, seetaRect, seetaPointFs)
                        faceRecognizer?.Extract(seetaImageData, seetaPointFs, features)

                        var feature = Feature(randomID(),randomUUID(),"dovt", features.toString())
                        //db?.featuresDao()?.insertFeature(feature)
                    }
                }

                camera.cancelAutoFocus()
                camera.startPreview()
            })
            return features
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.
            if (holder.surface == null) {
                // preview surface does not exist
                return
            }

            // stop preview before making changes
            try {
                camera!!.stopPreview()
            } catch (e: Exception) {
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                camera!!.setPreviewDisplay(holder)
                camera!!.startPreview()
            } catch (e: Exception) {
                Log.d("TAG", "Error starting camera preview: " + e.message)
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            camera?.setPreviewCallback(null)
            camera?.stopPreview()
            camera?.release()
            camera = null
        }

        fun register(name: String, seetaImageData: SeetaImageData){

            val gson = Gson()

//            var bmp = BitmapFactory.decodeResource(resources, R.drawable.face3)
//            var face1Bytes = getNV21(bmp.width, bmp.height, bmp)
//
//            var newBytes = yuv2rgb(face1Bytes, cameraWidth, cameraHeight)

            //var seetaImageData = SeetaImageData(cameraWidth, cameraHeight, 3)
            //seetaImageData!!.data = newBytes.clone()

            var seetaTrackingFaceInfos = faceTracker?.Track(seetaImageData)
            Log.d("dovt4", "register: " + seetaTrackingFaceInfos?.size)

            if (seetaTrackingFaceInfos?.size!! > 0) {

                var seetaRect = SeetaRect()
                var seetaPointFs = Array(5) { SeetaPointF() }
                var features = FloatArray(faceRecognizer!!.GetExtractFeatureSize())

                for (stTFInfo in seetaTrackingFaceInfos!!) {
                    seetaRect.x = stTFInfo!!.x
                    seetaRect.y = stTFInfo.y
                    seetaRect.width = stTFInfo.width
                    seetaRect.height = stTFInfo.height
                    faceLandmarker?.mark(seetaImageData, seetaRect, seetaPointFs)
                    faceRecognizer?.Extract(seetaImageData, seetaPointFs, features)

                    var feature = Feature(randomID(),randomUUID(),name, features.toString())
                    db?.featuresDao()?.insertFeature(feature)
                }
            }
        }

        fun yuv2rgb(yuv: ByteArray, width: Int, height: Int): ByteArray {
            val total = width * height
            var listRgb: MutableList<Byte> = arrayListOf()
            //val rgb = ByteArray(total)
            var Y: Int
            var Cb = 0
            var Cr = 0
            //var index = 0
            var R: Int
            var G: Int
            var B: Int
            for (y in 0 until height) {
                for (x in 0 until width) {
                    Y = yuv[y * width + x].toInt()
                    if (Y < 0) Y += 255
                    if (x and 1 == 0) {
                        Cr = yuv[(y shr 1) * width + x + total].toInt()
                        Cb = yuv[(y shr 1) * width + x + total + 1].toInt()
                        if (Cb < 0) Cb += 127 else Cb -= 128
                        if (Cr < 0) Cr += 127 else Cr -= 128
                    }
//                R = Y + Cr + (Cr shr 2) + (Cr shr 3) + (Cr shr 5)
//                G =
//                    Y - (Cb shr 2) + (Cb shr 4) + (Cb shr 5) - (Cr shr 1) + (Cr shr 3) + (Cr shr 4) + (Cr shr 5)
//                B = Y + Cb + (Cb shr 1) + (Cb shr 2) + (Cb shr 6)

                    // Approximation
                    R = ((Y + 1.40200 * Cr).toInt());
                    G = ((Y - 0.34414 * Cb - 0.71414 * Cr).toInt());
                    B = ((Y + 1.77200 * Cb).toInt());

                    if (R < 0) R = 0 else if (R > 255) R = 255
                    if (G < 0) G = 0 else if (G > 255) G = 255
                    if (B < 0) B = 0 else if (B > 255) B = 255
                    //rgb[index++] = -0x1000000 + (R shl 16) + (G shl 8) + B
                    listRgb?.add(B.toByte())
                    listRgb?.add(G.toByte())
                    listRgb?.add(R.toByte())
                }
            }
            return listRgb.toByteArray()
        }

        fun getNV21(inputWidth: Int, inputHeight: Int, scaled: Bitmap): ByteArray {
            val argb = IntArray(inputWidth * inputHeight)
            scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight)
            val yuv = ByteArray(inputWidth * inputHeight * 3 / 2)
            encodeYUV420SP(yuv, argb, inputWidth, inputHeight)
            scaled.recycle()
            return yuv
        }
        fun jpegToBitmap(jpegData: ByteArray): Bitmap{
            return BitmapFactory.decodeByteArray(jpegData, 0, jpegData.size)
        }

        fun encodeYUV420SP(yuv420sp: ByteArray, argb: IntArray, width: Int, height: Int) {
            val frameSize = width * height
            var yIndex = 0
            var uvIndex = frameSize
            var a: Int
            var R: Int
            var G: Int
            var B: Int
            var Y: Int
            var U: Int
            var V: Int
            var index = 0
            for (j in 0 until height) {
                for (i in 0 until width) {
                    a = argb[index] and -0x1000000 shr 24 // a is not used obviously
                    R = argb[index] and 0xff0000 shr 16
                    G = argb[index] and 0xff00 shr 8
                    B = argb[index] and 0xff shr 0

// well known RGB to YUV algorithm
                    Y = (66 * R + 129 * G + 25 * B + 128 shr 8) + 16
                    U = (-38 * R - 74 * G + 112 * B + 128 shr 8) + 128
                    V = (112 * R - 94 * G - 18 * B + 128 shr 8) + 128

// NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2

// meaning for every 4 Y pixels there are 1 V and 1 U. Note the sampling is every other

// pixel AND every other scanline.
                    yuv420sp[yIndex++] = (if (Y < 0) 0 else if (Y > 255) 255 else Y).toByte()
                    if (j % 2 == 0 && index % 2 == 0) {
                        yuv420sp[uvIndex++] = (if (V < 0) 0 else if (V > 255) 255 else V).toByte()
                        yuv420sp[uvIndex++] = (if (U < 0) 0 else if (U > 255) 255 else U).toByte()
                    }
                    index++
                }
            }
        }

        fun randomUUID() = UUID.randomUUID().toString()
        fun randomID(): Long = Math.random().toLong()
    }
}