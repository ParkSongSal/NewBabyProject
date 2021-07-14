package com.example.newbabyproject.Visit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.newbabyproject.*
import com.example.newbabyproject.Notice.NoticeDataAdapter
import com.example.newbabyproject.Notice.NoticeDetailActivity
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_visit_admin_calendar.*
import kotlinx.android.synthetic.main.activity_visit_adminto_parent_list.*
import kotlinx.android.synthetic.main.item_to_parent.*
import kotlinx.android.synthetic.main.item_toolbar.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.*

class VisitAdmintoParentListActivity : BaseActivity() {

    var parentName: String? = ""
    var parentId: String? = ""
    var writeDateAry = ArrayList<ResultVisit>()

    var boardList: MutableList<ResultVisit> = arrayListOf()
    private var mAdapter: AdminToParentDataAdapter? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_adminto_parent_list)

        toolbar.setTitleTextColor(getColor(R.color.whiteColor))

        init(this@VisitAdmintoParentListActivity)

        if (intent != null) {
            parentName = intent.getStringExtra("parentName")
            parentId = intent.getStringExtra("parentId")
            toolbar.title = "$parentName 보호자님"
        } else {
            Common.intentCommon(this@VisitAdmintoParentListActivity, MainActivity::class.java)
            Toast.makeText(applicationContext, "잘못된 경로입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
        setSupportActionBar(toolbar)

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

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    // 보낸이 : NoticeDataAdapter
    @SuppressLint("RestrictedApi")
    @Subscribe
    fun onItemClick(event: AdminToParentDataAdapter.ItemClickEvent) {

        val resultVisit : ResultVisit = boardList[event.position]

        val intent = Intent(applicationContext, VisitAdminToParentDetailActivity::class.java)

        var list =  ArrayList<String>()

        list.add(boardList[event.position].path1.toString())
        list.add(boardList[event.position].path2.toString())
        list.add(boardList[event.position].path3.toString())
        intent.putExtra("pathList", list)

        intent.putExtra("resultVisit", resultVisit as Serializable)

        startActivity(intent)
        finish()
    }


    fun getToParentBoard(parentId: String?) {

        val parentIdPart = RequestBody.create(MultipartBody.FORM, parentId)

        val call: Call<List<ResultVisit>> = mVisitApi.toParentListAdmin(parentIdPart)
        call.enqueue(object : Callback<List<ResultVisit>> {
            override fun onResponse(
                call: Call<List<ResultVisit>>,
                response: Response<List<ResultVisit>>
            ) {



                //정상 결과
                val result: List<ResultVisit> = response.body()!!
                Log.d("TAG", "response ${response.body()}")
                Log.d("TAG", "response " + result.size)
                if(result.isNotEmpty()){
                    for (i in result.indices) {
                        val seq : Int = result[i].seq
                        val userId: String? = result[i].userId
                        val parentName: String? = result[i].parentName
                        val visitNotice: String? = result[i].visitNotice
                        val babyWeight = result[i].babyWeight
                        val babyLactation = result[i].babyLactation
                        val babyRequireItem = result[i].babyRequireItem
                        val babyEtc = result[i].babyEtc
                        val writeDate: String = result[i].writeDate
                        val boardConfirm = result[i].boardConfirm
                        val tempYn = result[i].tempYn
                        val reserveDate = result[i].reserveDate
                        val path1 = result[i].path1
                        val path2 = result[i].path2
                        val path3 = result[i].path3
                        //val replyCnt = result[i].replyCnt
                        val insertDate = result[i].insertDate
                        val babyName = result[i].babyName

                        val getServerdata = ResultVisit(
                            seq,
                            userId,
                            parentName,
                            visitNotice,
                            babyWeight,
                            babyLactation,
                            babyRequireItem,
                            babyEtc,
                            writeDate,
                            boardConfirm,
                            tempYn,
                            reserveDate,
                            path1,
                            path2,
                            path3,
                            //null,
                            insertDate,
                            babyName
                        )
                        boardList.add(getServerdata)
                        Log.d("TAG", "boardList ${boardList.toString()}")

                        mAdapter = AdminToParentDataAdapter(applicationContext, boardList)
                        recycle_view.adapter = mAdapter
                    }

                }else{
                    recycle_view.visibility = View.GONE
                    noDataLl.visibility = View.VISIBLE

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

    override fun onBackPressed() {
        startActivity(Intent(this@VisitAdmintoParentListActivity, VisitAdminUserSelActivity::class.java))
        finish()
    } //뒤로가기 종료버튼
}