/*
package com.example.newbabyproject.Retrofit2;

import android.telecom.Call;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {

      String BaseUrl = "http://tkdanr2427.cafe24.com/Gwangju_Univ/";


//    @Multipart
//    @POST("NickLogin.php")
//    Call<ResponseBody>Nick_login(@Part("Nick_Name") RequestBody title,
//                                  @Part("PW") RequestBody pw);



    // Login
    //@FormUrlEncoded
    @GET("retrofit_login.php")
    Call<ResultModel> Nick_login(@Query("Nick_Name") String nick_name,
                                 @Query("PW") String password);


    //닉네임 중복검사
    @GET("Nick_Name_Validate.php")
    Call<ResultModel> NickName_Validate(@Query("Nick_Name") String nick_name);

    //학번 중복검사
    @GET("Student_Id_Validate.php")
    Call<ResultModel> StudentId_Validate(@Query("Student_Id") String student_Id);



    //닉네임 회원가입
    @GET("NickName_Register.php")
    Call<ResultModel> NickName_Register(@Query("User_Name") String user_name,
                                        @Query("User_Phone") String user_phone,
                                        @Query("Nick_Name") String nick_name,
                                        @Query("PW") String password,
                                        @Query("Student_Id") String student_id,
                                        @Query("Department") String department,
                                        @Query("rgs_date") String rgs_date,
                                        @Query("UUID") String uuid);


    @GET("retrofit_login.php")
    Call<ResultModel> Nick_loginLog(@Query("Nick_Name") String nick_name,
                                    @Query("PW") String password,
                                    @Query("LastedDate") String lastdate);

    // 메인_최근로그인일시 조회
    @POST("LastedLogin.php")
    Call<List<ResultModel>> getLastLoginDate(@Query("Nick_Name") String nick_name);


}
*/
