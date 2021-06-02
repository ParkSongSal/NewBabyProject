package com.example.newbabyproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.newbabyproject.Notice.NoticeDataAdapter
import com.example.newbabyproject.Notice.ResultNotice
import kotlinx.android.synthetic.main.activity_notice_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeListActivity : BaseActivity() {

    var  boardList: MutableList<ResultNotice> = arrayListOf()

    private var mAdapter: NoticeDataAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_list)

        init(this@NoticeListActivity)


        fab.setOnClickListener(View.OnClickListener {
            intent = Intent(applicationContext, NoticeInsertActivity::class.java)
            startActivity(intent)
            finish()
        })

        getServerData()
    }


    //게시판 리스트 조회
    private fun getServerData() {
        val call: Call<List<ResultNotice>> = mBoardApi.getNoticeList()
        call.enqueue(object : Callback<List<ResultNotice>> {
            override fun onResponse(
                call: Call<List<ResultNotice>>,
                response: Response<List<ResultNotice>>
            ) {

                Log.d("TAG","response ${response.body()}")
                //정상 결과
                val result: List<ResultNotice> = response.body()!!
                for (i in result.indices) {
                    val SEQ: Int = result[i].seq
                    val TITLE: String = result[i].title
                    val CONTENT: String = result[i].content
                    val INSERT_ID: String = result[i].insertId
                    val INSERT_DATE: String = result[i].insertDate
                    val UPDATE_ID: String = result[i].updateId
                    val UPDATE_DATE: String = result[i].updateDate
                    val getServerdata = ResultNotice(SEQ, TITLE, CONTENT, INSERT_ID, INSERT_DATE, UPDATE_ID, UPDATE_DATE)
                    boardList.add(getServerdata)
                    //saveList.add(getServerdata)
                    mAdapter = NoticeDataAdapter(applicationContext, boardList)
                    recycle_view.adapter = mAdapter
                }
            }

            override fun onFailure(call: Call<List<ResultNotice>>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@NoticeListActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}