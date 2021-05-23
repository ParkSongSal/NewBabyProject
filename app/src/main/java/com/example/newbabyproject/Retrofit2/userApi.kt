package com.example.newbabyproject.Retrofit2

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface userApi {

    //var BASE_URL: String = "http://tkdanr2427.cafe24.com/NewBaby"

    @POST("retrofit_login.php")
    //@POST("user_login.php")
    fun userLogin(@Query("userId") userId : String,
                  @Query("userPw") userPw : String) : Call<ResultModel>

}