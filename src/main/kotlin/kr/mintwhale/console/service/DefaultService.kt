package kr.mintwhale.console.service

import kr.mintwhale.console.mapper.dao.DefaultMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DefaultService {

    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    lateinit var defaultMapper: DefaultMapper

    fun getPing() : String {
        return defaultMapper.getPing()
    }

}