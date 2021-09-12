package com.example.newbabyproject

import android.app.DatePickerDialog

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.newbabyproject.Retrofit2.ResultModel
import com.example.newbabyproject.Visit.ResultVisit
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_user_info_update.*
import kotlinx.android.synthetic.main.activity_user_info_update.babyBirthDate
import kotlinx.android.synthetic.main.activity_user_info_update.babyBirthTime
import kotlinx.android.synthetic.main.activity_user_info_update.babyNameEdit
import kotlinx.android.synthetic.main.activity_user_info_update.babyNumEdit
import kotlinx.android.synthetic.main.activity_user_info_update.nameEdit
import kotlinx.android.synthetic.main.activity_user_info_update.passwordEdit
import kotlinx.android.synthetic.main.activity_user_info_update.password2Edit
import kotlinx.android.synthetic.main.activity_user_info_update.phoneEdit
import kotlinx.android.synthetic.main.activity_user_info_update.userIdEdit
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoUpdateActivity : BaseActivity() {

    private var passwordOk = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info_update)

        init(this@UserInfoUpdateActivity)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "회원정보수정"

        loginId = setting.getString("loginId", "").toString()

        getBabyInfo(loginId)

        phoneEdit.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        this.InitializeListener()


        password2Edit.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
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

        babyBirthDate.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    //  .. 포커스시
                    val dialog = DatePickerDialog(this, dateCallbackMethod, 2021, 6, 10)

                    dialog.show()
                } else {
                    //  .. 포커스 뺏겼을 때
                }
            }

        babyBirthTime.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
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
            R.id.updateBtn -> updateAct()
            R.id.cancelBtn -> finish()
        }


    }

    fun getBabyInfo(loginId: String) {

            userIdEdit.setText(loginId)
            userIdEdit.isEnabled = false
            val loginIdPart = RequestBody.create(MultipartBody.FORM, loginId)

            mUserApi.getUserInfo(loginIdPart).enqueue(object :
                Callback<List<ResultVisit>> {
                override fun onResponse(
                    call: Call<List<ResultVisit>>,
                    response: Response<List<ResultVisit>>
                ) {

                    //정상 결과
                    val result: List<ResultVisit>? = response.body()

                    Log.d("TAG", "list : $result")
                    for (i in result!!.indices) {
                        nameEdit.setText(result[i].userName)
                        phoneEdit.setText(result[i].userPhone)
                        babyNameEdit.setText(result[i].babyName)
                        babyNumEdit.setText(result[i].babyNum)
                        babyBirthDate.setText(result[i].babyBirthDate)
                        babyBirthTime.setText(result[i].babyBirthTime)

                    }
                }

                override fun onFailure(call: Call<List<ResultVisit>>, t: Throwable) {
                    // 네트워크 문제
                    Toast.makeText(
                        coxt,
                        "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun updateAct(){
        val userId = userIdEdit.text.toString()
        val userPw = passwordEdit.text.toString()
        val userName = nameEdit.text.toString()
        val userPhone = phoneEdit.text.toString()
        val babyName = babyNameEdit.text.toString()
        val babyNum = babyNumEdit.text.toString()
        val babyBirthDate = babyBirthDate.text.toString()
        val babyBirthTime = babyBirthTime.text.toString()
        val regDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")

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

        mUserApi.userUpdate(
            userId,
            userPw,
            userName,
            userPhone,
            babyName,
            babyNum,
            babyBirthDate,
            babyBirthTime,
            regDate
        ).enqueue(object :
            Callback<ResultModel> {
            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {

                //정상 결과
                if (response.body()!!.result == "success") {
                    Toast.makeText(
                        this@UserInfoUpdateActivity,
                        "$userId 님 회원정보가 수정되었습니다.",
                        Toast.LENGTH_LONG
                    ).show()
                    Common.intentCommon(this@UserInfoUpdateActivity, MainActivity::class.java)
                    finish()
                } else {
                    //중복인 닉네임 존재
                    dlg.setMessage("수정중 오류가 발생했습니다.\n 다시 시도 부탁드립니다.")
                        .setNegativeButton("확인", null)
                    dlg.show()
                }
            }

            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@UserInfoUpdateActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })


    }



    fun InitializeListener() {
        dateCallbackMethod =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                val month = when (monthOfYear + 1) {
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