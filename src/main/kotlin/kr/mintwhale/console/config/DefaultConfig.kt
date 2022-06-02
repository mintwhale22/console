package kr.mintwhale.console.config

object DefaultConfig {
    const val STATUS_SUCCESS = 200
    const val STATUS_PARAMERROR = 300
    const val STATUS_NOTFOUND = 400
    const val STATUS_APIERROR = 500
    const val STATUS_NOTUSER = 600
    const val STATUS_CUTUSER = 610
    const val STATUS_REJECTUSER = 611
    const val STATUS_LOGOUT = 699
    const val STATUS_DBERROR = 800
    const val STATUS_NULL = 900

    const val MESSAGE_OK = "정상처리 되었습니다."
    const val MESSAGE_SERVER_ERROR = "서버 에러가 발생하였습니다."
    const val MESSAGE_LOGOUT = "로그아웃 되었습니다."
    const val MESSAGE_EMPTY_EMAIL = "이메일을 입력해주세요."
    const val MESSAGE_EMPTY_PASS = "비밀번호를 입력해주세요."
    const val MESSAGE_EMPTY_MEMBER = "회원정보가 없습니다."
    const val MESSAGE_NOTMATCH_PASS = "비밀번호 정보가 다릅니다."
    const val MESSAGE_CUT_USER = "로그인이 제한되었습니다."
    const val MESSAGE_REJECT_USER = "탈퇴한 회원입니다."

    const val TOKEN_HEADER = "mint-token"
    const val TOKEN_ISSUER = "mintwhale"
    const val TOKEN_KEY = "akjdlkjaixxkskdf"

    const val MEMBER_USE    = 1
    const val MEMBER_CUT    = 5
    const val MEMBER_REJECT = 9


}