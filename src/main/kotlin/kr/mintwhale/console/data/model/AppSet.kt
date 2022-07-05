package kr.mintwhale.console.data.model

import com.google.gson.annotations.SerializedName

data class AppSet(
    @SerializedName("seq") var intSeq : Int? = null,
    @SerializedName("type") var intType : Int? = null,
    @SerializedName("iver") var intVersion : Int? = null,
    @SerializedName("sver") var strVersion : String? = null,
    @SerializedName("download") var strDownloadURL : String? = null,
    @SerializedName("message") var txtMessage : String? = null,
    @SerializedName("update") var intUpdate : Int? = null,
    @SerializedName("status") var intStatus : Int? = null,
    @SerializedName("reg") var dateReg : String? = null,
    @SerializedName("edit") var dateEdit : String? = null
)
