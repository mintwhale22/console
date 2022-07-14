package kr.mintwhale.console.controller

import com.google.gson.Gson
import kr.mintwhale.console.config.DefaultConfig
import kr.mintwhale.console.data.model.ListSearch
import kr.mintwhale.console.data.model.Member
import kr.mintwhale.console.data.model.Notice
import kr.mintwhale.console.data.model.ReturnValue
import kr.mintwhale.console.service.MemberService
import kr.mintwhale.console.service.NoticeService
import kr.mintwhale.console.util.Token
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = ["${DefaultConfig.LINK_API}/notice"])
class NoticeController {

    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    lateinit var memberService: MemberService

    @Autowired
    lateinit var noticeService: NoticeService

    @RequestMapping(value = ["/add"], produces = ["application/json"], method = [RequestMethod.POST])
    @ResponseBody
    @Throws(Exception::class)
    fun add(@RequestHeader(value = DefaultConfig.TOKEN_HEADER) token: String?,
            @RequestBody data: Notice,
            request: HttpServletRequest
    ): Any {
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
                } else if (rinfo.intStatus == DefaultConfig.MEMBER_REJECT) {
                    rtnValue.status = DefaultConfig.STATUS_REJECTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_REJECT_USER
                }
            }
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && data.strTitle.isNullOrEmpty()) {
            rtnValue.status = DefaultConfig.STATUS_PARAMERROR
            rtnValue.message = DefaultConfig.MESSAGE_EMPTY_TITLE
        }
        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && data.txtContents.isNullOrEmpty()) {
            rtnValue.status = DefaultConfig.STATUS_PARAMERROR
            rtnValue.message = DefaultConfig.MESSAGE_EMPTY_CONTENTS
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS) {
            if(noticeService.add(data)) {
                rtnValue.result = data.intSeq
            } else {
                rtnValue.status = DefaultConfig.STATUS_DBERROR
                rtnValue.message = DefaultConfig.MESSAGE_SERVER_ERROR
            }
        }

        return rtnValue
    }

    @RequestMapping(value = ["/edit"], produces = ["application/json"], method = [RequestMethod.POST])
    @ResponseBody
    @Throws(Exception::class)
    fun edit(@RequestHeader(value = DefaultConfig.TOKEN_HEADER) token: String?,
            @RequestBody data: Notice,
            request: HttpServletRequest
    ): Any {
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
                } else if (rinfo.intStatus == DefaultConfig.MEMBER_REJECT) {
                    rtnValue.status = DefaultConfig.STATUS_REJECTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_REJECT_USER
                }
            }
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && data.strTitle.isNullOrEmpty()) {
            rtnValue.status = DefaultConfig.STATUS_PARAMERROR
            rtnValue.message = DefaultConfig.MESSAGE_EMPTY_TITLE
        }
        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && data.txtContents.isNullOrEmpty()) {
            rtnValue.status = DefaultConfig.STATUS_PARAMERROR
            rtnValue.message = DefaultConfig.MESSAGE_EMPTY_CONTENTS
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS) {
            if(noticeService.edit(data)) {
                rtnValue.result = data.intSeq
            } else {
                rtnValue.status = DefaultConfig.STATUS_DBERROR
                rtnValue.message = DefaultConfig.MESSAGE_SERVER_ERROR
            }
        }

        return rtnValue
    }

    @RequestMapping(value = ["/list"], produces = ["application/json"], method = [RequestMethod.POST])
    @ResponseBody
    @Throws(Exception::class)
    fun list (
        @RequestHeader(value = DefaultConfig.TOKEN_HEADER) token: String?,
        @RequestBody data: ListSearch,
        request: HttpServletRequest
    ): Any {
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
                } else if (rinfo.intStatus == DefaultConfig.MEMBER_REJECT) {
                    rtnValue.status = DefaultConfig.STATUS_REJECTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_REJECT_USER
                }
            }
        }

        val gson = Gson()
        data.search = gson.fromJson(gson.toJson(data.search), Notice::class.java)

        rtnValue.result = noticeService.list(data)

        return rtnValue
    }


    @RequestMapping(value = ["/info"], produces = ["application/json"], method = [RequestMethod.POST])
    @ResponseBody
    @Throws(Exception::class)
    fun info (
        @RequestHeader(value = DefaultConfig.TOKEN_HEADER) token: String?,
        @RequestBody data: Notice,
        request: HttpServletRequest
    ): Any {
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
                } else if (rinfo.intStatus == DefaultConfig.MEMBER_REJECT) {
                    rtnValue.status = DefaultConfig.STATUS_REJECTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_REJECT_USER
                }
            }
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS) {
            rtnValue.result = noticeService.info(data)
        }

        return rtnValue
    }

}