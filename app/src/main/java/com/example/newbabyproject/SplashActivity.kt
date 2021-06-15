package com.example.newbabyproject

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.newbabyproject.utils.Common

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed({
            Common.intentCommon(this@SplashActivity, LoginActivity::class.java)
            finish()
        }, 1200) // 2.5초후에 실행
    }
}