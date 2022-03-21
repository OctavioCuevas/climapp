package com.example.climapp.domains

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.climapp.R
import com.example.climapp.databinding.ActivitySplashScreenBinding
import com.example.climapp.ui.view.MainActivity

class Splash : AppCompatActivity(){

    fun showSplash(splashScreenBinding: ActivitySplashScreenBinding,context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val splashAnimation = AnimationUtils.loadAnimation(context, R.anim.splashanim)
        splashScreenBinding.appTextView.animation = splashAnimation

        splashAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                splashScreenBinding.appTextView.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 1000)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
}