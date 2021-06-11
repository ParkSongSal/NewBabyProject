package com.example.newbabyproject.Visit

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fastcampusandroid.Calendar.EventDecorator
import com.example.fastcampusandroid.Calendar.SaturdayDecorator
import com.example.fastcampusandroid.Calendar.SundayDecorator
import com.example.newbabyproject.BaseActivity
import com.example.newbabyproject.MainActivity
import com.example.newbabyproject.Notice.NoticeDataAdapter
import com.example.newbabyproject.Notice.ResultNotice
import com.example.newbabyproject.NoticeInsertActivity
import com.example.newbabyproject.R
import com.example.newbabyproject.utils.Common
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_notice_list.*
import kotlinx.android.synthetic.main.activity_visit_admin_calendar.*
import kotlinx.android.synthetic.main.activity_visit_admin_calendar.fab
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class VisitAdminCalendarActivity : BaseActivity() {

    var parentName : String? = ""
    var parentId : String? = ""
    var writeDateAry = ArrayList<ResultVisit>()
    val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_admin_calendar)

        init(this@VisitAdminCalendarActivity)

        if(intent != null){
            parentName = intent.getStringExtra("parentName")
            parentId = intent.getStringExtra("parentId")
        }else{
            Common.intentCommon(this@VisitAdminCalendarActivity, MainActivity::class.java)
            Toast.makeText(applicationContext, "잘못된 경로입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        getToParentBoard(parentId)


        calendarView.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(2017, 0, 1))   //달력의 시작
            .setMaximumDate(CalendarDay.from(2030, 11, 31))   //달력의 끝
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()

        calendarView.selectedDate = CalendarDay.today()

        calendarView.addDecorators(
            SundayDecorator(),
            SaturdayDecorator()
        )

        //val result = arrayOf("2021,03,18", "2021,04,18","2021,04,19","2021,05,26","2021,05,27", "2021,05,18", "2021,11,18")




        calendarView.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            val Year = date.year
            val Month = date.month + 1
            val Day = date.day
            val shot_Day = if (Month < 10) {
                "$Year-0$Month-$Day"
            } else {
                "$Year-$Month-$Day"
            }
            calendarView.clearSelection()

            for (i in writeDateAry.indices) {
                if(shot_Day == writeDateAry[i].writeDate){
                    parentNameTxt.text = "보호자명 : " + writeDateAry[i].parentName
                    writeDateTxt.text = "작성일자 : " + writeDateAry[i].writeDate
                    visitNoticeTxt.text = "면회시 주의사항 : " + writeDateAry[i].visitNotice
                }
            }


        })

        fab.setOnClickListener {
            intent = Intent(this@VisitAdminCalendarActivity, VisitAdminWriteActivity::class.java)
            intent.putExtra("parentName", parentName)
            intent.putExtra("parentId", parentId)
            startActivity(intent)
            finish()
        }
    }

    fun getToParentBoard(parentId : String?){

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
                    val userId : String = result[i].userId
                    val parentName : String = result[i].parentName
                    val visitNotice : String = result[i].visitNotice
                    val writeDate : String = result[i].writeDate

                    val getServerdata = ResultVisit(
                       userId,
                        parentName,
                        visitNotice,
                        writeDate
                    )

                    writeDateAry.add(getServerdata)
                    //saveList.add(getServerdata)

                    for (i in writeDateAry.indices) {
                        val time =  writeDateAry[i].writeDate.split("-".toRegex()).toTypedArray()
                        //val time: Array<String> = result[i].split(",".toRegex()).toTypedArray()

                        val year = time[0].toInt()
                        val month = time[1].toInt()
                        val dayy = time[2].toInt()

                        calendar.set(Calendar.YEAR, year)

                        when(month){
                            1 -> calendar.set(Calendar.MONTH,0)
                            2 -> calendar.set(Calendar.MONTH,1)
                            3 -> calendar.set(Calendar.MONTH,2)
                            4 -> calendar.set(Calendar.MONTH,3)
                            5 -> calendar.set(Calendar.MONTH,4)
                            6 -> calendar.set(Calendar.MONTH,5)
                            7 -> calendar.set(Calendar.MONTH,6)
                            8 -> calendar.set(Calendar.MONTH,7)
                            9 -> calendar.set(Calendar.MONTH,8)
                            10 -> calendar.set(Calendar.MONTH,9)
                            11 -> calendar.set(Calendar.MONTH,10)
                            12 -> calendar.set(Calendar.MONTH,11)
                            else -> Log.d("TAG","no Point!")
                        }
                        calendar.set(Calendar.DATE, dayy)

                        calendarView.addDecorator(EventDecorator(Color.RED, Collections.singleton(CalendarDay.from(calendar))))
                    }
                }
            }

            override fun onFailure(call: Call<List<ResultVisit>>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@VisitAdminCalendarActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}