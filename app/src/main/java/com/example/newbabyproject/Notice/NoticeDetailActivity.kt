package com.example.newbabyproject.Notice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.newbabyproject.*
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_notice_detail.*
import kotlinx.android.synthetic.main.activity_notice_list.*

class NoticeDetailActivity : BaseActivity() {

    var seq = 0
    var title : String?  = ""
    var writer : String? = ""
    var content : String? = ""
    var insertDate : String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_detail)

        init(this@NoticeDetailActivity)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "공지사항 상세"

        loginId = setting.getString("loginId", "").toString()

        if (intent != null) {
            seq = intent.getIntExtra("Seq", 0) //글번호
            title = intent.getStringExtra("TITLE")
            writer = intent.getStringExtra("WRITER")
            content = intent.getStringExtra("CONTENT")
            insertDate = intent.getStringExtra("DATE")

            titleTxt.text = title.toString()
            userTxt.text = writer.toString()
            contentTxt.text = content.toString()
            dateTxt.text = insertDate.toString()

            // 관리자만 수정, 삭제 가능
            if (loginId == "admin") {
                btnLl.visibility = View.VISIBLE
                updateBtn.setOnClickListener{
                    intent = Intent(this@NoticeDetailActivity, NoticeUpdateActivity::class.java)
                    intent.putExtra("updateSeq",seq)
                    intent.putExtra("updateTitle",title)
                    intent.putExtra("updateContent",content)
                    startActivity(intent)
                }
            } else {
                btnLl.visibility = View.GONE
            }

        } else {
            Common.intentCommon(this@NoticeDetailActivity, MainActivity::class.java)
            Toast.makeText(applicationContext, "잘못된 경로입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this@NoticeDetailActivity, NoticeListActivity::class.java))
        finish()
    } //뒤로가기 종료버튼
}