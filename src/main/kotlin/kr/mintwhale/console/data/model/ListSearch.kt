package kr.mintwhale.console.data.model

data class ListSearch<T>(
    var search : T? = null,
    var sort : Int? = null,
    var limit : Int? = null,
    var size : Int? = null
)
