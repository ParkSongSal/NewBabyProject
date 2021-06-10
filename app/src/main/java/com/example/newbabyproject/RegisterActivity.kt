package com.example.newbabyproject

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import com.example.newbabyproject.Retrofit2.ResultModel
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : BaseActivity() {

    private var passwordOk = true
    private var babyRelation = "D"

    private var callbackMethod : DatePickerDialog.OnDateSetListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init(applicationContext)

        password2Edit.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                //Toast.makeText(getApplicationContext(), "Focus Lose", Toast.LENGTH_SHORT).show();
                val pass1: String = passwordEdit.text.toString()
                val pass2: String = password2Edit.text.toString()
                if (pass1 == pass2) {
                    passwordOk = true
                    Toast.makeText(applicationContext, "패스워드가 일치합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    passwordOk = false
                    Toast.makeText(applicationContext, "패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        // 아기와의 관계 라디오 버튼 클릭시
        babyRelationRadioGroup.setOnCheckedChangeListener{ radioGroup, i ->
            when(i){
                R.id.dadRadio ->
                    if (dadRadio.isChecked) {
                        babyRelation = "D"
                    }
                R.id.momRadio ->
                    if (momRadio.isChecked) {
                        babyRelation = "M"
                    }
            }

        }

        phoneEdit.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        this.InitializeListener()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.valiBtn -> validateAct()
            R.id.registerBtn -> RegisterAct()
            R.id.cancelBtn -> finish()
        }
    }

    // ID 중복검사
    private fun validateAct(){
        val userId = userIdEdit.text.toString()

        if(userId == ""){
            dlg.setMessage("ID를 입력해주세요.")
                .setNegativeButton("확인", null)
            dlg.show()
            return
        }

        mUserApi.userIdValidate(userId).enqueue(object : Callback<ResultModel> {
            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {

                // 정상결과
                if (response.body()!!.result == "success") {
                    Toast.makeText(
                        this@RegisterActivity,
                        "사용 가능한 ID 입니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    userIdEdit.isEnabled = false
                    validate = true
                    valiBtn.setBackgroundColor(resources.getColor(R.color.mainTextColor))
                    valiBtn.setTextColor(resources.getColor(R.color.whiteColor))
                } else {
                    //중복인 닉네임 존재
                    dlg.setMessage("이미 사용중인 ID 입니다.")
                        .setNegativeButton("확인", null)
                    dlg.show()
                }
            }

            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
                Log.d("TAG", "중복검사 Failed ${t.message}")
                // 네트워크 문제
                Toast.makeText(
                    this@RegisterActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // 회원가입
    private fun RegisterAct(){
        val userId = userIdEdit.text.toString()
        val userPw = passwordEdit.text.toString()
        val userName = nameEdit.text.toString()
        val userPhone = phoneEdit.text.toString()
        val babyName = babyNameEdit.text.toString()
        val babyNum = babyNumEdit.text.toString()
        val regDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")
        var userAuth = "U"
        if("admin" == userId){
            userAuth = "A"
        }

        if(!validate){
            dlg.setMessage("먼저 중복 체크를 해주세요.")
                .setNegativeButton("확인", null)
            dlg.show()
            return
        }

        if (userId == "" || userPw == "" || userName == "" || userPhone == "" || babyName == "" || babyNum == "") {
            dlg.setMessage("빈 칸 없이 입력해주세요.")
                .setNegativeButton("확인", null)
            dlg.show()
            return
        }
        if(!passwordOk){
            dlg.setMessage("입력하신 패스워드가 일치하지 않습니다.")
                .setNegativeButton("확인", null)
            dlg.show()
            return
        }

        mUserApi.userRegister(
            userId,
            userPw,
            userName,
            userPhone,
            babyName,
            babyNum,
            babyRelation,
            regDate,
            userAuth
        ).enqueue(object :
            Callback<ResultModel> {
            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {

                //정상 결과
                if (response.body()!!.result == "success") {
                    Toast.makeText(
                        this@RegisterActivity,
                        "$userId 님 가입을 축하드립니다!",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    //중복인 닉네임 존재
                    dlg.setMessage("가입중 오류가 발생했습니다.\n 다시 시도 부탁드립니다.")
                        .setNegativeButton("확인", null)
                    dlg.show()
                }
            }

            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@RegisterActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

    }

    fun InitializeListener() {
        callbackMethod =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth -> babyBirthDate.setText(year.toString() + "년" + monthOfYear + "월" + dayOfMonth + "일") }
    }

    fun OnClickHandler(view: View) {

        val dialog = DatePickerDialog(this, callbackMethod, 2021, 6, 10)

        dialog.show()
    }
}
