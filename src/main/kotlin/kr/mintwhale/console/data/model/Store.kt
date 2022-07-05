package kr.mintwhale.console.data.model

import com.google.gson.annotations.SerializedName

data class Store(
    @SerializedName("seq") var intSeq : Int? = null,
    @SerializedName("mseq") var intMSeq : Int? = null,
    @SerializedName("stype") var intStoreType : Int? = null,
    @SerializedName("sname") var strStoreName : String? = null,
    @SerializedName("sinfo") var txtStoreInfo: String? = null,
    @SerializedName("status") var intStatus: Int? = null,
    @SerializedName("stel") var strStoreTel: String? = null,
    @SerializedName("zipcode") var strZipcode: String? = null,
    @SerializedName("addr1") var strAddress1: String? = null,
    @SerializedName("addr2") var strAddress2: String? = null,
    @SerializedName("lat") var dwLat: Double? = null,
    @SerializedName("lng") var dwLng: Double? = null,
    @SerializedName("reg") var dateReg: String? = null,
    @SerializedName("edit") var dateEdit: String? = null,
    @SerializedName("sale1") var strSale1: String? = null,
    @SerializedName("sale2") var strSale2: String? = null,
    @SerializedName("sale3") var strSale3: String? = null,
    @SerializedName("sale4") var strSale4: String? = null,
    @SerializedName("sale5") var strSale5: String? = null,
    @SerializedName("files") var arrFiles: ArrayList<StoreFile>? = null
)