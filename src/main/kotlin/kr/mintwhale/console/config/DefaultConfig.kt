package kr.mintwhale.console.config

object DefaultConfig {
    const val SERVER_SUCCESS = 200
    const val SERVER_PARAMERROR = 300
    const val SERVER_NOTFOUND = 400
    const val SERVER_APIERROR = 500
    const val SERVER_NOTUSER = 600
    const val SERVER_LOGOUT = 699
    const val SERVER_DBERROR = 800
    const val SERVER_NULL = 900

    const val ERROR_PARAM = "param error"
    const val ERROR_NOTFOUND = "not found"
    const val ERROR_APIERROR = "api error"
    const val ERROR_NOTUSER = "not user"
    const val ERROR_LOGOUT = "logout"
    const val ERROR_DBERROR = "database error"
    const val ERROR_NULL = "null"
    const val ERROR_PROCESS = "server error"

    const val MESSAGE_OK = "정상처리 되었습니다."
    const val MESSAGE_SERVER_ERROR = "서버 에러가 발생하였습니다."

    const val TOKEN_ISSUER = "mintwhale"
    const val TOKEN_KEY = "akjdlkjaixxkskdf"
}