package com.example.newbabyproject.Visit

import java.io.Serializable

class ResultReply(
    var result : String,
    var seq: Int,
    var userId: String,
    var replyContent: String,
    var insertDate: String
) : Serializable {


    override fun toString(): String {
        return "ResultReply(result='$result', seq=$seq, userId='$userId', replyContent='$replyContent', insertDate='$insertDate')"
    }
}