package com.example.newbabyproject.Visit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.newbabyproject.BaseActivity
import com.example.newbabyproject.MainActivity
import com.example.newbabyproject.R
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_app_introduce_modify.*
import kotlinx.android.synthetic.main.activity_app_introduce_modify.contentTxt
import kotlinx.android.synthetic.main.activity_app_introduce_modify.insertBtn
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_visit_admin_write.*
import kotlinx.android.synthetic.main.item_toolbar.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VisitAdminWriteActivity : BaseActivity() {

    lateinit var call: Call<ResultVisit>

    var parentName = ""
    var parentId = ""

    var saveGubun = 0
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_admin_write)


        toolbar.setTitleTextColor(getColor(R.color.whiteColor))
        toolbar.title = "면회소식 등록"
        setSupportActionBar(toolbar)

        init(this@VisitAdminWriteActivity)


        if(intent != null){
            parentName = intent.getStringExtra("parentName")
            parentId = intent.getStringExtra("parentId")
        }else{
            Common.intentCommon(this@VisitAdminWriteActivity, MainActivity::class.java)
            Toast.makeText(applicationContext, "잘못된 경로입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        loginId = setting.getString("loginId", "").toString()

        val items = resources.getStringArray(R.array.saveWay)
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        saveSpinner.adapter = myAdapter

        saveSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                var dp =  resources.displayMetrics.density
                var layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (130 * dp).toInt()
                )
                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when (position) {
                    0 -> {
                        saveGubun = 0
                        contentScroll.layoutParams = layoutParams
                        reservLl.visibility = View.GONE
                    }
                    1 -> {
                        saveGubun = 1
                        contentScroll.layoutParams = layoutParams
                        reservLl.visibility = View.GONE
                    }
                    2 -> {
                        saveGubun = 2
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            (100 * dp).toInt()
                        )
                        contentScroll.layoutParams = layoutParams
                        reservLl.visibility = View.VISIBLE
                    }
                    //...
                    else -> {
                        0
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        insertBtn.setOnClickListener(View.OnClickListener {
            adminWriteAct(saveGubun)
        })

        this.InitializeListener()

        saveReserveDate.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    //  .. 포커스시
                    val dialog = DatePickerDialog(this, dateCallbackMethod, 2021, 6, 10)

                    dialog.show()
                } else {
                    //  .. 포커스 뺏겼을 때
                }
            }

        saveReserveTime.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    //  .. 포커스시
                    val dialog = TimePickerDialog(
                        this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        timeCallbackMethod,
                        8,
                        10,
                        false
                    )

                    dialog.show()
                } else {
                    //  .. 포커스 뺏겼을 때
                }
            }



    }

    private fun adminWriteAct(saveGubun: Int) {


        val visitNotice = contentTxt.text.toString()
        val babyWeight = babyWeightTxt.text.toString()
        val babyLactation = babyLactationTxt.text.toString()
        val babyRequireItem = babyRequireItemTxt.text.toString()
        val babyEtc = babyEtcTxt.text.toString()
        val writeDate = Common.nowDate("yyyy-MM-dd")
        val date = Common.nowDate("yyyy-MM-dd HH:mm:ss")

        val parentIdPart = RequestBody.create(MultipartBody.FORM, parentId)
        val parentNamePart = RequestBody.create(MultipartBody.FORM, parentName)
        val visitNoticePart = RequestBody.create(MultipartBody.FORM, visitNotice)
        val babyWeightPart = RequestBody.create(MultipartBody.FORM, babyWeight)
        val babyLactationPart = RequestBody.create(MultipartBody.FORM, babyLactation)
        val babyRequireItemPart = RequestBody.create(MultipartBody.FORM, babyRequireItem)
        val babyEtcPart = RequestBody.create(MultipartBody.FORM, babyEtc)
        val writeDatePart = RequestBody.create(MultipartBody.FORM, writeDate)
        val insertIdPart = RequestBody.create(MultipartBody.FORM, loginId)
        val insertDatePart = RequestBody.create(MultipartBody.FORM, date)
        val updateIdPart = RequestBody.create(MultipartBody.FORM, loginId)
        val updateDatePart = RequestBody.create(MultipartBody.FORM, date)

        call = mVisitApi.toParentInsert(
            parentIdPart,
            parentNamePart,
            visitNoticePart,
            babyWeightPart,
            babyLactationPart,
            babyRequireItemPart,
            babyEtcPart,
            writeDatePart,
            insertIdPart,
            insertDatePart,
            updateIdPart,
            updateDatePart
        )

        call.enqueue(object : Callback<ResultVisit> {
            override fun onResponse(call: Call<ResultVisit>, response: Response<ResultVisit>) {

                // 정상결과
                if (response.body()!!.result == "success") {
                    intent = Intent(
                        this@VisitAdminWriteActivity,
                        VisitAdmintoParentListActivity::class.java
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    intent.putExtra("parentId", parentId)
                    startActivity(intent)
                    finish()
                    Toast.makeText(
                        this@VisitAdminWriteActivity,
                        "등록되었습니다.",
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
                    this@VisitAdminWriteActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    fun InitializeListener() {
        dateCallbackMethod =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                val month = when (monthOfYear) {
                    1 -> "01"
                    2 -> "02"
                    3 -> "03"
                    4 -> "04"
                    5 -> "05"
                    6 -> "06"
                    7 -> "07"
                    8 -> "08"
                    9 -> "09"
                    else -> monthOfYear.toString()
                }
                val day = when (dayOfMonth) {
                    1 -> "01"
                    2 -> "02"
                    3 -> "03"
                    4 -> "04"
                    5 -> "05"
                    6 -> "06"
                    7 -> "07"
                    8 -> "08"
                    9 -> "09"
                    else -> dayOfMonth.toString()
                }

                saveReserveDate.setText(
                    "$year-$month-$day"
                )
            }

        timeCallbackMethod =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val hour = when (hourOfDay) {
                    1 -> "01"
                    2 -> "02"
                    3 -> "03"
                    4 -> "04"
                    5 -> "05"
                    6 -> "06"
                    7 -> "07"
                    8 -> "08"
                    9 -> "09"
                    else -> hourOfDay.toString()
                }
                val min = when (minute) {
                    1 -> "01"
                    2 -> "02"
                    3 -> "03"
                    4 -> "04"
                    5 -> "05"
                    6 -> "06"
                    7 -> "07"
                    8 -> "08"
                    9 -> "09"
                    else -> minute.toString()
                }
                saveReserveTime.setText("$hour:$min")
            }
    }

}