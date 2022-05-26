package kr.mintwhale.console.data.model

import kr.mintwhale.console.config.DefaultConfig

data class ReturnValue(
    var status: Int = DefaultConfig.SERVER_SUCCESS,
    var error : String = "",
    var message : String = DefaultConfig.MESSAGE_OK,
    var result: Any? = null
)
