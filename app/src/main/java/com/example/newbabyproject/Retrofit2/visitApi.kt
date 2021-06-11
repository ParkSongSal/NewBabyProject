package com.example.newbabyproject.Retrofit2

import com.example.newbabyproject.Visit.ResultVisit
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface visitApi {



    /* 소개문 List*/
    @POST("visit_admin_userList.php")
    fun VisitAdminUserList() : Call<List<ResultVisit>>


    /* 소개문 등록*/
    @Multipart
    @POST("to_parent_board_insert.php")
    fun toParentInsert(@Part("parentId") parentId : RequestBody,
                       @Part("parentName") parentName : RequestBody,
                       @Part("visitNotice") visitNotice : RequestBody,
                       @Part("babyWeight") babyWeight : RequestBody,
                       @Part("babyLactation") babyLactation : RequestBody,
                       @Part("babyRequireItem") babyRequireItem : RequestBody,
                       @Part("babyEtc") babyEtc : RequestBody,
                       @Part("writeDate") writeDate : RequestBody,
                       @Part("insertId") insertId : RequestBody,
                       @Part("insertDate") insertDate : RequestBody,
                       @Part("updateId") updateId : RequestBody,
                       @Part("updateDate") updateDate : RequestBody
    ) : Call<ResultVisit>
}