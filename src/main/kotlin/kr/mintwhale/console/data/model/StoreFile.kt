package kr.mintwhale.console.data.model

import com.google.gson.annotations.SerializedName

data class StoreFile(
    @SerializedName("seq") var intSeq : Long? = null,
    @SerializedName("sseq") var intSSeq : Int? = null,
    @SerializedName("mseq") var intMSeq : Int? = null,
    @SerializedName("type") var intType : Int? = null,
    @SerializedName("url") var strURL : String? = null,
    @SerializedName("name") var strFileName : String? = null,
    @SerializedName("sort") var intSort : Int? = null,
    @SerializedName("date") var dateReg : String? = null
)
