package com.psmStudio.newbabyproject

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.psmStudio.newbabyproject.Retrofit2.ResultModel
import com.psmStudio.newbabyproject.Retrofit2.RetrofitClient
import com.psmStudio.newbabyproject.Retrofit2.boardApi
import kotlinx.android.synthetic.main.activity_app_introduce.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.item_toolbar.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AppIntroduceActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_introduce)

        /*val actionBar: ActionBar? = supportActionBar
        //actionBar?.hide()
        actionBar?.title = "앱 소개"*/

        toolbar.setTitleTextColor(getColor(R.color.whiteColor))
        toolbar.title = "앱 소개"
        setSupportActionBar(toolbar)
        init(applicationContext)
        introduceValidate("0")  // 앱 소개문

        setting = getSharedPreferences("setting", MODE_PRIVATE)
        editor = setting.edit()
        editor.apply()


        loginId = setting.getString("loginId", "").toString()

        if (loginId == "admin") {
            modifyBtn.visibility = View.VISIBLE
            // 관리자만 수정 가능
            modifyBtn.setOnClickListener {


                if (validate) {
                    val intent =
                        Intent(this@AppIntroduceActivity, AppIntroduceModifyActivity::class.java)


                    intent.putExtra("boardGubun", "0")
                    intent.putExtra("actGubun", actGubun)
                    if(actGubun == "update"){
                        intent.putExtra("content",appIntroTxt.text)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@AppIntroduceActivity,
                        "다시 시도 바랍니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        } else {
            modifyBtn.visibility = View.GONE
        }

        introduceList("0")

    }


}


