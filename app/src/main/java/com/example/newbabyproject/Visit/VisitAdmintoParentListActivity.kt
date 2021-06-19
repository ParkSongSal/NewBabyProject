package com.example.newbabyproject.Visit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.newbabyproject.BaseActivity
import com.example.newbabyproject.MainActivity
import com.example.newbabyproject.R
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_visit_admin_calendar.*
import kotlinx.android.synthetic.main.activity_visit_adminto_parent_list.*
import kotlinx.android.synthetic.main.item_to_parent.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class VisitAdmintoParentListActivity : BaseActivity() {

    var parentName: String? = ""
    var parentId: String? = ""
    var writeDateAry = ArrayList<ResultVisit>()

    var boardList: MutableList<ResultVisit> = arrayListOf()
    private var mAdapter: AdminToParentDataAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_adminto_parent_list)

        init(this@VisitAdmintoParentListActivity)

        if (intent != null) {
            parentName = intent.getStringExtra("parentName")
            parentId = intent.getStringExtra("parentId")
        } else {
            Common.intentCommon(this@VisitAdmintoParentListActivity, MainActivity::class.java)
            Toast.makeText(applicationContext, "잘못된 경로입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }


        getToParentBoard(parentId)

        fab.setOnClickListener {
            intent =
                Intent(this@VisitAdmintoParentListActivity, VisitAdminWriteActivity::class.java)
            intent.putExtra("parentName", parentName)
            intent.putExtra("parentId", parentId)
            startActivity(intent)
            finish()
        }


    }

    fun getToParentBoard(parentId: String?) {

        val parentIdPart = RequestBody.create(MultipartBody.FORM, parentId)

        val call: Call<List<ResultVisit>> = mVisitApi.toParentList(parentIdPart)
        call.enqueue(object : Callback<List<ResultVisit>> {
            override fun onResponse(
                call: Call<List<ResultVisit>>,
                response: Response<List<ResultVisit>>
            ) {

                Log.d("TAG", "response ${response.body()}")
                //정상 결과
                val result: List<ResultVisit> = response.body()!!
                for (i in result.indices) {
                    val userId: String? = result[i].userId
                    val parentName: String? = result[i].parentName
                    val visitNotice: String? = result[i].visitNotice
                    val babyWeight = result[i].babyWeight
                    val babyLactation = result[i].babyLactation
                    val babyRequireItem = result[i].babyRequireItem
                    val babyEtc = result[i].babyEtc
                    val writeDate: String = result[i].writeDate
                    val boardConfirm = result[i].boardConfirm
                    //val path = result[i].path
                    //val replyCnt = result[i].replyCnt
                    val insertDate = result[i].insertDate
                    val babyName = result[i].babyName

                    val getServerdata = ResultVisit(
                        userId,
                        parentName,
                        visitNotice,
                        babyWeight,
                        babyLactation,
                        babyRequireItem,
                        babyEtc,
                        writeDate,
                        boardConfirm,
                        null,
                        null,
                        insertDate,
                        babyName
                    )
                    boardList.add(getServerdata)
                    mAdapter = AdminToParentDataAdapter(applicationContext, boardList)
                    recycle_view.adapter = mAdapter
                }
            }

            override fun onFailure(call: Call<List<ResultVisit>>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@VisitAdmintoParentListActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}