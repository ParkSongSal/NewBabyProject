package com.psmStudio.newbabyproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.psmStudio.newbabyproject.Visit.ResultVisit
import com.psmStudio.newbabyproject.Visit.VisitParentCalendarActivity
import com.psmStudio.newbabyproject.utils.BackPressedForFinish
import com.psmStudio.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_app_introduce.*
import kotlinx.android.synthetic.main.activity_enter_introduce.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_out_introduce.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    var backPressedForFinish : BackPressedForFinish? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init(this@MainActivity)

        loginId = setting.getString("loginId", "").toString()

        getBabyInfo(loginId)

        backPressedForFinish = BackPressedForFinish(this@MainActivity)
    }

    fun mOnClick(view: View) {
        when (view.id) {

            /* 앱 소개 */
            R.id.intro_ll,
            R.id.appIntro,
            R.id.intro_img -> {
                Common.intentCommon(this@MainActivity, AppIntroduceActivity::class.java)
            }

            /* 입원 안내문 소개  */
            R.id.enter_ll,
            R.id.enterTxt,
            R.id.enter_img -> {
                Common.intentCommon(this@MainActivity, EnterIntroduceActivity::class.java)
            }

            /* 퇴원 안내문 소개*/
            R.id.leave_ll,
            R.id.leave_txt,
            R.id.leave_img -> {
                Common.intentCommon(this@MainActivity, OutIntroduceActivity::class.java)
            }

            /* 설정 */
            R.id.logoutImg -> {
                Common.intentCommon(this@MainActivity, SettingActivity::class.java)
                finish()
            }

            /* 공지사항 */
            R.id.notice_ll,
            R.id.notice_txt,
            R.id.notice_img -> {
                Common.intentCommon(this@MainActivity, NoticeListActivity::class.java)
            }
            /* 면회 */
            R.id.visit_ll,
            R.id.visitBtn,
            R.id.visit_img -> {
                if ("admin" == loginId) {
                    Common.intentCommon(this@MainActivity, VisitAdminUserSelActivity::class.java)
                } else {
                    Common.intentCommon(this@MainActivity, VisitParentCalendarActivity::class.java)
                }

            }
        }
    }

    // 로그아웃
    private fun Logout() {
        setting = getSharedPreferences("setting", MODE_PRIVATE)
        editor = setting.edit()
        editor.clear()
        //editor.commit();
        editor.apply()
        intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        startActivity(intent)
        finish()
    }


    fun getBabyInfo(loginId: String) {
        if(loginId == "admin"){
            Login_txt.text = "메인"
            babyNameTxt.text = "관리자님"
            babyBirthDateTxt.text = "환영합니다."
        }else{
            val loginIdPart = RequestBody.create(MultipartBody.FORM, loginId)

            mUserApi.mainBabyInfo(loginIdPart).enqueue(object :
                Callback<List<ResultVisit>> {
                override fun onResponse(
                    call: Call<List<ResultVisit>>,
                    response: Response<List<ResultVisit>>
                ) {

                    //정상 결과
                    val result: List<ResultVisit>? = response.body()

                    Log.d("TAG", "list : $result")
                    for (i in result!!.indices) {
                        babyNameTxt.text = "● " + result[i].babyName
                        babyNumTxt.text = " " + result[i].babyNum
                        babyBirthDateTxt.text = "● " + Common.dataSplitFormat(
                            result[i].babyBirthDate,
                            "date"
                        )
                        babyBirthTimeTxt.text = Common.dataSplitFormat(
                            result[i].babyBirthTime,
                            "time"
                        )
                    }
                }

                override fun onFailure(call: Call<List<ResultVisit>>, t: Throwable) {
                    // 네트워크 문제
                    Toast.makeText(
                        coxt,
                        "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    }

    override fun onBackPressed() {
        // BackPressedForFinish 클래시의 onBackPressed() 함수를 호출한다.
        backPressedForFinish?.onBackPressed()
    } //뒤로가기 종료버튼

}



