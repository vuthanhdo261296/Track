package com.example.track

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.track.seeta6.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    var surfaceholder: SurfaceHolder? = null
    var camera: Camera? = null
    var surfaceView: SurfaceView? = null
    var surfaceView_view: SurfaceView? = null
    var faceTracker: FaceTracker? = null
    var faceLandmarker: FaceLandmarker? = null
    var canvas: Canvas? = null
    var cameraWidth = 640
    var cameraHeight = 480

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        surfaceView = findViewById(R.id.surfaceView)
        surfaceView_view = findViewById(R.id.surfaceView_view)
        surfaceView_view?.setZOrderMediaOverlay(true)
        surfaceView_view?.getHolder()?.setFormat(PixelFormat.TRANSLUCENT)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //has permision
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 999)
        }

        faceTracker = FaceTracker(this@MainActivity, cameraWidth, cameraHeight)
        faceLandmarker = FaceLandmarker(this@MainActivity)

        surfaceholder = surfaceView?.holder
        surfaceholder?.addCallback(callback)
        surfaceholder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

    }

    var callback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {

        override fun surfaceCreated(holder: SurfaceHolder) {
            try {
                camera = Camera.open(1)
                var parameters = camera?.getParameters()
                parameters?.setPreviewSize(cameraWidth, cameraHeight)
                camera?.setParameters(parameters)
                camera?.setDisplayOrientation(90)
                camera?.setPreviewDisplay(holder)
                camera?.setPreviewCallback { bytes, camera ->

                    var newBytes = yuv2rgb(bytes, cameraWidth, cameraHeight)

                    var seetaImageData = SeetaImageData(cameraWidth, cameraHeight, 3)
                    seetaImageData!!.data = newBytes.clone()

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
                            Log.d("dovt1: ", "seetaPointFs: " + seetaPointFs.size)

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
            } catch (e: IOException) {
                Log.d("TAG", "Error setting camera preview: " + e.message)
            }
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

    }
}