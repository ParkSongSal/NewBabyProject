package com.example.newbabyproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.newbabyproject.Retrofit2.ResultModel
import com.example.newbabyproject.Retrofit2.RetrofitClient
import com.example.newbabyproject.Retrofit2.boardApi
import kotlinx.android.synthetic.main.activity_app_introduce.*
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AppIntroduceActivity : BaseActivity() {

    var loginId = ""
    // 아이디 저장 기능
    lateinit var setting: SharedPreferences
    lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_introduce)

        init(applicationContext)
        introduceValidate("0")

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
                    startActivity(intent)
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

    }


}


