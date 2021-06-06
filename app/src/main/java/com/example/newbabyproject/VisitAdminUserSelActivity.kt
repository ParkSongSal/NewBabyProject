package com.example.newbabyproject

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.newbabyproject.Notice.NoticeDataAdapter
import com.example.newbabyproject.Notice.NoticeDetailActivity
import com.example.newbabyproject.Notice.ResultNotice
import com.example.newbabyproject.Visit.ResultVisit
import com.example.newbabyproject.Visit.VisitUserDataAdapter
import kotlinx.android.synthetic.main.activity_notice_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisitAdminUserSelActivity : BaseActivity() {

    var  userList: MutableList<ResultVisit> = arrayListOf()
    private var mAdapter: VisitUserDataAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_admin_user_sel)

        init(this@VisitAdminUserSelActivity)

        loginId = setting.getString("loginId", "").toString()


        getUserList()
    }

    //게시판 리스트 조회
    private fun getUserList() {
        val call: Call<List<ResultVisit>> = mVisitApi.VisitAdminUserList()
        call.enqueue(object : Callback<List<ResultVisit>> {
            override fun onResponse(
                call: Call<List<ResultVisit>>,
                response: Response<List<ResultVisit>>
            ) {

                Log.d("TAG", "response ${response.body()}")
                //정상 결과
                val result: List<ResultVisit> = response.body()!!
                for (i in result.indices) {
                    val USER_ID: String = result[i].userId
                    val USER_NAME: String = result[i].userName
                    val USER_PHONE: String = result[i].userPhone
                    val BABY_NAME: String = result[i].babyName
                    val BABY_NUM: String = result[i].babyNum
                    val BABY_RELATION: String = result[i].babyRelation
                    val getServerdata = ResultVisit(
                        USER_ID,
                        USER_NAME,
                        USER_PHONE,
                        BABY_NAME,
                        BABY_NUM,
                        BABY_RELATION
                    )
                    userList.add(getServerdata)
                    //saveList.add(getServerdata)
                    mAdapter = VisitUserDataAdapter(applicationContext, userList)
                    recycle_view.adapter = mAdapter
                }
            }

            override fun onFailure(call: Call<List<ResultVisit>>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@VisitAdminUserSelActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
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
    fun onItemClick(event: VisitUserDataAdapter.ItemClickEvent) {


        val parentName = userList[event.position].userName

        dlg.setTitle("보호자 선택 알림")
            .setMessage("선택하신 보호자($parentName)에게 작성한 게시글을 확인하시겠습니까?")
            .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(applicationContext, "이동합니다.", Toast.LENGTH_SHORT).show()
            })
            .setNegativeButton("아니오", null)
        dlg.show()

    }

}