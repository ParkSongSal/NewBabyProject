package com.example.newbabyproject.Visit

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.denzcoskun.imageslider.models.SlideModel
import com.example.newbabyproject.*
import com.example.newbabyproject.Notice.ResultNotice
import com.example.newbabyproject.utils.Common
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_visit_admin_to_parent_detail.*
import kotlinx.android.synthetic.main.item_toolbar.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class VisitAdminToParentDetailActivity : BaseActivity() {

    lateinit var call: Call<ResultVisit>

    lateinit var replyCall: Call<ResultReply>

    var slideModels : ArrayList<SlideModel> = ArrayList<SlideModel>()
    var pathList = ArrayList<String>()

    private var count = 0
    var seq = -1
    var babyName : String? = null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_admin_to_parent_detail)

        /*toolbar.setTitleTextColor(getColor(R.color.whiteColor))
        toolbar.title = "면회내용 상세보기"
        setSupportActionBar(toolbar)*/

        init(this@VisitAdminToParentDetailActivity)

        loginId = setting.getString("loginId", "").toString()


        try {
            val intent : Intent = intent
            if(intent.hasExtra("resultVisit")){
                val resultVisit = intent.getSerializableExtra("resultVisit") as ResultVisit
                Log.d("TAG", "Detail Activity writeDateArray : ${resultVisit.toString()}")
                seq = resultVisit.seq
                babyName = resultVisit.babyName
                babyNameTxt.text = resultVisit.babyName + "아기 면회소식"
                writeNameTxt.text = "관리자"
                writeDateTxt.text = Common.dataSplitFormat(resultVisit.writeDate, "date")
                visitNoticeTxt.text = resultVisit.visitNotice
                babyWeightTxt.text = resultVisit.babyWeight
                babyLactationTxt.text = resultVisit.babyLactation
                babyRequireItemTxt.text = resultVisit.babyRequireItem
                babyEtcTxt.text = resultVisit.babyEtc
                Log.d("TAG", "Detail Activity Exceptilon ")

                pathList = intent.getSerializableExtra("pathList") as ArrayList<String>
                Log.d("TAG", "Detail Activity Exceptilon $pathList")


                for (j in pathList.indices){
                    if("null" == pathList[j] || "" == pathList[j]){
                        pathList[j] = "android.resource://$packageName/drawable/deleteiconblack2"
                    }else{
                        continue
                    }
                }

                for (i in pathList.indices) {
                    if(pathList[i].isNotEmpty()){
                        if(pathList[i].contains("deleteiconblack2")) {
                            continue
                        }else{
                            count++
                            slideModels.add(SlideModel(pathList[i]))
                            //slideModels.add(SlideModel(pathList[i],resultVisit.babyName))
                            //setImage(pathList[i], i)
                        }
                    }else{
                        continue
                    }
                }


            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", "Detail Activity Exceptilon ${e.toString()}")
        }

        img_slider.setImageList(slideModels, true)

        setSupportActionBar(toolbar2)
        supportActionBar?.title = ""

        app_bar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                when {
                    verticalOffset == 0 -> { //  이미지가 보일때
                        supportActionBar?.title = ""
                        toolbar_layout.title = ""
                    }
                    Math.abs(verticalOffset) >= app_bar.totalScrollRange -> { // 이미지 안보이고 툴바만 보일떄
                        supportActionBar?.title = "면회소식 상세"
                    }
                    Math.abs(verticalOffset) <= app_bar.totalScrollRange -> {// 중간
                        toolbar_layout.title = ""
                        supportActionBar?.subtitle = ""
                    }
                    else -> {
                        supportActionBar?.title = ""
                        toolbar_layout.title = ""
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.btn_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.delBtn -> {
                dlg.setTitle("삭제 알림")
                    .setMessage("게시물을 삭제 하시겠습니까?")
                    .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                        deleteToParentBoard()
                    })
                    .setNegativeButton("아니오", null)
                dlg.show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /* 댓글달기 */
    fun replyInsert(){

        val boardSeq = seq.toString()
        val userId = loginId
        val replyContent = ReplyTxt.text.toString()
        val insertDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")
        if (replyContent == "" || replyContent.isEmpty()) {

            dlg.setMessage("빈칸은 등록할 수 없습니다.")
                .setNegativeButton("확인", null)
                .create()
            dlg.show()

            return
        }


        val replyBoardSeqPart = RequestBody.create(MultipartBody.FORM, boardSeq)
        val replyUserIdPart = RequestBody.create(MultipartBody.FORM, userId)
        val replyContentPart = RequestBody.create(MultipartBody.FORM, replyContent)
        val replyInsertDatePart = RequestBody.create(MultipartBody.FORM, insertDate)

        replyCall = mVisitApi.replyInsert(
            replyBoardSeqPart,
            replyUserIdPart,
            replyContentPart,
            replyInsertDatePart
        )
        replyCall.enqueue(object : Callback<ResultReply> {

            override fun onResponse(call: Call<ResultReply>, response: Response<ResultReply>) {

                // 정상결과
                if (response.body()!!.result == "success") {
                    Toast.makeText(
                        this@VisitAdminToParentDetailActivity,
                        "등록되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //replyList.clear()
                    ReplyTxt.text.clear()
                    //getReplyList()
                } else {
                    dlg.setMessage("다시 시도 바랍니다.")
                        .setNegativeButton("확인", null)
                    dlg.show()
                }
            }

            override fun onFailure(call: Call<ResultReply>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@VisitAdminToParentDetailActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }
    /* 면회소식 삭제 */
    fun deleteToParentBoard(){
        val boardSeqPart = RequestBody.create(MultipartBody.FORM, seq.toString())
        call = mVisitApi.admin_to_parent_board_delete(boardSeqPart)

        call.enqueue(object : Callback<ResultVisit> {

            override fun onResponse(call: Call<ResultVisit>, response: Response<ResultVisit>) {

                // 정상결과
                if (response.body()!!.result == "success") {
                    intent = Intent(
                        this@VisitAdminToParentDetailActivity,
                        VisitAdminUserSelActivity::class.java
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish()
                    Toast.makeText(
                        this@VisitAdminToParentDetailActivity,
                        "삭제되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    //중복인 닉네임 존재
                    dlg.setMessage("다시 시도 바랍니다.")
                        .setNegativeButton("확인", null)
                    dlg.show()
                }
            }

            override fun onFailure(call: Call<ResultVisit>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@VisitAdminToParentDetailActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun onClick(view: View) {
        when (view.id) {

            /* 댓글등록 버튼 */
            R.id.replyBtn -> {
                replyInsert()
            }

        }
    }

}