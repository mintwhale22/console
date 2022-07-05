package kr.mintwhale.console.data.model

import com.google.gson.annotations.SerializedName

data class Notice(
    @SerializedName("seq") var intSeq : Int? = null,
    @SerializedName("title") var strTitle : String? = null,
    @SerializedName("contents") var txtContents : String? = null,
    @SerializedName("status") var intStatus : Int? = null,
    @SerializedName("reg") var dateReg : String? = null,
    @SerializedName("edit") var dateEdit : String? = null
)
