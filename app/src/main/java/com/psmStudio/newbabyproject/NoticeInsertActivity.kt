package com.psmStudio.newbabyproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.psmStudio.newbabyproject.Notice.ResultNotice
import com.psmStudio.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_notice_insert.*
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeInsertActivity : BaseActivity() {

    lateinit var call: Call<ResultNotice>

    var resultMsg = ""
    var noticeTitle = ""
    var noticeContent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_insert)

        init(this@NoticeInsertActivity)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "공지사항 등록"

        insertBtn.setOnClickListener {
            NoticeWriteAct()
        }
    }

    // 공지사항 등록
    private fun NoticeWriteAct() {

        noticeTitle = titleTxt.text.toString()
        noticeContent = contentTxt.text.toString()
        loginId = setting.getString("loginId", "").toString()
        val regDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")

        if (noticeTitle == "" || noticeContent == "") {
            dlg.setMessage("빈 칸 없이 입력해주세요.")
                .setNegativeButton("확인", null)
            dlg.show()
            return
        }

        val noticeTitlePart = RequestBody.create(MultipartBody.FORM, noticeTitle)
        val noticeContentPart = RequestBody.create(MultipartBody.FORM, noticeContent)
        val noticeInsertIdPart = RequestBody.create(MultipartBody.FORM, loginId)
        val noticeInsertDatePart = RequestBody.create(MultipartBody.FORM, regDate)
        val noticeUpdateIdPart = RequestBody.create(MultipartBody.FORM, loginId)
        val noticeUpdateDatePart = RequestBody.create(MultipartBody.FORM, regDate)

        call = mBoardApi.NoticeInsert(
            noticeTitlePart,
            noticeContentPart,
            noticeInsertIdPart,
            noticeInsertDatePart,
            noticeUpdateIdPart,
            noticeUpdateDatePart
        )

        resultMsg = "등록되었습니다."

        call.enqueue(object : Callback<ResultNotice> {

            override fun onResponse(call: Call<ResultNotice>, response: Response<ResultNotice>) {

                // 정상결과
                if (response.body()!!.result == "success") {
                    intent = Intent(this@NoticeInsertActivity, NoticeListActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish()
                    Toast.makeText(
                        this@NoticeInsertActivity,
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

            override fun onFailure(call: Call<ResultNotice>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@NoticeInsertActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@NoticeInsertActivity, NoticeListActivity::class.java))
        finish()
    } //뒤로가기 종료버튼

}