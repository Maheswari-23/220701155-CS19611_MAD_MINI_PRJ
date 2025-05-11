package com.example.quickscribe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Find views from layout
        val tvAppName = findViewById<TextView>(R.id.tvAppName)
        val tvTagline = findViewById<TextView>(R.id.tvTagline)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Apply animations
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        tvAppName.startAnimation(fadeIn)
        tvTagline.startAnimation(fadeIn)
        progressBar.startAnimation(fadeIn)

        // Proceed to main activity after delay
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000) // Increased to 2 seconds to make sure it has time to transition
    }
}