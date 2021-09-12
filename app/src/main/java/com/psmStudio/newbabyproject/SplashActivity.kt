package com.psmStudio.newbabyproject

import android.os.Bundle
import android.os.Handler
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.psmStudio.newbabyproject.utils.Common

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val actionBar = supportActionBar
        actionBar?.hide()

        init(this@SplashActivity)


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