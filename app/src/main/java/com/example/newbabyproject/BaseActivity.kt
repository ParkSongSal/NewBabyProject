package com.example.newbabyproject

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.newbabyproject.Retrofit2.*
import kotlinx.android.synthetic.main.activity_app_introduce.*
import kotlinx.android.synthetic.main.activity_enter_introduce.*
import kotlinx.android.synthetic.main.activity_out_introduce.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

abstract class BaseActivity : AppCompatActivity() {

    var loginId = ""

    var introContent : String? = ""
    var enterContent : String? = ""
    var outContent : String? = ""
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



    fun init(context: Context) {

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
    fun introduceValidate(boardGubun: String) {

        val boardGubunPart = RequestBody.create(MultipartBody.FORM, boardGubun)


        mBoardApi.getIntroduceValidate(boardGubunPart).enqueue(object : Callback<ResultModel> {
            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {

                Log.d("TAG", "response : ${response.body()}")

                // 정상결과
                if (response.body()!!.result == "insert") {
                    actGubun = "insert"
                    validate = true
                } else {
                    actGubun = "update"
                    validate = true
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


    fun introduceList(boardGubun: String){
        val boardGubunPart = RequestBody.create(MultipartBody.FORM, boardGubun)

        mBoardApi.getIntroduceList(boardGubunPart).enqueue(object : Callback<List<ResultIntroduce>> {
            override fun onResponse(
                call: Call<List<ResultIntroduce>>,
                response: Response<List<ResultIntroduce>>
            ) {


                //정상 결과
                val result: List<ResultIntroduce>? = response.body()

                Log.d("TAG","list : $result")
                for (i in result!!.indices) {
                    val boardGb: String = result[i].boardGubun

                    when(boardGb){
                        "0"-> appIntroTxt.text = result[i].boardContent
                        "1"-> enterIntroTxt.text = result[i].boardContent
                        "2"-> outIntroTxt.text = result[i].boardContent
                    }
                }

            }

            override fun onFailure(call: Call<List<ResultIntroduce>>, t: Throwable) {
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