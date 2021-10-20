package com.psmStudio.newbabyproject.Visit

import android.app.ProgressDialog
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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.psmStudio.newbabyproject.BaseActivity
import com.psmStudio.newbabyproject.R
import com.psmStudio.newbabyproject.utils.Common
import com.psmStudio.newbabyproject.utils.FileUtils
import com.github.siyamed.shapeimageview.RoundedImageView
import com.opensooq.supernova.gligar.GligarPicker
import kotlinx.android.synthetic.main.activity_visit_admin_update.*
import kotlinx.android.synthetic.main.activity_visit_admin_update.babyEtcTxt
import kotlinx.android.synthetic.main.activity_visit_admin_update.babyLactationTxt
import kotlinx.android.synthetic.main.activity_visit_admin_update.babyRequireItemTxt
import kotlinx.android.synthetic.main.activity_visit_admin_update.babyWeightTxt
import kotlinx.android.synthetic.main.activity_visit_admin_update.cameraIcon
import kotlinx.android.synthetic.main.activity_visit_admin_update.contentTxt
import kotlinx.android.synthetic.main.activity_visit_admin_update.imageLinear
import kotlinx.android.synthetic.main.activity_visit_admin_update.imageTxtCount
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

            contentTxt.setText(visitNotice)
            babyWeightTxt.setText(babyWeight)
            babyLactationTxt.setText(babyLactation)
            babyRequireItemTxt.setText(babyRequireItem)
            babyEtcTxt.setText(babyEtc)

            for(i in pathList.indices){
                uriList2.add(Uri.fromFile(File(originalPathList[i])))
                count++
                setImageUpdate(pathList[i])
            }
        }

        //이미지 불러오기기
        cameraIcon.setOnClickListener{
            selectImage()
        }

        updateBtn.setOnClickListener {



            Handler().postDelayed(Runnable {
                updateBoard(uriList2)
                //여기에 딜레이 후 시작할 작업들을 입력
            }, 2000) // 0.5초 정도 딜레이를 준 후 시작

        }

    }

    private fun updateBoard(filePath : MutableList<Uri>?){

        val Seq = seq
        val User = loginId
        val visitNotice = contentTxt.text.toString()
        val babyWeight = babyWeightTxt.text.toString()
        val babyLactation = babyLactationTxt.text.toString()
        val babyRequireItem = babyRequireItemTxt.text.toString()
        val babyEtc = babyEtcTxt.text.toString()

        val date = Common.nowDate("yyyy-MM-dd HH:mm:ss")

        val seqPart = RequestBody.create(MultipartBody.FORM, Seq.toString())
        val parentIdPart = RequestBody.create(MultipartBody.FORM, parentId)
        val visitNoticePart = RequestBody.create(MultipartBody.FORM, visitNotice)
        val babyWeightPart = RequestBody.create(MultipartBody.FORM, babyWeight)
        val babyLactationPart = RequestBody.create(MultipartBody.FORM, babyLactation)
        val babyRequireItemPart = RequestBody.create(MultipartBody.FORM, babyRequireItem)
        val babyEtcPart = RequestBody.create(MultipartBody.FORM, babyEtc)

        val updateIdPart = RequestBody.create(MultipartBody.FORM, User)
        val updateDatePart = RequestBody.create(MultipartBody.FORM, date)

        when (filePath?.size) {
            0 ->{
                updateCall = mVisitApi.toParentUpdateNoImage(
                    seqPart,
                    parentIdPart,
                    visitNoticePart,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
                    updateIdPart,
                    updateDatePart
                )
            }
            1 ->{
                val originalPath = RequestBody.create(MultipartBody.FORM, filePath[0].toString())


                val f1 : File = FileUtils.getFile(this@VisitAdminUpdateActivity, filePath[0])
                val imagePart = RequestBody.create(MediaType.parse("multipart/form-data"), f1)
                val file1 = MultipartBody.Part.createFormData("image[]", f1.name, imagePart)

                updateCall = mVisitApi.toParentUpdate(
                    seqPart,
                    parentIdPart,
                    visitNoticePart,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
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

                val originalPath = RequestBody.create(MultipartBody.FORM, filePath[0].toString())
                val originalPath2 = RequestBody.create(MultipartBody.FORM, filePath[1].toString())

                val f1 : File = FileUtils.getFile(this@VisitAdminUpdateActivity, filePath[0])
                val imagePart = RequestBody.create(MediaType.parse("multipart/form-data"), f1)
                val file1 = MultipartBody.Part.createFormData("image[]", f1.name, imagePart)

                val f2 : File = FileUtils.getFile(this@VisitAdminUpdateActivity, filePath[1])
                val imagePart2 = RequestBody.create(MediaType.parse("multipart/form-data"), f2)
                val file2 = MultipartBody.Part.createFormData("image[]", f2.name, imagePart2)

                updateCall = mVisitApi.toParentUpdate(
                    seqPart,
                    parentIdPart,
                    visitNoticePart,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
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

                val originalPath = RequestBody.create(MultipartBody.FORM, filePath[0].toString())
                val originalPath2 = RequestBody.create(MultipartBody.FORM, filePath[1].toString())
                val originalPath3 = RequestBody.create(MultipartBody.FORM, filePath[2].toString())

                val f1 : File = FileUtils.getFile(this@VisitAdminUpdateActivity, filePath[0])
                val imagePart = RequestBody.create(MediaType.parse("multipart/form-data"), f1)
                val file1 = MultipartBody.Part.createFormData("image[]", f1.name, imagePart)

                val f2 : File = FileUtils.getFile(this@VisitAdminUpdateActivity, filePath[1])
                val imagePart2 = RequestBody.create(MediaType.parse("multipart/form-data"), f2)
                val file2 = MultipartBody.Part.createFormData("image[]", f2.name, imagePart2)

                val f3 : File = FileUtils.getFile(this@VisitAdminUpdateActivity, filePath[2])
                val imagePart3 = RequestBody.create(MediaType.parse("multipart/form-data"), f3)
                val file3 = MultipartBody.Part.createFormData("image[]", f3.name, imagePart3)
                updateCall = mVisitApi.toParentUpdate(
                    seqPart,
                    parentIdPart,
                    visitNoticePart,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
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
    fun setImageUpdate(imagePath: String) {
        val inflater2 =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val statLayoutItem2 =
            inflater2.inflate(R.layout.addimage, null) as LinearLayout
        val addImg: RoundedImageView = statLayoutItem2.findViewById(R.id.addImage)
        val delImg =
            statLayoutItem2.findViewById<ImageView>(R.id.delImage)
        delImg.setOnClickListener {

            if (pathList.contains(imagePath)) {
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

}