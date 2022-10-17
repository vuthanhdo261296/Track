package com.example.track

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.os.Process
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.track.seeta6.*
import java.util.*


class MainActivity : AppCompatActivity() {
    public var PERMISSION_REQ = 0x123456;

    private var mPermission = arrayOf<String>(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.SYSTEM_ALERT_WINDOW
    )

    private var mRequestPermission = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var faceDetector = FaceDetector(applicationContext)
//        faceDetector.loadEngine()
//        Log.d("dovt1: ", faceDetector.helloWorld().toString())

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            for (one in mPermission) {
                if (PackageManager.PERMISSION_GRANTED != this.checkPermission(
                        one,
                        Process.myPid(),
                        Process.myUid()
                    )
                ) {
                    mRequestPermission.add(one)
                }
                if (!mRequestPermission.isEmpty()) {
                    this.requestPermissions(mRequestPermission.toTypedArray(), PERMISSION_REQ)
                }
            }
        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.detect -> startActivity(Intent(this, FaceRecognition::class.java))
            R.id.dmp -> startActivity(Intent(this, DmpCommunication::class.java))
        }
    }
}