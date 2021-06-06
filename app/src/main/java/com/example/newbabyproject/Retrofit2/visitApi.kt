package com.example.newbabyproject.Retrofit2

import com.example.newbabyproject.Visit.ResultVisit
import retrofit2.Call
import retrofit2.http.POST

interface visitApi {



    /* 소개문 List*/
    @POST("visit_admin_userList.php")
    fun VisitAdminUserList() : Call<List<ResultVisit>>


}