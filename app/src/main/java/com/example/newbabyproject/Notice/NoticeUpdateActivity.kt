package com.example.newbabyproject.Notice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.newbabyproject.BaseActivity
import com.example.newbabyproject.MainActivity
import com.example.newbabyproject.NoticeListActivity
import com.example.newbabyproject.R
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_notice_update.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeUpdateActivity : BaseActivity() {

    lateinit var call: Call<ResultNotice>

    var resultMsg = ""
    var updateSeq = 0
    var updateTitle : String? = ""
    var updateContent : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_update)

        init(this@NoticeUpdateActivity)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "공지사항 수정"

        if(intent != null){
            //Update
            updateSeq = intent.getIntExtra("updateSeq", 0) //글번호
            Log.d("TAG","updateSeq : $updateSeq")
            updateTitle = intent.getStringExtra("updateTitle")
            updateContent = intent.getStringExtra("updateContent")

            titleTxt.setText(updateTitle)
            contentTxt.setText(updateContent)

        } else {
            Common.intentCommon(this@NoticeUpdateActivity, MainActivity::class.java)
            Toast.makeText(applicationContext, "잘못된 경로입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        // 수정하기 버튼
        updateBtn.setOnClickListener{
            NoticeUpdateAct()
        }
    }

    // 공지사항 등록
    private fun NoticeUpdateAct() {

        updateTitle = titleTxt.text.toString()
        updateContent = contentTxt.text.toString()
        loginId = setting.getString("loginId", "").toString()
        val regDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")

        if (updateTitle == "" || updateContent == "") {
            dlg.setMessage("빈 칸 없이 입력해주세요.")
                .setNegativeButton("확인", null)
            dlg.show()
            return
        }

        val noticeSeqPart = RequestBody.create(MultipartBody.FORM, updateSeq.toString())
        val noticeTitlePart = RequestBody.create(MultipartBody.FORM, updateTitle)
        val noticeContentPart = RequestBody.create(MultipartBody.FORM, updateContent)
        val noticeUpdateIdPart = RequestBody.create(MultipartBody.FORM, loginId)
        val noticeUpdateDatePart = RequestBody.create(MultipartBody.FORM, regDate)

        call = mBoardApi.NoticeUpdate(
            noticeSeqPart,
            noticeTitlePart,
            noticeContentPart,
            noticeUpdateIdPart,
            noticeUpdateDatePart
        )

        resultMsg = "수정되었습니다."

        call.enqueue(object : Callback<ResultNotice> {

            override fun onResponse(call: Call<ResultNotice>, response: Response<ResultNotice>) {

                // 정상결과
                if (response.body()!!.result == "success") {
                    intent = Intent(this@NoticeUpdateActivity, NoticeListActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish()
                    Toast.makeText(
                        this@NoticeUpdateActivity,
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
                    this@NoticeUpdateActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onBackPressed() {
        finish()
    } //뒤로가기 종료버튼

}