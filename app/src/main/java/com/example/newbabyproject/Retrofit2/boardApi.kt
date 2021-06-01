package com.example.newbabyproject.Retrofit2

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface boardApi {

    /* 공지사항 목록 */
    @POST("getNoticeList.php")
    fun getNoticeList() : Call<ResultModel>

    /* 소개문 내용*/
    @Multipart
    @POST("getIntroduce.php")
    fun getIntroduce(@Part("boardGubun") boardGubun : RequestBody) : Call<ResultModel>


    /* 소개문 등록*/
    @Multipart
    @POST("introduce_insert.php")
    fun getIntroduceInsert(@Part("boardGubun") boardGubun : RequestBody,
                           @Part("boardContent") boardContent : RequestBody,
                           @Part("insertId") insertId : RequestBody,
                           @Part("insertDate") insertDate : RequestBody,
                           @Part("updateId") updateId : RequestBody,
                           @Part("updateDate") updateDate : RequestBody) : Call<ResultModel>

    /* 소개문 수정*/
    @Multipart
    @POST("introduce_modify.php")
    fun getIntroduceModify(@Part("boardGubun") boardGubun : RequestBody,
                           @Part("boardContent") boardContent : RequestBody,
                           @Part("updateId") updateId : RequestBody,
                           @Part("updateDate") updateDate : RequestBody) : Call<ResultModel>


    /* 소개문 등록 / 수정 여부  */
    @Multipart
    @POST("introduce_validate.php")
    fun getIntroduceValidate(@Part("boardGubun") boardGubun: RequestBody) : Call<ResultModel>


    /* 소개문 List*/
    @POST("introduceList.php")
    fun getIntroduceList() : Call<List<ResultIntroduce>>


}