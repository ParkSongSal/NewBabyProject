package com.example.newbabyproject.Retrofit2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface userApi {

    /* 로그인 */
    @POST("user_login.php")
    fun userLogin(@Query("userId") userId : String,
                  @Query("userPw") userPw : String) : Call<ResultModel>

    /* id 중복검사 */
    @GET("user_id_validate.php")
    fun userIdValidate(@Query("userId") userId : String) : Call<ResultModel>


    /* 회원가입 */
    @GET("user_register.php")
    fun userRegister(@Query("userId") userId : String,
                     @Query("userPw") userPw: String,
                     @Query("userName") userName : String,
                     @Query("userPhone") userPhone : String,
                     @Query("babyNum") babyNum : String,
                     @Query("babyRelation") babyRelation : String,
                     @Query("regDate") regDate : String,
                     @Query("userAuth") userAuth : String) : Call<ResultModel>

}