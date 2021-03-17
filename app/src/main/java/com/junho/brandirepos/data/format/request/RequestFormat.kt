package com.junho.brandirepos.data.format.request

import com.google.gson.annotations.SerializedName

data class ReqHeader(
    @SerializedName("CmdType") var CmdType: String,
)