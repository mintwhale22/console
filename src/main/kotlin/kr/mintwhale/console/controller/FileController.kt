package kr.mintwhale.console.controller

import kr.mintwhale.console.config.DefaultConfig
import kr.mintwhale.console.data.model.ReturnValue
import kr.mintwhale.console.data.model.StoreFile
import kr.mintwhale.console.service.S3Service
import kr.mintwhale.console.util.Token
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = ["/file"])
class FileController {

    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    lateinit var s3Service: S3Service

    @RequestMapping(value = ["/upload"], method = [RequestMethod.POST])
    @Throws(IOException::class)
    fun upload(@RequestHeader(value = DefaultConfig.TOKEN_HEADER) token: String?, @RequestPart("file") multipartFile: MultipartFile?, request: HttpServletRequest): Any {
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
        }

        if(rtnValue.status == DefaultConfig.STATUS_SUCCESS) {
            try {
                val storeFile = StoreFile()
                storeFile.intType = if (request.getParameter("type") != null) {
                    request.getParameter("type").toInt()
                } else {
                    null
                }
                storeFile.intSSeq = if (request.getParameter("sseq") != null) {
                    request.getParameter("sseq").toInt()
                } else {
                    null
                }
                storeFile.strURL = s3Service.upload(multipartFile!!, "store/")

            } catch (e: Exception) {
                log.error(e.message)
                rtnValue.status = DefaultConfig.STATUS_NULL
                rtnValue.message = DefaultConfig.MESSAGE_SERVER_ERROR
            }
        }

        return rtnValue
    }

}