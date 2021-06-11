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
import com.example.newbabyproject.NoticeInsertActivity
import com.example.newbabyproject.R
import com.example.newbabyproject.utils.Common
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_notice_list.*
import kotlinx.android.synthetic.main.activity_visit_admin_calendar.*
import kotlinx.android.synthetic.main.activity_visit_admin_calendar.fab
import java.util.*

class VisitAdminCalendarActivity : BaseActivity() {
    var parentName : String? = ""
    var userId : String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_admin_calendar)

        init(this@VisitAdminCalendarActivity)

        if(intent != null){
            parentName = intent.getStringExtra("parentName")
            userId = intent.getStringExtra("userId")
        }else{
            Common.intentCommon(this@VisitAdminCalendarActivity, MainActivity::class.java)
            Toast.makeText(applicationContext, "잘못된 경로입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }


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

        val result = arrayOf("2021,03,18", "2021,04,18","2021,04,19","2021,05,26","2021,05,27", "2021,05,18", "2021,11,18")

        val calendar = Calendar.getInstance()

        for (i in result.indices) {
            val time: Array<String> = result[i].split(",".toRegex()).toTypedArray()

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

            Toast.makeText(applicationContext, shot_Day, Toast.LENGTH_SHORT).show()
        })

        fab.setOnClickListener {
            intent = Intent(this@VisitAdminCalendarActivity, NoticeInsertActivity::class.java)
            intent.putExtra("parentName", parentName)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }


    }
}