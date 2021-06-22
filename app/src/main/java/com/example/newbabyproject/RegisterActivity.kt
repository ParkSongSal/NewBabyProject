package com.example.newbabyproject

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.newbabyproject.Retrofit2.ResultModel
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.item_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class RegisterActivity : BaseActivity() {

    private var passwordOk = true
    private var babyRelation = "D"



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        toolbar.setTitleTextColor(getColor(R.color.whiteColor))
        toolbar.title = "회원가입"
        setSupportActionBar(toolbar)

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
        babyRelationRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
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


        babyBirthDate.onFocusChangeListener =
            OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    //  .. 포커스시
                    val dialog = DatePickerDialog(this, dateCallbackMethod, 2021, 6, 10)

                    dialog.show()
                } else {
                    //  .. 포커스 뺏겼을 때
                }
            }

        babyBirthTime.onFocusChangeListener =
            OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    //  .. 포커스시
                    val dialog = TimePickerDialog(
                        this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        timeCallbackMethod,
                        8,
                        10,
                        false
                    )

                    dialog.show()
                } else {
                    //  .. 포커스 뺏겼을 때
                }
            }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.valiBtn -> validateAct()
            R.id.registerBtn -> RegisterAct()
            R.id.cancelBtn -> finish()
        }
    }

    // ID 중복검사
    private fun validateAct() {
        val userId = userIdEdit.text.toString()

        if (userId == "") {
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
    private fun RegisterAct() {
        val userId = userIdEdit.text.toString()
        val userPw = passwordEdit.text.toString()
        val userName = nameEdit.text.toString()
        val userPhone = phoneEdit.text.toString()
        val babyName = babyNameEdit.text.toString()
        val babyNum = babyNumEdit.text.toString()
        val babyBirthDate = babyBirthDate.text.toString()
        val babyBirthTime = babyBirthTime.text.toString()
        val regDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")
        var userAuth = "U"
        if ("admin" == userId) {
            userAuth = "A"
        }

        if (!validate) {
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
        if (!passwordOk) {
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
            babyBirthDate,
            babyBirthTime,
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
        dateCallbackMethod = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            val month = when (monthOfYear+1) {
                1 -> "01"
                2 -> "02"
                3 -> "03"
                4 -> "04"
                5 -> "05"
                6 -> "06"
                7 -> "07"
                8 -> "08"
                9 -> "09"
                else -> monthOfYear.toString()
            }
            val day = when (dayOfMonth) {
                1 -> "01"
                2 -> "02"
                3 -> "03"
                4 -> "04"
                5 -> "05"
                6 -> "06"
                7 -> "07"
                8 -> "08"
                9 -> "09"
                else -> dayOfMonth.toString()
            }

            babyBirthDate.setText(
                "$year-$month-$day"
            )
        }

        timeCallbackMethod =
            OnTimeSetListener { view, hourOfDay, minute ->
                val hour = when (hourOfDay) {
                    1 -> "01"
                    2 -> "02"
                    3 -> "03"
                    4 -> "04"
                    5 -> "05"
                    6 -> "06"
                    7 -> "07"
                    8 -> "08"
                    9 -> "09"
                    else -> hourOfDay.toString()
                }
                val min = when (minute) {
                    1 -> "01"
                    2 -> "02"
                    3 -> "03"
                    4 -> "04"
                    5 -> "05"
                    6 -> "06"
                    7 -> "07"
                    8 -> "08"
                    9 -> "09"
                    else -> minute.toString()
                }
                babyBirthTime.setText("$hour:$min")
            }
    }
}
