package com.baliproject.scoreboardtennis

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.baliproject.scoreboardtennis.UI.Activity.MainActivity
import com.baliproject.scoreboardtennis.UI.Activity.StartEventActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val logoImage: ImageView = findViewById(R.id.logoImage)
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        logoImage.startAnimation(animation)

        // Delay pindah ke MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, StartEventActivity::class.java))
            finish()
        }, 2000) // durasi splash 2 detik
    }
}