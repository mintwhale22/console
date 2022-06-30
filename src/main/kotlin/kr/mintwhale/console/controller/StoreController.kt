package kr.mintwhale.console.controller

import com.google.gson.Gson
import kr.mintwhale.console.config.DefaultConfig
import kr.mintwhale.console.data.model.ListSearch
import kr.mintwhale.console.data.model.Member
import kr.mintwhale.console.data.model.ReturnValue
import kr.mintwhale.console.data.model.Store
import kr.mintwhale.console.service.MemberService
import kr.mintwhale.console.util.Token
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = ["${DefaultConfig.LINK_API}/store"])
class StoreController {
    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    lateinit var memberService: MemberService


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
                } else if (rinfo.intStatus == DefaultConfig.MEMBER_CUT) {
                    rtnValue.status = DefaultConfig.STATUS_CUTUSER
                    rtnValue.message = DefaultConfig.MESSAGE_CUT_USER
                }
            }
        }

        val gson = Gson()
        data.search = gson.fromJson(gson.toJson(data.search), Store::class.java)

        rtnValue.result = memberService.listStore(data)

        return rtnValue
    }

}