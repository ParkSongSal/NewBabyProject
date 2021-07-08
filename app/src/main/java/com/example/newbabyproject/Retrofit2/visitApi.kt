package com.example.newbabyproject.Retrofit2

import com.example.newbabyproject.Notice.ResultNotice
import com.example.newbabyproject.Visit.ResultVisit
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface visitApi {



    /* 소개문 List*/
    @POST("visit_admin_userList.php")
    fun VisitAdminUserList() : Call<List<ResultVisit>>


    /* 관리자To보호자 게시글 등록*/
    @Multipart
    @POST("to_parent_board_insert.php")
    fun toParentInsert(@Part("parentId") parentId: RequestBody,
                       @Part("parentName") parentName: RequestBody,
                       @Part("visitNotice") visitNotice: RequestBody,
                       @Part("babyWeight") babyWeight: RequestBody,
                       @Part("babyLactation") babyLactation: RequestBody,
                       @Part("babyRequireItem") babyRequireItem: RequestBody,
                       @Part("babyEtc") babyEtc: RequestBody,
                       @Part("writeDate") writeDate: RequestBody,
                       @Part("tempYn") tempYn: RequestBody?,
                       @Part("reserveDate") reserveDate: RequestBody?,
                       @Part image: MultipartBody.Part,
                       @Part image2: MultipartBody.Part?,
                       @Part image3: MultipartBody.Part?,
                       @Part("insertId") insertId: RequestBody,
                       @Part("insertDate") insertDate: RequestBody,
                       @Part("updateId") updateId: RequestBody,
                       @Part("updateDate") updateDate: RequestBody
    ) : Call<ResponseBody>

    /* 관리자To보호자 게시글 등록*/
    @Multipart
    @POST("to_parent_board_insert_NoImage.php")
    fun toParentInsertNoImage(@Part("parentId") parentId : RequestBody,
                              @Part("parentName") parentName : RequestBody,
                              @Part("visitNotice") visitNotice : RequestBody,
                              @Part("babyWeight") babyWeight : RequestBody,
                              @Part("babyLactation") babyLactation : RequestBody,
                              @Part("babyRequireItem") babyRequireItem : RequestBody,
                              @Part("babyEtc") babyEtc : RequestBody,
                              @Part("writeDate") writeDate : RequestBody,
                              @Part("tempYn") tempYn : RequestBody?,
                              @Part("reserveDate") reserveDate : RequestBody?,
                              @Part("insertId") insertId : RequestBody,
                              @Part("insertDate") insertDate : RequestBody,
                              @Part("updateId") updateId : RequestBody,
                              @Part("updateDate") updateDate : RequestBody
    ) : Call<ResponseBody>


    /* 관리자To보호자 게시글 리스트 */
    @Multipart
    @POST("admin_to_parent_board_List.php")
    fun toParentListAdmin(@Part("parentId") parentId : RequestBody) : Call<List<ResultVisit>>

    /* 관리자To보호자 게시글 리스트 */
    @Multipart
    @POST("user_to_parent_board_List.php")
    fun toParentListUser(@Part("parentId") parentId : RequestBody) : Call<List<ResultVisit>>

    /* 관리자가 작성한 글을 보호자가 게시글 확인시 확인표시 Update*/
    @Multipart
    @POST("to_parent_board_confirm_update.php")
    fun to_parent_board_confirm_update(@Part("seq") seq : RequestBody,
                                       @Part("parentId") parentId : RequestBody,
                                       @Part("boardConfirm") boardConfirm : RequestBody,
                                       @Part("boardConfirmDate") boardConfirmDate : RequestBody
    ) : Call<ResultVisit>


    /* 관리자To보호자 게시글 삭제*/
    @Multipart
    @POST("admin_to_parent_board_delete.php")
    fun admin_to_parent_board_delete(@Part("seq") seq : RequestBody) : Call<ResultVisit>
}