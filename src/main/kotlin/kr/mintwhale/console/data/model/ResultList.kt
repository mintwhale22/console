package kr.mintwhale.console.data.model

data class ResultList<T>(
    var list : ArrayList<T>? = null,
    var totalcount: Int = 0
)
