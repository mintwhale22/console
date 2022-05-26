package kr.mintwhale.console.controller

import kr.mintwhale.console.config.DefaultConfig
import kr.mintwhale.console.data.model.ReturnValue
import kr.mintwhale.console.service.S3Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@RestController
@RequestMapping(value = ["/file"])
class FileController {

    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    lateinit var s3Service: S3Service

    @RequestMapping(value = ["/upload"], method = [RequestMethod.POST])
    @Throws(IOException::class)
    fun upload(@RequestPart("images") multipartFile: MultipartFile?): Any {
        val rtnValue = ReturnValue()

        try {
            rtnValue.result = s3Service.upload(multipartFile!!, "source/")

        } catch (e: Exception) {
            log.error(e.message)
            rtnValue.error = DefaultConfig.ERROR_PROCESS
            rtnValue.status = DefaultConfig.SERVER_NULL
            rtnValue.message = DefaultConfig.MESSAGE_SERVER_ERROR
        }

        return rtnValue
    }

}