package kr.mintwhale.console.controller

import kr.mintwhale.console.config.DefaultConfig
import kr.mintwhale.console.data.model.Member
import kr.mintwhale.console.data.model.ReturnValue
import kr.mintwhale.console.service.MemberService
import kr.mintwhale.console.util.Token
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = ["/api/v1"])
class APIController {

    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    lateinit var memberService: MemberService

    @RequestMapping(value = ["/login"], produces = ["application/json"], method = [RequestMethod.POST])
    @ResponseBody
    fun login(@RequestBody data: Member, request: HttpServletRequest): Any {
        val rtnValue = ReturnValue()

        if(data.strAID.isNullOrEmpty() && data.strGID.isNullOrEmpty() && data.strKID.isNullOrEmpty() && data.strNID.isNullOrEmpty() && data.strFID.isNullOrEmpty()) {
            if(rtnValue.status == DefaultConfig.SERVER_SUCCESS && data.strEmail.isNullOrEmpty()) {

            }
            if(rtnValue.status == DefaultConfig.SERVER_SUCCESS && data.strPassword.isNullOrEmpty()) {

            }
        }

        if(rtnValue.status == DefaultConfig.SERVER_SUCCESS) {

        }

        return rtnValue
    }


}