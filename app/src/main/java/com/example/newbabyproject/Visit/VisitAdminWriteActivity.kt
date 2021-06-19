package com.example.newbabyproject.Visit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.newbabyproject.BaseActivity
import com.example.newbabyproject.MainActivity
import com.example.newbabyproject.R
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_app_introduce_modify.*
import kotlinx.android.synthetic.main.activity_app_introduce_modify.contentTxt
import kotlinx.android.synthetic.main.activity_app_introduce_modify.insertBtn
import kotlinx.android.synthetic.main.activity_visit_admin_write.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisitAdminWriteActivity : BaseActivity() {

    lateinit var call: Call<ResultVisit>

    var parentName = ""
    var parentId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_admin_write)


        init(this@VisitAdminWriteActivity)

        if(intent != null){
            parentName = intent.getStringExtra("parentName")
            parentId = intent.getStringExtra("parentId")
        }else{
            Common.intentCommon(this@VisitAdminWriteActivity, MainActivity::class.java)
            Toast.makeText(applicationContext, "잘못된 경로입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        loginId = setting.getString("loginId", "").toString()

        insertBtn.setOnClickListener(View.OnClickListener {
            adminWriteAct()
        })

    }

    private fun adminWriteAct() {


        val visitNotice = contentTxt.text.toString()
        val babyWeight = babyWeightTxt.text.toString()
        val babyLactation = babyLactationTxt.text.toString()
        val babyRequireItem = babyRequireItemTxt.text.toString()
        val babyEtc = babyEtcTxt.text.toString()
        val writeDate = Common.nowDate("yyyy-MM-dd")
        val date = Common.nowDate("yyyy-MM-dd HH:mm:ss")

        val parentIdPart = RequestBody.create(MultipartBody.FORM, parentId)
        val parentNamePart = RequestBody.create(MultipartBody.FORM, parentName)
        val visitNoticePart = RequestBody.create(MultipartBody.FORM, visitNotice)
        val babyWeightPart = RequestBody.create(MultipartBody.FORM, babyWeight)
        val babyLactationPart = RequestBody.create(MultipartBody.FORM, babyLactation)
        val babyRequireItemPart = RequestBody.create(MultipartBody.FORM, babyRequireItem)
        val babyEtcPart = RequestBody.create(MultipartBody.FORM, babyEtc)
        val writeDatePart = RequestBody.create(MultipartBody.FORM, writeDate)
        val insertIdPart = RequestBody.create(MultipartBody.FORM, loginId)
        val insertDatePart = RequestBody.create(MultipartBody.FORM, date)
        val updateIdPart = RequestBody.create(MultipartBody.FORM, loginId)
        val updateDatePart = RequestBody.create(MultipartBody.FORM, date)

        call = mVisitApi.toParentInsert(
            parentIdPart,
            parentNamePart,
            visitNoticePart,
            babyWeightPart,
            babyLactationPart,
            babyRequireItemPart,
            babyEtcPart,
            writeDatePart,
            insertIdPart,
            insertDatePart,
            updateIdPart,
            updateDatePart
        )

        call.enqueue(object : Callback<ResultVisit> {
            override fun onResponse(call: Call<ResultVisit>, response: Response<ResultVisit>) {

                // 정상결과
                if (response.body()!!.result == "success") {
                    intent = Intent(this@VisitAdminWriteActivity, VisitAdmintoParentListActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish()
                    Toast.makeText(
                        this@VisitAdminWriteActivity,
                        "등록되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    dlg.setMessage("다시 시도 바랍니다.")
                        .setNegativeButton("확인", null)
                    dlg.show()
                }
            }

            override fun onFailure(call: Call<ResultVisit>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@VisitAdminWriteActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}