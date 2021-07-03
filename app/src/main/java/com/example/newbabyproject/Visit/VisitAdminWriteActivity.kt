package com.example.newbabyproject.Visit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.newbabyproject.*
import com.example.newbabyproject.utils.Common
import com.example.newbabyproject.utils.FileUtils
import com.github.siyamed.shapeimageview.RoundedImageView
import com.opensooq.supernova.gligar.GligarPicker
import kotlinx.android.synthetic.main.activity_app_introduce_modify.*
import kotlinx.android.synthetic.main.activity_app_introduce_modify.contentTxt
import kotlinx.android.synthetic.main.activity_app_introduce_modify.insertBtn
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_visit_admin_write.*
import kotlinx.android.synthetic.main.item_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class VisitAdminWriteActivity : BaseActivity() {

    private val PICKER_REQUEST_CODE = 101


    lateinit var call: Call<ResponseBody>

    var parentName = ""
    var parentId = ""

    var saveGubun = 0

    private var bitmap: Bitmap? = null
    private var path: Uri? = null

    private var pathsList = emptyArray<String>()
    private var uriList : MutableList<String> = mutableListOf()
    private var uriList2 : MutableList<Uri> = mutableListOf()
    var count = 0

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
                    (80 * dp).toInt()
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
                            (60 * dp).toInt()
                        )
                        contentScroll.layoutParams = layoutParams
                        reservLl.visibility = View.VISIBLE
                    }
                    else -> {
                        saveGubun = 0
                        contentScroll.layoutParams = layoutParams
                        reservLl.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        insertBtn.setOnClickListener(View.OnClickListener {
            adminWriteAct(saveGubun, uriList2)
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
/*

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.saveBtn -> {
                adminWriteAct(saveGubun)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
*/

    private fun adminWriteAct(saveGubun: Int, filePath: MutableList<Uri>? ) {
        var tempYn: String = "N"
        var tempYnPart : RequestBody? = null

        val reserveDate: String
        var reserveDatePart : RequestBody? = null

        var writeDate = Common.nowDate("yyyy-MM-dd")
        when (saveGubun) {
            1 -> {     //임시저장
                tempYn = "Y"
                tempYnPart = RequestBody.create(MultipartBody.FORM, tempYn)
            }
            2 -> {      // 예약저장
                tempYn = "N"
                tempYnPart = RequestBody.create(MultipartBody.FORM, tempYn)

                reserveDate =
                    saveReserveDate.text.toString() + " " + saveReserveTime.text.toString()
                writeDate = saveReserveDate.text.toString()
                reserveDatePart = RequestBody.create(MultipartBody.FORM, reserveDate)
            }
            else -> {
                tempYn = "N"
                tempYnPart = RequestBody.create(MultipartBody.FORM, tempYn)

                reserveDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")
                reserveDatePart = RequestBody.create(MultipartBody.FORM, reserveDate)
            }
        }


        val visitNotice = contentTxt.text.toString()
        val babyWeight = babyWeightTxt.text.toString()
        val babyLactation = babyLactationTxt.text.toString()
        val babyRequireItem = babyRequireItemTxt.text.toString()
        val babyEtc = babyEtcTxt.text.toString()

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

        when(filePath?.size){
            0 ->{
                call = mVisitApi.toParentInsertNoImage(
                    parentIdPart,
                    parentNamePart,
                    visitNoticePart,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
                    writeDatePart,
                    tempYnPart,
                    reserveDatePart,
                    insertIdPart,
                    insertDatePart,
                    updateIdPart,
                    updateDatePart
                )
            }
            1 ->{
                val f1 : File = FileUtils.getFile(this@VisitAdminWriteActivity, filePath[0])
                val imagePart = RequestBody.create(MediaType.parse("multipart/form-data"), f1)
                val file1 = MultipartBody.Part.createFormData("image[]", f1.name, imagePart)

                call = mVisitApi.toParentInsert(
                    parentIdPart,
                    parentNamePart,
                    visitNoticePart,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
                    writeDatePart,
                    tempYnPart,
                    reserveDatePart,
                    file1,
                    null,
                    null,
                    insertIdPart,
                    insertDatePart,
                    updateIdPart,
                    updateDatePart
                )
            }
            2 ->{
                val f1 : File = FileUtils.getFile(this@VisitAdminWriteActivity, filePath[0])
                val imagePart = RequestBody.create(MediaType.parse("multipart/form-data"), f1)
                val file1 = MultipartBody.Part.createFormData("image[]", f1.name, imagePart)

                val f2 : File = FileUtils.getFile(this@VisitAdminWriteActivity, filePath[1])
                val imagePart2 = RequestBody.create(MediaType.parse("multipart/form-data"), f2)
                val file2 = MultipartBody.Part.createFormData("image[]", f2.name, imagePart2)

                call = mVisitApi.toParentInsert(
                    parentIdPart,
                    parentNamePart,
                    visitNoticePart,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
                    writeDatePart,
                    tempYnPart,
                    reserveDatePart,
                    file1,
                    file2,
                    null,
                    insertIdPart,
                    insertDatePart,
                    updateIdPart,
                    updateDatePart
                )
            }
            3 ->{
                val f1 : File = FileUtils.getFile(this@VisitAdminWriteActivity, filePath[0])
                val imagePart = RequestBody.create(MediaType.parse("multipart/form-data"), f1)
                val file1 = MultipartBody.Part.createFormData("image[]", f1.name, imagePart)

                val f2 : File = FileUtils.getFile(this@VisitAdminWriteActivity, filePath[1])
                val imagePart2 = RequestBody.create(MediaType.parse("multipart/form-data"), f2)
                val file2 = MultipartBody.Part.createFormData("image[]", f2.name, imagePart2)

                val f3 : File = FileUtils.getFile(this@VisitAdminWriteActivity, filePath[2])
                val imagePart3 = RequestBody.create(MediaType.parse("multipart/form-data"), f3)
                val file3 = MultipartBody.Part.createFormData("image[]", f3.name, imagePart3)
                call = mVisitApi.toParentInsert(
                    parentIdPart,
                    parentNamePart,
                    visitNoticePart,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
                    writeDatePart,
                    tempYnPart,
                    reserveDatePart,
                    file1,
                    file2,
                    file3,
                    insertIdPart,
                    insertDatePart,
                    updateIdPart,
                    updateDatePart
                )
            }
        }



        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                Log.d("TAG","response : " + response.body())
                Log.d("TAG","response : " + response.body().toString())
                if(response.isSuccessful){
                    Log.d("TAG","response : " + response.isSuccessful)

                    intent = Intent(this@VisitAdminWriteActivity,VisitAdmintoParentListActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    intent.putExtra("parentId", parentId)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@VisitAdminWriteActivity, "등록되었습니다.", Toast.LENGTH_LONG).show()
                }else{
                    dlg.setMessage("다시 시도 바랍니다.")
                        .setNegativeButton("확인", null)
                    dlg.show()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("TAG","Error : " + t.message)
                Log.d("TAG","Error : " + t.localizedMessage)
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

                val month = when (monthOfYear+1) {
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

    fun onClick(view: View) {
        when (view.id) {
            /* 앱 소개 */
            R.id.imgRL,
            R.id.cameraIcon,
            R.id.imageTxtCount-> {
                selectImage()
            }

        }
    }


    private fun selectImage() {
        if (pathsList.isNotEmpty()) {
            Toast.makeText(this@VisitAdminWriteActivity, "현재 이미지 개수 $count", Toast.LENGTH_SHORT).show()
            if (count == 3) {
                Toast.makeText(this@VisitAdminWriteActivity, "이미지는 최대 3장까지 선택가능합니다.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                GligarPicker()
                    .requestCode(PICKER_REQUEST_CODE)
                    .limit(3 - count) // 최대 이미지 수
                    .withActivity(this@VisitAdminWriteActivity) //Activity
                    //.withFragment -> Fragment
                    // .disableCamera(false) -> 카메라 캡처를 사용할지
                    // .cameraDirect(true) -> 바로 카메라를 실행할지
                    .show()
            }
        } else {
            GligarPicker()
                .requestCode(PICKER_REQUEST_CODE)
                .limit(3) // 최대 이미지 수
                .withActivity(this@VisitAdminWriteActivity) //Activity
                //.withFragment -> Fragment
                // .disableCamera(false) -> 카메라 캡처를 사용할지
                // .cameraDirect(true) -> 바로 카메라를 실행할지
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICKER_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            pathsList = data.extras?.getStringArray(GligarPicker.IMAGES_RESULT) as Array<String> // return list of selected images paths.
            for (i in pathsList.indices) {
                val uri : Uri = Uri.parse(pathsList[i])
                uriList.add(pathsList[i])
                uriList2.add(Uri.fromFile(File(pathsList[i])))
                Log.d("TAG", "uriList2 : $uriList2")
                count++
                setImage(pathsList[i])
            }
        }
    }

    // 파일 경로를 받아와 Bitmap 으로 변환후 ImageView 적용
    fun setImage(imagePath: String) {
        val imgFile = File(imagePath)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            val inflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val statLayoutItem = inflater.inflate(R.layout.addimage, null) as LinearLayout
            val addImg: RoundedImageView = statLayoutItem.findViewById(R.id.addImage)
            val delImg =
                statLayoutItem.findViewById<ImageView>(R.id.delImage)
            delImg.setOnClickListener {

                if (uriList.contains(imagePath)) {
                    uriList.remove(imagePath)
                    count--
                    imageLinear.removeView(statLayoutItem)
                    imageTxtCount.text = "$count/3"

                }
                Toast.makeText(
                    this@VisitAdminWriteActivity,
                    "ImageView $imagePath",
                    Toast.LENGTH_SHORT
                ).show()
            }
            Glide.with(applicationContext)
                .load(myBitmap)
                .override(300, 300)
                .fitCenter()
                .into(addImg)
            imageLinear.addView(statLayoutItem)
            imageTxtCount.text = "$count/3"
        }
    }

}