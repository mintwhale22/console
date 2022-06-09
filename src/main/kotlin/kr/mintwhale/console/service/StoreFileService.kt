package kr.mintwhale.console.service

import kr.mintwhale.console.data.model.StoreFile
import kr.mintwhale.console.mapper.dao.StoreFileMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StoreFileService {
    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    lateinit var storeFileMapper: StoreFileMapper

    @Transactional
    fun setFile(data: StoreFile) : Boolean? {
        return storeFileMapper.setFile(data)
    }

}