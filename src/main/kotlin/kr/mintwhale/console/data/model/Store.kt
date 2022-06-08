package kr.mintwhale.console.data.model

import com.google.gson.annotations.SerializedName

data class Store(
    @SerializedName("seq") var intSeq : Int? = null,
    @SerializedName("mseq") var intMSeq : Int? = null,
    @SerializedName("stype") var intStoreType : Int? = null,
    @SerializedName("sname") var strStoreName : String? = null,
    @SerializedName("sinfo") var txtStoreInfo: String? = null,
    @SerializedName("status") var intStatus: Int? = null,
    @SerializedName("reg") var dateReg: String? = null,
    @SerializedName("edit") var dateEdit: String? = null,
    @SerializedName("files") var arrFiles: ArrayList<StoreFile>? = null
)