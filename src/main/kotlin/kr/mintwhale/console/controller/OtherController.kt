package kr.mintwhale.console.controller

import kr.mintwhale.console.config.DefaultConfig
import kr.mintwhale.console.data.model.Member
import kr.mintwhale.console.data.model.ReturnValue
import kr.mintwhale.console.service.MemberService
import kr.mintwhale.console.util.Token
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.collections.HashMap


@RestController
@RequestMapping(value = [DefaultConfig.LINK_API + "/out"])
class OtherController {
    var log = LoggerFactory.getLogger(this::class.java) as Logger
    val restTemplate = RestTemplate()
    val gps = HashMap<String, Any>()

    @Autowired
    lateinit var memberService: MemberService

    @RequestMapping(value = ["/a2g/{address:.+}"], produces = ["application/json"], method = [RequestMethod.GET])
    @ResponseBody
    @Transactional
    @Throws(Exception::class)
    fun edit(@RequestHeader(value = DefaultConfig.TOKEN_HEADER) token: String?, @PathVariable("address") address: String, request: HttpServletRequest): Any {
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

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS) {
            val result = kakaoGeo(address)
            if(result == null) {
                val result2 = naverGeo(address)
                if(result2 == null) {
                    val result3 = googleGeo(address)
                    if(result3 == null) {
                        rtnValue.status = DefaultConfig.STATUS_NOTFOUND
                        rtnValue.message = DefaultConfig.MESSAGE_NODATA_GPS
                    } else {
                        rtnValue.result = result3
                    }
                } else {
                    rtnValue.result = result2
                }
            } else {
                rtnValue.result = result
            }
        }

        return rtnValue
    }

    fun googleGeo(address: String) : HashMap<String, Any>? {

        var url = "https://maps.googleapis.com/maps/api/geocode/json?key=${DefaultConfig.GEO_GOOGLE_KEY}&language=ko&address=$address"

        return try {
            val result = JSONObject(restTemplate.getForEntity(url, String::class.java).body)
            if(result == null || result.isNull("results") || result.getJSONArray("results").length() <= 0) {
                null
            } else {
                gps["lat"] = (result.getJSONArray("results").get(0) as JSONObject).getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                gps["lng"] = (result.getJSONArray("results").get(0) as JSONObject).getJSONObject("geometry").getJSONObject("location").getDouble("lng")
                gps
            }
        } catch (e: Exception) {
            log.error("google error :: " + e.message)
            null
        }
    }

    fun naverGeo(address: String) : HashMap<String, Any>? {
        val headers = HttpHeaders()
        headers.set("X-NCP-APIGW-API-KEY-ID", DefaultConfig.GEO_NAVER_APPID)
        headers.set("X-NCP-APIGW-API-KEY", DefaultConfig.GEO_NAVER_KEY)

        val requestEntity = HttpEntity("parameters", headers)

        var url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=$address"

        return try {
            val result = JSONObject(restTemplate.exchange(url, HttpMethod.GET, requestEntity, String::class.java).body)
            log.debug(result.toString())
            if(result == null || result.isNull("addresses") || result.getJSONArray("addresses").length() <= 0) {
                null
            } else {
                gps["lat"] = (result.getJSONArray("addresses").get(0) as JSONObject).getDouble("y")
                gps["lng"] = (result.getJSONArray("addresses").get(0) as JSONObject).getDouble("x")
                gps
            }
        } catch (e: Exception) {
            log.error("naver error :: " + e.message)
            null
        }
    }

    fun kakaoGeo(address: String) : HashMap<String, Any>? {

        val headers = HttpHeaders()
        headers.set("Authorization", DefaultConfig.GEO_KAKO_KEY)
        val requestEntity = HttpEntity("parameters", headers)

        var url = "https://dapi.kakao.com/v2/local/search/address.json?query=$address"

        return try {
            val result = JSONObject(restTemplate.exchange(url, HttpMethod.GET, requestEntity, String::class.java).body)
            log.debug(result.toString())
            if(result == null || result.isNull("documents") || result.getJSONArray("documents").length() <= 0) {
                null
            } else {
                gps["lat"] = (result.getJSONArray("documents").get(0) as JSONObject).getDouble("y")
                gps["lng"] = (result.getJSONArray("documents").get(0) as JSONObject).getDouble("x")
                gps
            }
        } catch (e: Exception) {
            log.error("kakao error :: " + e.message)
            null
        }
    }

}


