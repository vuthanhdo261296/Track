package com.example.track

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.hardware.Camera
import android.util.Log

class DrawUtils() {
    fun adjustRect(
        rect: Rect?,
        previewWidth: Int,
        previewHeight: Int,
        canvasWidth: Int,
        canvasHeight: Int,
        cameraOri: Int,
        mCameraId: Int
    ): Rect? {
        Log.d(
            "dovt3: ", "adjustRect: " + previewWidth + " " + previewHeight + " " + canvasWidth +
                    " " + canvasHeight + " " + cameraOri + " " + mCameraId
        )
        var previewWidth = previewWidth
        var previewHeight = previewHeight
        if (rect == null) {
            return null
        }
        if (canvasWidth < canvasHeight) {
            val t = previewHeight
            previewHeight = previewWidth
            previewWidth = t
        }
        val widthRatio = canvasWidth.toFloat() / previewWidth.toFloat()
        val heightRatio = canvasHeight.toFloat() / previewHeight.toFloat()
        if (cameraOri == 0 || cameraOri == 180) {
            rect.left *= widthRatio.toInt()
            rect.right *= widthRatio.toInt()
            rect.top *= heightRatio.toInt()
            rect.bottom *= heightRatio.toInt()
        } else {
            var left = rect.left * heightRatio
            var right = rect.right * heightRatio
            var top = rect.top * widthRatio
            var bottom = rect.bottom * widthRatio

            rect.left = left.toInt()
            rect.right = right.toInt()
            rect.top = top.toInt()
            rect.bottom = bottom.toInt()
        }
        val newRect = Rect()
        when (cameraOri) {
            0 -> {
                if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    newRect.left = canvasWidth - rect.left
                    newRect.right = canvasWidth - rect.right
                } else {
                    newRect.left = rect.left
                    newRect.right = rect.right
                }
                newRect.top = rect.top
                newRect.bottom = rect.bottom
            }
            90 -> {
                newRect.right = canvasWidth - rect.top
                newRect.left = canvasWidth - rect.bottom
                if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    newRect.top = canvasHeight - rect.left
                    newRect.bottom = canvasHeight - rect.right
                } else {
                    newRect.top = rect.left
                    newRect.bottom = rect.right
                }
            }
            180 -> {
                newRect.top = canvasHeight - rect.bottom
                newRect.bottom = canvasHeight - rect.top
                if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    newRect.left = rect.left
                    newRect.right = rect.right
                } else {
                    newRect.left = canvasWidth - rect.right
                    newRect.right = canvasWidth - rect.left
                }
            }
            270 -> {
                newRect.left = rect.top
                newRect.right = rect.bottom
                if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    newRect.top = rect.left
                    newRect.bottom = rect.right
                } else {
                    newRect.top = canvasHeight - rect.right
                    newRect.bottom = canvasHeight - rect.left
                }
            }
            else -> {}
        }
        return newRect
    }

    fun drawFaceRect(canvas: Canvas?, rect: Rect?, color: Int, faceRectThickness: Int) {
        if (canvas == null || rect == null) {
            return
        }
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = faceRectThickness.toFloat()
        paint.color = color
        val mPath = Path()
        mPath.moveTo(rect.left.toFloat(), (rect.top + rect.height() / 4).toFloat())
        mPath.lineTo(rect.left.toFloat(), rect.top.toFloat())
        mPath.lineTo((rect.left + rect.width() / 4).toFloat(), rect.top.toFloat())
        mPath.moveTo((rect.right - rect.width() / 4).toFloat(), rect.top.toFloat())
        mPath.lineTo(rect.right.toFloat(), rect.top.toFloat())
        mPath.lineTo(rect.right.toFloat(), (rect.top + rect.height() / 4).toFloat())
        mPath.moveTo(rect.right.toFloat(), (rect.bottom - rect.height() / 4).toFloat())
        mPath.lineTo(rect.right.toFloat(), rect.bottom.toFloat())
        mPath.lineTo((rect.right - rect.width() / 4).toFloat(), rect.bottom.toFloat())
        mPath.moveTo((rect.left + rect.width() / 4).toFloat(), rect.bottom.toFloat())
        mPath.lineTo(rect.left.toFloat(), rect.bottom.toFloat())
        mPath.lineTo(rect.left.toFloat(), (rect.bottom - rect.height() / 4).toFloat())
        canvas.drawPath(mPath, paint)
    }
}