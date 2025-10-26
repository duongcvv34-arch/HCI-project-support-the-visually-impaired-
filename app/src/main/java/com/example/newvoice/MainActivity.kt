package com.example.newvoice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    private val REQUIRED_PERMISSIONS = arrayOf(
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.POST_NOTIFICATIONS
    )
    private val REQUEST_CODE_PERMISSIONS = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread.sleep(3000)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        //Kiểm tra & xin quyền
        checkAndRequestPermissions()

        val button4: Button = findViewById(R.id.button4)

        button4.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkAndRequestPermissions() {
        val missingPermissions = REQUIRED_PERMISSIONS.filter {
            androidx.core.content.ContextCompat.checkSelfPermission(
                this, it
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            androidx.core.app.ActivityCompat.requestPermissions(
                this,
                missingPermissions.toTypedArray(),
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            val denied = grantResults.indices.filter {
                grantResults[it] != android.content.pm.PackageManager.PERMISSION_GRANTED
            }.map { permissions[it] }

            if (denied.isEmpty()) {
                Toast.makeText(this, "Đã cấp đủ quyền!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Chưa cấp quyền: ${denied.joinToString()}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
