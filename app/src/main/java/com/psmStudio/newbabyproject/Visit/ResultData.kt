package com.psmStudio.newbabyproject.Visit

import java.io.Serializable

class ResultData(
    var result : String
) : Serializable {

    override fun toString(): String {
        return "ResultData(result='$result')"
    }
}