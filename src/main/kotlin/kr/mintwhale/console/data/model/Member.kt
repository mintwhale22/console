package kr.mintwhale.console.data.model

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("seq") var intSeq : Int? = null,
    @SerializedName("type") var intType : Int? = null,
    @SerializedName("email") var strEmail : String? = null,
    @SerializedName("pass") var strPassword : String? = null,
    @SerializedName("pass2") var strPassword2 : String? = null,
    @SerializedName("name") var strName : String? = null,
    @SerializedName("nik") var strNikname : String? = null,
    @SerializedName("sex") var intSex : Int? = null,
    @SerializedName("birth") var dateBirth : String? = null,
    @SerializedName("job") var intJob : Int? = null,
    @SerializedName("level") var intLevel : Int? = null,
    @SerializedName("status") var intStatus : Int? = null,
    @SerializedName("fid") var strFID : String? = null,
    @SerializedName("gid") var strGID : String? = null,
    @SerializedName("aid") var strAID : String? = null,
    @SerializedName("nid") var strNID : String? = null,
    @SerializedName("kid") var strKID : String? = null
)
