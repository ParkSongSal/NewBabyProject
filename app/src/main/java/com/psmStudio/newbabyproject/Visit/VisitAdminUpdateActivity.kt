package com.psmStudio.newbabyproject.Visit

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.psmStudio.newbabyproject.BaseActivity
import com.psmStudio.newbabyproject.R
import com.psmStudio.newbabyproject.utils.Common
import com.github.siyamed.shapeimageview.RoundedImageView
import com.opensooq.supernova.gligar.GligarPicker
import com.psmStudio.newbabyproject.utils.FileUtils
import kotlinx.android.synthetic.main.activity_visit_admin_update.*
import kotlinx.android.synthetic.main.activity_visit_admin_update.babyEtcTxt
import kotlinx.android.synthetic.main.activity_visit_admin_update.babyLactationTxt
import kotlinx.android.synthetic.main.activity_visit_admin_update.babyRequireItemTxt
import kotlinx.android.synthetic.main.activity_visit_admin_update.babyWeightTxt
import kotlinx.android.synthetic.main.activity_visit_admin_update.cameraIcon
import kotlinx.android.synthetic.main.activity_visit_admin_update.imageLinear
import kotlinx.android.synthetic.main.activity_visit_admin_update.imageTxtCount
import kotlinx.android.synthetic.main.activity_visit_admin_update.saveSpinner
import kotlinx.android.synthetic.main.activity_visit_admin_update.reservLl
import kotlinx.android.synthetic.main.activity_visit_admin_update.saveReserveDate
import kotlinx.android.synthetic.main.activity_visit_admin_update.saveReserveTime
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

class VisitAdminUpdateActivity : BaseActivity() {

    val PICKER_REQUEST_CODE = 101

    lateinit var updateCall: Call<ResponseBody>


    private var pathsList = emptyArray<String>()

    var count =0
    var saveGubun = 0

    var seq = 0
    var babyName : String? = null
    var parentId: String? = null
    var parentName: String? = null
    var visitNotice : String? = null
    var babyWeight : String? = null
    var babyLactation : String? = null
    var babyRequireItem : String? = null
    var babyEtc : String? = null
    var pathList = java.util.ArrayList<String>()
    var originalPathList = ArrayList<String>()
    private var uriList : MutableList<String> = mutableListOf()
    private var uriList2 : MutableList<Uri> = mutableListOf()
    var asyncDialog : ProgressDialog? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_admin_update)

        toolbar.setTitleTextColor(getColor(R.color.whiteColor))
        toolbar.title = "면회소식 수정"
        setSupportActionBar(toolbar)

        init(this@VisitAdminUpdateActivity)

        loginId = setting.getString("loginId", "").toString()




        if(intent != null){
            seq = intent.getIntExtra("seq",0)

            babyName = intent.getStringExtra("babyName")
            parentId = intent.getStringExtra("parentId")

            parentName = intent.getStringExtra("parentName")
            visitNotice = intent.getStringExtra("visitNotice")
            babyWeight = intent.getStringExtra("babyWeight")
            babyLactation = intent.getStringExtra("babyLactation")
            babyRequireItem = intent.getStringExtra("babyRequireItem")
            babyEtc = intent.getStringExtra("babyEtc")

            pathList = intent.getSerializableExtra("pathList") as ArrayList<String>
            originalPathList = intent.getSerializableExtra("originalPathList") as ArrayList<String>

            babyWeightTxt.setText(babyWeight)
            babyLactationTxt.setText(babyLactation)
            babyRequireItemTxt.setText(babyRequireItem)
            babyEtcTxt.setText(babyEtc)

            Log.d("TAG", " pathList 1 : $pathList")

            Log.d("TAG", " originalPathList 1 : $originalPathList")

            for(i in pathList.indices){

                uriList2.add(Uri.fromFile(File(originalPathList[i])))
                count++
                setImageUpdate(pathList[i], originalPathList[i])
            }
        }



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
                        reservLl.visibility = View.GONE
                    }
                    1 -> {
                        saveGubun = 1
                        reservLl.visibility = View.GONE
                    }
                    2 -> {
                        saveGubun = 2
                        reservLl.visibility = View.VISIBLE
                    }
                    else -> {
                        saveGubun = 0
                        reservLl.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        this.InitializeListener()


        //이미지 불러오기기
        cameraIcon.setOnClickListener{
            selectImage()
        }

        updateBtn.setOnClickListener {



            Handler().postDelayed(Runnable {
                updateBoard(saveGubun, uriList2)
                //여기에 딜레이 후 시작할 작업들을 입력
            }, 2000) // 0.5초 정도 딜레이를 준 후 시작

        }
    }

    private fun updateBoard(saveGubun: Int, filePath : MutableList<Uri>?){

        val Seq = seq
        val User = loginId
        val babyWeight = babyWeightTxt.text.toString()
        val babyLactation = babyLactationTxt.text.toString()
        val babyRequireItem = babyRequireItemTxt.text.toString()
        val babyEtc = babyEtcTxt.text.toString()

        val date = Common.nowDate("yyyy-MM-dd HH:mm:ss")

        val seqPart = RequestBody.create(MultipartBody.FORM, Seq.toString())
        val parentIdPart = RequestBody.create(MultipartBody.FORM, parentId)
        val babyWeightPart = RequestBody.create(MultipartBody.FORM, babyWeight)
        val babyLactationPart = RequestBody.create(MultipartBody.FORM, babyLactation)
        val babyRequireItemPart = RequestBody.create(MultipartBody.FORM, babyRequireItem)
        val babyEtcPart = RequestBody.create(MultipartBody.FORM, babyEtc)

        val updateIdPart = RequestBody.create(MultipartBody.FORM, User)
        val updateDatePart = RequestBody.create(MultipartBody.FORM, date)

        var tempYn: String = "N"
        var tempYnPart : RequestBody? = null
        val reserveDate: String
        var reserveDatePart : RequestBody? = null

        Log.d("TAG", "Parent Id  $parentId")

        when (saveGubun) {
            1 -> {     //임시저장
                tempYn = "Y"
                tempYnPart = RequestBody.create(MultipartBody.FORM, tempYn)
            }
            2 -> {      // 예약저장
                tempYn = "N"
                tempYnPart = RequestBody.create(MultipartBody.FORM, tempYn)

                reserveDate = saveReserveDate.text.toString() + " " + saveReserveTime.text.toString()
                reserveDatePart = RequestBody.create(MultipartBody.FORM, reserveDate)
            }
            else -> {
                tempYn = "N"
                tempYnPart = RequestBody.create(MultipartBody.FORM, tempYn)
                reserveDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")
                reserveDatePart = RequestBody.create(MultipartBody.FORM, reserveDate)
            }
        }

        when (filePath?.size) {
            0 ->{
                updateCall = mVisitApi.toParentUpdateNoImage(
                    seqPart,
                    parentIdPart,
                    null,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
                    tempYnPart,
                    reserveDatePart,
                    updateIdPart,
                    updateDatePart
                )
            }
            1 ->{

                var fileFormat : Uri? = null
                var f1 : File? = null
                if (filePath[0].toString().contains("(")) {
                    val idx = filePath[0].toString().indexOf("(")
                    val path = filePath[0].toString().substring(0, idx)

                    fileFormat = Uri.parse(path)
                    f1 = FileUtils.getFile(this@VisitAdminUpdateActivity, fileFormat)
                } else {
                    f1 = FileUtils.getFile(this@VisitAdminUpdateActivity, filePath[0])
                }

                val originalPath = RequestBody.create(MultipartBody.FORM, fileFormat.toString())
                val imagePart = RequestBody.create(MediaType.parse("multipart/form-data"), f1)
                val file1 = MultipartBody.Part.createFormData("image[]", f1?.name, imagePart)

                updateCall = mVisitApi.toParentUpdate(
                    seqPart,
                    parentIdPart,
                    null,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
                    tempYnPart,
                    reserveDatePart,
                    file1,
                    null,
                    null,
                    originalPath,
                    null,
                    null,
                    updateIdPart,
                    updateDatePart
                )
            }
            2 ->{

                var fileFormat : Uri? = null
                var f1 : File?
                if (filePath[0].toString().contains("(")) {
                    val idx = filePath[0].toString().indexOf("(")
                    val path = filePath[0].toString().substring(0, idx)

                    fileFormat = Uri.parse(path)
                    f1 = FileUtils.getFile(this@VisitAdminUpdateActivity, fileFormat)
                } else {
                    f1 = FileUtils.getFile(this@VisitAdminUpdateActivity, filePath[0])
                }

                var fileFormat2 : Uri? = null
                var f2: File?
                if (filePath[1].toString().contains("(")) {
                    val idx = filePath[1].toString().indexOf("(")
                    val path = filePath[1].toString().substring(0, idx)

                    fileFormat2 = Uri.parse(path)
                    f2 = FileUtils.getFile(this@VisitAdminUpdateActivity, fileFormat2)
                } else {
                    f2 = FileUtils.getFile(this@VisitAdminUpdateActivity, filePath[1])
                }


                val originalPath = RequestBody.create(MultipartBody.FORM, fileFormat.toString())
                val originalPath2 = RequestBody.create(MultipartBody.FORM, fileFormat2.toString())

                val imagePart = RequestBody.create(MediaType.parse("multipart/form-data"), f1)
                val file1 = MultipartBody.Part.createFormData("image[]", f1?.name, imagePart)

                val imagePart2 = RequestBody.create(MediaType.parse("multipart/form-data"), f2)
                val file2 = MultipartBody.Part.createFormData("image[]", f2?.name, imagePart2)

                updateCall = mVisitApi.toParentUpdate(
                    seqPart,
                    parentIdPart,
                    null,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
                    tempYnPart,
                    reserveDatePart,
                    file1,
                    file2,
                    null,
                    originalPath,
                    originalPath2,
                    null,
                    updateIdPart,
                    updateDatePart
                )
            }
            3 ->{

                var fileFormat : Uri? = null
                var f1 : File?
                if (filePath[0].toString().contains("(")) {
                    val idx = filePath[0].toString().indexOf("(")
                    val path = filePath[0].toString().substring(0, idx)

                    fileFormat = Uri.parse(path)
                    f1 = FileUtils.getFile(this@VisitAdminUpdateActivity, fileFormat)
                } else {
                    f1 = FileUtils.getFile(this@VisitAdminUpdateActivity, filePath[0])
                }

                var fileFormat2 : Uri? = null
                var f2: File?
                if (filePath[1].toString().contains("(")) {
                    val idx = filePath[1].toString().indexOf("(")
                    val path = filePath[1].toString().substring(0, idx)

                    fileFormat2 = Uri.parse(path)
                    f2 = FileUtils.getFile(this@VisitAdminUpdateActivity, fileFormat2)
                } else {
                    f2 = FileUtils.getFile(this@VisitAdminUpdateActivity, filePath[1])
                }

                var fileFormat3 : Uri? = null
                var f3: File?
                if (filePath[2].toString().contains("(")) {
                    val idx = filePath[2].toString().indexOf("(")
                    val path = filePath[2].toString().substring(0, idx)

                    fileFormat3 = Uri.parse(path)
                    f3 = FileUtils.getFile(this@VisitAdminUpdateActivity, fileFormat3)
                } else {
                    f3 = FileUtils.getFile(this@VisitAdminUpdateActivity, filePath[2])
                }

                val originalPath = RequestBody.create(MultipartBody.FORM, fileFormat.toString())
                val originalPath2 = RequestBody.create(MultipartBody.FORM, fileFormat2.toString())
                val originalPath3 = RequestBody.create(MultipartBody.FORM, fileFormat3.toString())

                val imagePart = RequestBody.create(MediaType.parse("multipart/form-data"), f1)
                val file1 = MultipartBody.Part.createFormData("image[]", f1?.name, imagePart)

                val imagePart2 = RequestBody.create(MediaType.parse("multipart/form-data"), f2)
                val file2 = MultipartBody.Part.createFormData("image[]", f2?.name, imagePart2)

                val imagePart3 = RequestBody.create(MediaType.parse("multipart/form-data"), f3)
                val file3 = MultipartBody.Part.createFormData("image[]", f3?.name, imagePart3)
                updateCall = mVisitApi.toParentUpdate(
                    seqPart,
                    parentIdPart,
                    null,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
                    tempYnPart,
                    reserveDatePart,
                    file1,
                    file2,
                    file3,
                    originalPath,
                    originalPath2,
                    originalPath3,
                    updateIdPart,
                    updateDatePart
                )
            }

        }


        updateCall.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if(response.isSuccessful){

                    intent = Intent(this@VisitAdminUpdateActivity, VisitAdmintoParentListActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    intent.putExtra("parentName", parentName)
                    intent.putExtra("parentId", parentId)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@VisitAdminUpdateActivity, "등록되었습니다.", Toast.LENGTH_LONG).show()
                }else{
                    dlg.setMessage("다시 시도 바랍니다.")
                        .setNegativeButton("확인", null)
                    dlg.show()
                }
            }

            override fun onFailure(
                call: Call<ResponseBody?>,
                t: Throwable
            ) {
                Log.d("TAG","Error : " + t.message)
                Toast.makeText(
                    this@VisitAdminUpdateActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()

            }


        })


    }
    fun setImageUpdate(imagePath: String, oriPath: String) {
        Log.d("TAG","oriPath : $oriPath")
        val inflater2 =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val statLayoutItem2 =
            inflater2.inflate(R.layout.addimage, null) as LinearLayout
        val addImg: RoundedImageView = statLayoutItem2.findViewById(R.id.addImage)
        val delImg =
            statLayoutItem2.findViewById<ImageView>(R.id.delImage)
        delImg.setOnClickListener {

            Log.d("TAG","imagePath 222 : $imagePath")
            Log.d("TAG","oriPat 222h : $oriPath")

            if (pathList.contains(imagePath)) {
                val idx = imagePath.indexOf("(")
                val fileIdx = imagePath.substring(idx+1, idx+2)

                val idx2 = oriPath.indexOf("(")
                val fileIdx2 = oriPath.substring(idx2+1, idx2+2)
                if(fileIdx == fileIdx2){
                    Log.d("TAG","oriPat 3333 : $oriPath")
                    uriList2.remove(Uri.fromFile(File(oriPath)))
                    Log.d("TAG","uriList2 3333 : $uriList2")

                }

                pathList.remove(imagePath)
                count--
                imageLinear.removeView(statLayoutItem2)
                imageTxtCount.text = "$count/3"
                if(count == 0){
                    uriList2.clear()
                }
            }
            Toast.makeText(
                this@VisitAdminUpdateActivity,
                "ImageView $imagePath",
                Toast.LENGTH_SHORT
            ).show()
        }
        Glide.with(applicationContext)
            .load(imagePath)
            .override(300, 300)
            .fitCenter()
            .into(addImg)
        imageLinear.addView(statLayoutItem2)
        imageTxtCount.text = "$count/3"
    }

    override fun onBackPressed() {

        dlg.setTitle("게시글 수정 취소 알림")
            .setMessage("게시글 수정을 취소하시겠습니까??")
            .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(this@VisitAdminUpdateActivity, "취소되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@VisitAdminUpdateActivity, VisitAdmintoParentListActivity::class.java)
                intent.putExtra("parentId", parentId)
                intent.putExtra("parentName", parentName)
                startActivity(intent)
                finish()
            })
            .setNegativeButton("아니오", null)
        dlg.show()

    } //뒤로가기 종료버튼

    private fun selectImage() {
        if (pathsList.isNotEmpty()) {
            Toast.makeText(this@VisitAdminUpdateActivity, "현재 이미지 개수 $count", Toast.LENGTH_SHORT).show()
            if (count == 3) {
                Toast.makeText(this@VisitAdminUpdateActivity, "이미지는 최대 3장까지 선택가능합니다.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                GligarPicker()
                    .requestCode(PICKER_REQUEST_CODE)
                    .limit(3 - count) // 최대 이미지 수
                    .withActivity(this@VisitAdminUpdateActivity) //Activity
                    //.withFragment -> Fragment
                    // .disableCamera(false) -> 카메라 캡처를 사용할지
                    // .cameraDirect(true) -> 바로 카메라를 실행할지
                    .show()
            }
        } else {
            GligarPicker()
                .requestCode(PICKER_REQUEST_CODE)
                .limit(3) // 최대 이미지 수
                .withActivity(this@VisitAdminUpdateActivity) //Activity
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
                    if(count == 0){
                        uriList2.clear()
                    }

                }
                Toast.makeText(
                    this@VisitAdminUpdateActivity,
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

}