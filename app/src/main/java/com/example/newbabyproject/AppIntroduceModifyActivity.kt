package com.example.newbabyproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.newbabyproject.Retrofit2.ResultModel
import com.example.newbabyproject.Retrofit2.RetrofitClient
import com.example.newbabyproject.Retrofit2.boardApi
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_app_introduce_modify.*
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AppIntroduceModifyActivity : BaseActivity() {

    var loginId = ""
    var resultMsg = ""


    // 아이디 저장 기능
    lateinit var setting: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    lateinit var call: Call<ResultModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_introduce_modify)

        // 초기화
        init(applicationContext)

        if (intent != null) {
            boardGubun = intent.getStringExtra("boardGubun")
            actGubun = intent.getStringExtra("actGubun")
        } else {
            Common.intentCommon(this@AppIntroduceModifyActivity, MainActivity::class.java)
            Toast.makeText(applicationContext, "잘못된 경로입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        when (boardGubun) {
            "0" -> {
                board_gubun.text = "앱 소개"
                intent = Intent(this@AppIntroduceModifyActivity, AppIntroduceActivity::class.java)
            }
            "1" -> {
                board_gubun.text = "입원안내문"
                intent = Intent(this@AppIntroduceModifyActivity, EnterIntroduceActivity::class.java)
            }
        }


        setting = getSharedPreferences("setting", MODE_PRIVATE)
        editor = setting.edit()
        editor.apply()


        loginId = setting.getString("loginId", "").toString()

        insertBtn.setOnClickListener(View.OnClickListener {
            introduceInsert()
        })

    }

    private fun introduceInsert() {

        val contentText = contentTxt.text.toString()
        if (contentText == "") {
            dlg.setMessage("내용을 입력해주세요.")
                .setNegativeButton("확인", null)
            dlg.show()
            return
        }

        val date = Common.nowDate("yyyy-MM-dd HH:mm:ss")


        val boardGubunPart = RequestBody.create(MultipartBody.FORM, boardGubun)
        val boardContentPart = RequestBody.create(MultipartBody.FORM, contentText)
        val insertIdPart = RequestBody.create(MultipartBody.FORM, loginId)
        val insertDatePart = RequestBody.create(MultipartBody.FORM, date)
        val updateIdPart = RequestBody.create(MultipartBody.FORM, loginId)
        val updateDatePart = RequestBody.create(MultipartBody.FORM, date)


        when(actGubun){
            "insert" -> {
                call = mBoardApi.getIntroduceInsert(
                    boardGubunPart,
                    boardContentPart,
                    insertIdPart,
                    insertDatePart,
                    updateIdPart,
                    updateDatePart)

                resultMsg = "등록되었습니다."
            }
            "update" ->{
                call = mBoardApi.getIntroduceModify(
                    boardGubunPart,
                    boardContentPart,
                    updateIdPart,
                    updateDatePart)

                resultMsg = "수정되었습니다."
            }
        }


        call.enqueue(object : Callback<ResultModel> {
            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {

                // 정상결과
                if (response.body()!!.result == "success") {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish()
                    Toast.makeText(
                        this@AppIntroduceModifyActivity,
                        resultMsg,
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    //중복인 닉네임 존재
                    dlg.setMessage("다시 시도 바랍니다.")
                        .setNegativeButton("확인", null)
                    dlg.show()
                }
            }

            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@AppIntroduceModifyActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


}
