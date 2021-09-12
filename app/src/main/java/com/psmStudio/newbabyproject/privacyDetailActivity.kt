package com.psmStudio.newbabyproject

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.ActionBar

class privacyDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_detail)

        init(this@privacyDetailActivity)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "개인정보처리방침"

        val fileName = intent.getStringExtra("fileNm")

        //웹뷰셋팅

        //웹뷰셋팅
        val webView = findViewById<View>(R.id.webView) as WebView

        webView.webViewClient = WebViewClient() //클릭시 새창 안뜨게

        webView.webChromeClient = WebChromeClient() //현제 페이지에서 일어나는 알람등을 알려 주기 위한 콜백 인터페이스

        //세부 세팅 등록
        //세부 세팅 등록
        val webViewSetting = webView.settings
        // 웹뷰가 캐시를 사용하지 않도록 설정(배포할때 주석처리 해야함)
//        webViewSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 웹뷰가 캐시를 사용하지 않도록 설정(배포할때 주석처리 해야함)
//        webViewSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webViewSetting.useWideViewPort = true // wide viewport를 사용하도록 설정

        webViewSetting.javaScriptEnabled = true //자바스크립트 사용 허용


        webViewSetting.javaScriptCanOpenWindowsAutomatically =
            true // javascript가 window.open()을 사용할 수 있도록 설정

        webViewSetting.setSupportMultipleWindows(false) // 여러개의 윈도우를 사용할 수 있도록 설정


//        webViewSetting.setBuiltInZoomControls(true); // 안드로이드에서 제공하는 줌 아이콘을 사용할 수 있도록 설정
//        webViewSetting.setSupportZoom(true); // 확대,축소 기능을 사용할 수 있도록 설정


//        webViewSetting.setBuiltInZoomControls(true); // 안드로이드에서 제공하는 줌 아이콘을 사용할 수 있도록 설정
//        webViewSetting.setSupportZoom(true); // 확대,축소 기능을 사용할 수 있도록 설정
        webViewSetting.loadsImagesAutomatically = true // 웹뷰가 앱에 등록되어 있는 이미지 리소스를 자동으로 로드하도록 설정


        //로드

        //로드
        webView.loadUrl("file:///android_asset/www/$fileName")
    }
}