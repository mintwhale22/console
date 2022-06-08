package kr.mintwhale.console.controller

import kr.mintwhale.console.config.DefaultConfig
import kr.mintwhale.console.data.model.Member
import kr.mintwhale.console.data.model.ReturnValue
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
@RequestMapping(value = ["${DefaultConfig.LINK_API}/member"])
class MemberContorller {
    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    lateinit var memberService: MemberService

    @RequestMapping(value = ["/add"], produces = ["application/json"], method = [RequestMethod.POST])
    @ResponseBody
    @Transactional
    @Throws(Exception::class)
    fun add(@RequestHeader(value = DefaultConfig.TOKEN_HEADER) token: String?, @RequestBody data: Member, request: HttpServletRequest): Any {
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
                } else if (rinfo.intStatus == DefaultConfig.MEMBER_CUT) {
                    rtnValue.status = DefaultConfig.STATUS_CUTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_CUT_USER
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
        } else {
            val member2 = Member()
            member2.intType = data.intType
            member2.strEmail = data.strEmail.toString()

            val rinfo2 = memberService.info(member2)
            if(rinfo2 != null) {
                rtnValue.status = DefaultConfig.STATUS_PARAMERROR
                rtnValue.message = DefaultConfig.MESSAGE_REJOIN_EMAIL
            }
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && data.strPassword.isNullOrEmpty()) {
            rtnValue.status = DefaultConfig.STATUS_PARAMERROR
            rtnValue.message = DefaultConfig.MESSAGE_EMPTY_EMAIL
        }
        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && !Etc.checkPassword(data.strPassword.toString())) {
            rtnValue.status = DefaultConfig.STATUS_PARAMERROR
            rtnValue.message = DefaultConfig.MESSAGE_NOTMATCH_PASSTYPE
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS && data.strName.isNullOrEmpty()) {
            rtnValue.status = DefaultConfig.STATUS_PARAMERROR
            rtnValue.message = DefaultConfig.MESSAGE_EMPTY_NAME
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS) {
            val result = memberService.add(data)
        }

        return rtnValue
    }

}