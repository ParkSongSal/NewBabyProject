package com.example.newbabyproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.newbabyproject.Retrofit2.*
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginActivity : BaseActivity() {

    private val PERMISSION_ALLOW = 1
    val READ_PHONE_PERMISSON = 1

    var userId = ""
    var userPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Retrofit 서버연결
        init(this@LoginActivity)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "로그인"

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) !== PackageManager.PERMISSION_GRANTED
        ){


            //Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED ||
            //ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,  // 저장공간
                    Manifest.permission.READ_EXTERNAL_STORAGE,  // 저장공간
                    Manifest.permission.READ_PHONE_STATE
                ),
                PERMISSION_ALLOW
            )
        }

        chk_auto.setOnCheckedChangeListener { buttonView, isChecked ->
            if (chk_auto.isChecked) {
                val ID: String = idEditText.text.toString()
                val PW: String = pwEditText.text.toString()
                if (ID == "" || PW == "") {
                    //중복인 닉네임 존재
                    dlg.setMessage("닉네임과 패스워드를 입력해주세요.")
                        .setNegativeButton("다시 시도", null)
                    dlg.show()
                    chk_auto.isChecked = false
                }else if(ID != "" && PW != ""){
                    editor.putString("ID", ID)
                    editor.putString("PW", PW)
                    editor.putBoolean("chk_auto", true)
                    editor.apply()
                }

            } else {
                setting = getSharedPreferences("setting", MODE_PRIVATE)
                editor = setting.edit()
                editor.clear()
                editor.commit()
            }
        }

        // 아이디, 패스워드 저장 기능
        if(setting.getBoolean("chk_auto", false)){
            userId = setting.getString("ID", "").toString()
            userPassword = setting.getString("PW", "").toString()
            idEditText.setText(userId)
            pwEditText.setText(userPassword)
            Login(userId, userPassword)

            chk_auto.isChecked = true

        }


    }

    fun onClick(view: View) {

        when (view.id) {
            // 로그인
            R.id.loginBtn -> {
                userId = idEditText.text.toString()
                userPassword = pwEditText.text.toString()
                Login(userId, userPassword)
            }

            // 회원가입
            R.id.registerTxt -> Register()

            // 문의하기
            //R.id.inquireTxt -> Inquire()
        }
    }

    // 회원가입 화면 이동
    fun Register(){
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
    }

    fun Login(userId: String, userPassword: String){

        var lastedDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")

         if("" == userId){
             Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
             return;
         }else if("" == userPassword){
             Toast.makeText(this, "패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
             return;
         }
        mUserApi.userLogin(userId, userPassword).enqueue(object : Callback<ResultModel> {
            override fun onResponse(
                call: retrofit2.Call<ResultModel>,
                response: Response<ResultModel>
            ) {

                if ("success" == response.body()?.result) { // 로그인 성공

                    editor.putString("loginId", userId)
                    editor.apply()

                    //로그인 성공
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()

                    Toast.makeText(this@LoginActivity, "$userId 님 환영합니다!", Toast.LENGTH_SHORT).show()
                } else {  // 로그인 실패
                    Toast.makeText(this@LoginActivity, "입력하신 정보를 다시 확인바랍니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: retrofit2.Call<ResultModel>, t: Throwable) {

                // 네트워크 문제
                Toast.makeText(
                    this@LoginActivity,
                    "데이터 접속 w상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()

            }
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_PHONE_PERMISSON -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "권한 승인함", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "권한 거부함", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

