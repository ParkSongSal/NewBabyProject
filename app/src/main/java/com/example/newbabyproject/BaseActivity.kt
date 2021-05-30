package com.example.newbabyproject

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.newbabyproject.Retrofit2.ResultModel
import com.example.newbabyproject.Retrofit2.RetrofitClient
import com.example.newbabyproject.Retrofit2.boardApi
import com.example.newbabyproject.Retrofit2.userApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

abstract class BaseActivity : AppCompatActivity() {

    var boardGubun: String? = ""
    var actGubun : String? = ""
    var validate = false

    lateinit var retrofit: Retrofit
    lateinit var mBoardApi: boardApi
    lateinit var mUserApi: userApi

    lateinit var dlg: AlertDialog.Builder

    lateinit var coxt : Context

    // 아이디 저장 기능
    lateinit var setting : SharedPreferences
    lateinit var editor: SharedPreferences.Editor



    fun init(context : Context) {

        coxt = context

        // Retrofit Init
        retrofit = RetrofitClient.getInstance()
        mBoardApi = retrofit.create(boardApi::class.java)
        mUserApi = retrofit.create(userApi::class.java)

        // AlertDialog Init
        dlg = AlertDialog.Builder(
            context,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )

        // SharedPreferences Init
        setting = getSharedPreferences("setting", MODE_PRIVATE)
        editor = setting.edit()
        editor.apply()
    }

    /* 앱소개
    * 입원 안내문
    * 퇴원 안내문
    * 등록 / 수정 체크
    * */
    fun introduceValidate(boardGubun : String) {

        val boardGubunPart = RequestBody.create(MultipartBody.FORM, boardGubun)


        mBoardApi.getIntroduceValidate(boardGubunPart).enqueue(object : Callback<ResultModel> {
            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {

                Log.d("TAG", "response : ${response.body()}")

                // 정상결과
                if (response.body()!!.result == "insert") {
                    actGubun = "insert"
                    validate = true
                    Toast.makeText(
                        coxt,
                        "소개문을 신규 작성합니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    actGubun = "update"
                    validate = true
                    Toast.makeText(
                        coxt,
                        "소개문을 수정 작성합니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
                validate = false
                // 네트워크 문제
                Toast.makeText(
                    coxt,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}