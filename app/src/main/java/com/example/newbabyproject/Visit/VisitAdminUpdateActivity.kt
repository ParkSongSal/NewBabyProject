package com.example.newbabyproject.Visit

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.newbabyproject.BaseActivity
import com.example.newbabyproject.R
import com.opensooq.supernova.gligar.GligarPicker
import kotlinx.android.synthetic.main.item_toolbar.*

class VisitAdminUpdateActivity : BaseActivity() {



    private var pathsList = emptyArray<String>()

    var count =0

    var parentName: String? = "강호동"
    var parentId: String? = "test3"
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_admin_update)

        toolbar.setTitleTextColor(getColor(R.color.whiteColor))
        toolbar.title = "면회소식 수정"
        setSupportActionBar(toolbar)

        init(this@VisitAdminUpdateActivity)

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
}