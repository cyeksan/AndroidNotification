package com.csappgenerator.androidnotification

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startMyServiceBtn: Button = findViewById(R.id.notification_btn)
        startMyServiceBtn.setOnClickListener { startMyService() }

    }

    private fun startMyService() {
        val intent = Intent(this, MyForegroundService::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = "start"
        startService(intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}