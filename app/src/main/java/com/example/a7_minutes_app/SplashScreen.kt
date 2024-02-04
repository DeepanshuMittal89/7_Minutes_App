package com.example.a7_minutes_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.a7_minutes_app.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val logo = findViewById<ImageView>(R.id.logo)
        val Animation = AnimationUtils.loadAnimation(this,R.anim.scale)
        logo.startAnimation(Animation)
        android.os.Handler(Looper.getMainLooper())
            .postDelayed(
                {
                    startActivity(Intent(this@SplashScreen,MainActivity::class.java))
                    finish()
                },5000
            )
    }
}