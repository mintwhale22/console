package kr.mintwhale.console.controller

import kr.mintwhale.console.config.DefaultConfig
import kr.mintwhale.console.data.model.AppSet
import kr.mintwhale.console.data.model.ListSearch
import kr.mintwhale.console.data.model.Member
import kr.mintwhale.console.data.model.ReturnValue
import kr.mintwhale.console.service.DefaultService
import kr.mintwhale.console.service.MemberService
import kr.mintwhale.console.util.Etc
import kr.mintwhale.console.util.Token
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = [DefaultConfig.LINK_API])
class DefaultController {

    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    lateinit var memberService: MemberService

    @Autowired
    lateinit var defaultService: DefaultService

    @RequestMapping(value = ["/ping"])
    @ResponseBody
    @Throws(Exception::class)
    fun ping() : Any {
        return defaultService.getPing()
    }

    @RequestMapping(value = ["/login"], produces = ["application/json"], method = [RequestMethod.POST])
    @ResponseBody
    fun login(@RequestBody data: Member, request: HttpServletRequest): Any {
        val rtnValue = ReturnValue()

        if(data.strAID.isNullOrEmpty() && data.strGID.isNullOrEmpty() && data.strKID.isNullOrEmpty() && data.strNID.isNullOrEmpty() && data.strFID.isNullOrEmpty()) {
            if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && data.strEmail.isNullOrEmpty()) {
                rtnValue.status = DefaultConfig.STATUS_PARAMERROR
                rtnValue.message = DefaultConfig.MESSAGE_EMPTY_EMAIL
            }
            if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && data.strPassword.isNullOrEmpty()) {
                rtnValue.status = DefaultConfig.STATUS_PARAMERROR
                rtnValue.message = DefaultConfig.MESSAGE_EMPTY_PASS
            }
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS) {
            val result = memberService.info(data)
            if(result != null) {
                if(result.strPassword != result.strPassword2) {
                    rtnValue.status = DefaultConfig.STATUS_NOTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_NOTMATCH_PASS
                } else {
                    rtnValue.result = Token.make(result)
                }
            } else {
                rtnValue.status = DefaultConfig.STATUS_NOTUSER
                rtnValue.message = DefaultConfig.MESSAGE_EMPTY_MEMBER
            }
        }

        return rtnValue
    }

    @RequestMapping(value = ["/info"], produces = ["application/json"], method = [RequestMethod.POST, RequestMethod.GET])
    @ResponseBody
    fun info(@RequestHeader(value = DefaultConfig.TOKEN_HEADER) token: String?, request: HttpServletRequest): Any {
        val rtnValue = ReturnValue()

        log.debug(token)

        if (rtnValue.status == DefaultConfig.STATUS_SUCCESS && token.isNullOrEmpty()) {
            rtnValue.status = DefaultConfig.STATUS_LOGOUT
            rtnValue.message = DefaultConfig.MESSAGE_LOGOUT
        } else {
            val tdata = Token.get(token.toString())
            if (tdata == null) {
                rtnValue.status = DefaultConfig.STATUS_LOGOUT
                rtnValue.message = DefaultConfig.MESSAGE_LOGOUT
            }

            val member = Member()
            member.intType = 3
            member.strEmail = tdata?.strEmail.toString()

            val result = memberService.info(member)

            if(result == null) {
                rtnValue.status = DefaultConfig.STATUS_LOGOUT
                rtnValue.message = DefaultConfig.MESSAGE_LOGOUT
            } else {
                if(result.intStatus == DefaultConfig.MEMBER_CUT) {
                    rtnValue.status = DefaultConfig.STATUS_CUTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_CUT_USER
                } else if(result.intStatus == DefaultConfig.MEMBER_REJECT) {
                    rtnValue.status = DefaultConfig.STATUS_REJECTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_REJECT_USER
                } else {
                    result.strPassword = null
                    result.intSeq = null
                    result.intStatus = null
                    result.intLevel = null
                    result.intType = null
                    result.strFID = if (!result.strFID.isNullOrEmpty()) {
                        "true"
                    } else {
                        "false"
                    }
                    result.strAID = if (!result.strAID.isNullOrEmpty()) {
                        "true"
                    } else {
                        "false"
                    }
                    result.strGID = if (!result.strGID.isNullOrEmpty()) {
                        "true"
                    } else {
                        "false"
                    }
                    result.strNID = if (!result.strNID.isNullOrEmpty()) {
                        "true"
                    } else {
                        "false"
                    }
                    result.strKID = if (!result.strKID.isNullOrEmpty()) {
                        "true"
                    } else {
                        "false"
                    }

                    rtnValue.result = result
                }
            }
        }

        return rtnValue
    }

    @RequestMapping(value = ["/edit"], produces = ["application/json"], method = [RequestMethod.POST])
    @ResponseBody
    @Transactional
    @Throws(Exception::class)
    fun edit(@RequestHeader(value = DefaultConfig.TOKEN_HEADER) token: String?, @RequestBody data: Member, request: HttpServletRequest): Any {
        val rtnValue = ReturnValue()
        var email = ""

        if (rtnValue.status == DefaultConfig.STATUS_SUCCESS && token.isNullOrEmpty()) {
            rtnValue.status = DefaultConfig.STATUS_LOGOUT
            rtnValue.message = DefaultConfig.MESSAGE_LOGOUT
        } else {
            val tdata = Token.get(token.toString())
            if (tdata == null) {
                rtnValue.status = DefaultConfig.STATUS_LOGOUT
                rtnValue.message = DefaultConfig.MESSAGE_LOGOUT
            }

            val member = Member()
            member.intType = 3
            member.strEmail = tdata?.strEmail.toString()
            email = tdata?.strEmail.toString()

            val rinfo = memberService.info(member)

            if (rinfo == null) {
                rtnValue.status = DefaultConfig.STATUS_LOGOUT
                rtnValue.message = DefaultConfig.MESSAGE_LOGOUT
            } else {
                if (rinfo.intStatus == DefaultConfig.MEMBER_CUT) {
                    rtnValue.status = DefaultConfig.STATUS_CUTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_CUT_USER
                } else if(rinfo.intStatus == DefaultConfig.MEMBER_REJECT) {
                    rtnValue.status = DefaultConfig.STATUS_REJECTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_REJECT_USER
                } else {
                    data.intSeq = rinfo.intSeq
                }
            }
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && data.strEmail.isNullOrEmpty()) {
            rtnValue.status = DefaultConfig.STATUS_PARAMERROR
            rtnValue.message = DefaultConfig.MESSAGE_EMPTY_EMAIL
        }
        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && !Etc.checkEmail(data.strEmail.toString())) {
            rtnValue.status = DefaultConfig.STATUS_PARAMERROR
            rtnValue.message = DefaultConfig.MESSAGE_NOTMATCH_EMAIL
        }
        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && data.strName.isNullOrEmpty()) {
            rtnValue.status = DefaultConfig.STATUS_PARAMERROR
            rtnValue.message = DefaultConfig.MESSAGE_EMPTY_NAME
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS) {
            if(memberService.edit(data) != true) {
                rtnValue.status = DefaultConfig.STATUS_DBERROR
                rtnValue.message = DefaultConfig.MESSAGE_SERVER_ERROR
            } else {
                val member = Member()
                member.intType = 3
                member.strEmail = if(data.strEmail.isNullOrEmpty()) { email } else { data.strEmail.toString() }
                val rinfo2 = memberService.info(member)
                if(rinfo2 != null) {
                    rtnValue.result = Token.make(rinfo2)
                }
            }
        }

        return rtnValue
    }

    @RequestMapping(value = ["/appset/list"], produces = ["application/json"], method = [RequestMethod.POST])
    @ResponseBody
    @Transactional
    @Throws(Exception::class)
    fun appsetlist(@RequestHeader(value = DefaultConfig.TOKEN_HEADER) token: String?, @RequestBody data: AppSet, request: HttpServletRequest): Any {
        val rtnValue = ReturnValue()

        if (rtnValue.status == DefaultConfig.STATUS_SUCCESS && token.isNullOrEmpty()) {
            rtnValue.status = DefaultConfig.STATUS_LOGOUT
            rtnValue.message = DefaultConfig.MESSAGE_LOGOUT
        } else {
            val tdata = Token.get(token.toString())
            if (tdata == null) {
                rtnValue.status = DefaultConfig.STATUS_LOGOUT
                rtnValue.message = DefaultConfig.MESSAGE_LOGOUT
            }

            val member = Member()
            member.intType = 3
            member.strEmail = tdata?.strEmail.toString()

            val rinfo = memberService.info(member)

            if (rinfo == null) {
                rtnValue.status = DefaultConfig.STATUS_LOGOUT
                rtnValue.message = DefaultConfig.MESSAGE_LOGOUT
            } else {
                if (rinfo.intStatus == DefaultConfig.MEMBER_CUT) {
                    rtnValue.status = DefaultConfig.STATUS_CUTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_CUT_USER
                } else if(rinfo.intStatus == DefaultConfig.MEMBER_REJECT) {
                    rtnValue.status = DefaultConfig.STATUS_REJECTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_REJECT_USER
                }
            }
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS) {
            val ls = ListSearch()
            ls.search = data
            rtnValue.result = defaultService.getVersion(ls)
        }

        return rtnValue
    }

    @RequestMapping(value = ["/appset/edit"], produces = ["application/json"], method = [RequestMethod.POST])
    @ResponseBody
    @Throws(Exception::class)
    fun appsetedit(@RequestHeader(value = DefaultConfig.TOKEN_HEADER) token: String?, @RequestBody data: AppSet, request: HttpServletRequest): Any {
        val rtnValue = ReturnValue()

        if (rtnValue.status == DefaultConfig.STATUS_SUCCESS && token.isNullOrEmpty()) {
            rtnValue.status = DefaultConfig.STATUS_LOGOUT
            rtnValue.message = DefaultConfig.MESSAGE_LOGOUT
        } else {
            val tdata = Token.get(token.toString())
            if (tdata == null) {
                rtnValue.status = DefaultConfig.STATUS_LOGOUT
                rtnValue.message = DefaultConfig.MESSAGE_LOGOUT
            }

            val member = Member()
            member.intType = 3
            member.strEmail = tdata?.strEmail.toString()

            val rinfo = memberService.info(member)

            if (rinfo == null) {
                rtnValue.status = DefaultConfig.STATUS_LOGOUT
                rtnValue.message = DefaultConfig.MESSAGE_LOGOUT
            } else {
                if (rinfo.intStatus == DefaultConfig.MEMBER_CUT) {
                    rtnValue.status = DefaultConfig.STATUS_CUTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_CUT_USER
                } else if(rinfo.intStatus == DefaultConfig.MEMBER_REJECT) {
                    rtnValue.status = DefaultConfig.STATUS_REJECTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_REJECT_USER
                }
            }
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && data.intVersion == null) {
            rtnValue.status = DefaultConfig.STATUS_PARAMERROR
            rtnValue.message = DefaultConfig.MESSAGE_EMPTY_VERCODE
        }
        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && data.strVersion.isNullOrEmpty()) {
            rtnValue.status = DefaultConfig.STATUS_PARAMERROR
            rtnValue.message = DefaultConfig.MESSAGE_EMPTY_VER
        }
        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && data.strDownloadURL.isNullOrEmpty()) {
            rtnValue.status = DefaultConfig.STATUS_PARAMERROR
            rtnValue.message = DefaultConfig.MESSAGE_EMPTY_DOWNLOAD
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS) {
            if(defaultService.editVersion(data)) {
                rtnValue.result = data.intType
            } else {
                rtnValue.status = DefaultConfig.STATUS_DBERROR
                rtnValue.message = DefaultConfig.MESSAGE_SERVER_ERROR
            }
        }

        return rtnValue
    }


}