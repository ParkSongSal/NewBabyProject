package com.example.newbabyproject.Visit

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.models.SlideModel
import com.example.newbabyproject.BaseActivity
import com.example.newbabyproject.R
import com.example.newbabyproject.utils.Common
import com.example.newbabyproject.utils.Common.dataSplitFormat
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_visit_admin_to_parent_detail.*
import kotlinx.android.synthetic.main.activity_visit_user_detail.*
import kotlinx.android.synthetic.main.activity_visit_user_detail.babyEtcTxt
import kotlinx.android.synthetic.main.activity_visit_user_detail.babyLactationTxt
import kotlinx.android.synthetic.main.activity_visit_user_detail.babyNameTxt
import kotlinx.android.synthetic.main.activity_visit_user_detail.babyRequireItemTxt
import kotlinx.android.synthetic.main.activity_visit_user_detail.babyWeightTxt
import kotlinx.android.synthetic.main.activity_visit_user_detail.img_slider
import kotlinx.android.synthetic.main.activity_visit_user_detail.visitNoticeTxt
import kotlinx.android.synthetic.main.activity_visit_user_detail.writeDateTxt
import kotlinx.android.synthetic.main.activity_visit_user_detail.writeNameTxt
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class VisitUserDetailActivity : BaseActivity() {

    var writeDateAry: ArrayList<String>? = ArrayList<String>()
    lateinit var call: Call<ResultVisit>

    var slideModels : ArrayList<SlideModel> = ArrayList<SlideModel>()
    var pathList = ArrayList<String>()
    private var count = 0
    var babyName : String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_user_detail)

        init(this@VisitUserDetailActivity)

        try {
            val intent : Intent = intent
            if(intent.hasExtra("resultVisit")){
                val resultVisit = intent.getSerializableExtra("resultVisit") as ResultVisit
                Log.d("TAG", "Detail Activity writeDateArray : ${resultVisit.toString()}")
                if("N" == resultVisit.boardConfirm){
                    boardConfirmAct(resultVisit.parentId, resultVisit.seq)
                }
                babyName = resultVisit.babyName

                babyNameTxt.text = resultVisit.babyName + "아기 면회소식"
                writeNameTxt.text = "관리자"
                writeDateTxt.text = dataSplitFormat(resultVisit.writeDate,"date")
                visitNoticeTxt.text = resultVisit.visitNotice
                babyWeightTxt.text = resultVisit.babyWeight
                babyLactationTxt.text = resultVisit.babyLactation
                babyRequireItemTxt.text = resultVisit.babyRequireItem
                babyEtcTxt.text = resultVisit.babyEtc
                Log.d("TAG", "Detail Activity Exceptilon ")
                pathList = intent.getSerializableExtra("pathList") as ArrayList<String>

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
        setSupportActionBar(toolbar3)
        supportActionBar?.title = ""

        app_bar2.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                when {
                    verticalOffset == 0 -> { //  이미지가 보일때
                        supportActionBar?.title = ""
                        toolbar_layout2.title = ""
                    }
                    Math.abs(verticalOffset) >= app_bar2.totalScrollRange -> { // 이미지 안보이고 툴바만 보일떄
                        supportActionBar?.title = "$babyName 면회소식"

                    }
                    Math.abs(verticalOffset) <= app_bar2.totalScrollRange -> {// 중간
                        toolbar_layout2.title = ""
                        supportActionBar?.subtitle=""
                    }
                    else -> {
                        supportActionBar?.title = ""
                        toolbar_layout2.title = ""
                    }
                }
            }
        })

    }

    private fun boardConfirmAct(userId : String?, seq : Int){
        if(userId != null){
            val parentId = userId
            val boardConfirmYn = "Y"
            val boardConfirmDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")

            val seqPart = RequestBody.create(MultipartBody.FORM, seq.toString())
            val parentIdPart = RequestBody.create(MultipartBody.FORM, parentId)
            val boardConfirmYnPart = RequestBody.create(MultipartBody.FORM, boardConfirmYn)
            val boardConfirmDatePart = RequestBody.create(MultipartBody.FORM, boardConfirmDate)

            call = mVisitApi.to_parent_board_confirm_update(
                seqPart,
                parentIdPart,
                boardConfirmYnPart,
                boardConfirmDatePart
            )

            call.enqueue(object : Callback<ResultVisit> {
                override fun onResponse(call: Call<ResultVisit>, response: Response<ResultVisit>) {
                    // 정상결과
                    if (response.body()!!.result == "success") {

                        Toast.makeText(
                            this@VisitUserDetailActivity,
                            "면회 소식을 확인하였습니다.",
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
                        this@VisitUserDetailActivity,
                        "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }else{
            Log.d("TAG", "userId Null...")
        }
    }
}