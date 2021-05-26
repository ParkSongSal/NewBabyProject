package com.example.newbabyproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newbabyproject.Retrofit2.*
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.Callback as Callback

class LoginActivity : AppCompatActivity() {


    var userId = ""
    var userPassword = ""




    private val TAG = this::class.java.simpleName
    private lateinit var retrofit : Retrofit
    private lateinit var mUserApi: userApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Retrofit 서버연결
        initRetrofit()

    }

    fun onClick(view: View) {

        when (view.id) {
            R.id.loginBtn -> Login()
            R.id.registerTxt -> Register()
            //R.id.inquireTxt -> Inquire()
        }
    }

    // 회원가입 화면 이동
    fun Register(){
        //로그인 성공
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
    }

    // Retrofit 서버연결
    fun initRetrofit(){
        retrofit = RetrofitClient.getInstance()
        mUserApi = retrofit.create(userApi::class.java)
    }



    fun Login(){
        userId = idEditText.text.toString()
        userPassword = pwEditText.text.toString()

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

                    //로그인 성공
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()

                    Toast.makeText(this@LoginActivity,"$userId 님 환영합니다!",Toast.LENGTH_LONG).show()
                } else {  // 로그인 실패
                    Toast.makeText(this@LoginActivity,"입력하신 정보를 다시 확인바랍니다.",Toast.LENGTH_LONG).show()
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
}

