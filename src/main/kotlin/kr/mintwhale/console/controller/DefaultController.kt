package kr.mintwhale.console.controller

import kr.mintwhale.console.service.DefaultService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DefaultController {

    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    lateinit var defaultService: DefaultService

    @RequestMapping(value = ["/ping"])
    @ResponseBody
    @Throws(Exception::class)
    fun ping() : Any {
        return defaultService.getPing()
    }
}