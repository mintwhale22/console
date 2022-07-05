package kr.mintwhale.console.service

import kr.mintwhale.console.data.model.ListSearch
import kr.mintwhale.console.data.model.Notice
import kr.mintwhale.console.data.model.ResultList
import kr.mintwhale.console.mapper.dao.NoticeMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NoticeService {

    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    lateinit var noticeMapper: NoticeMapper

    @Transactional
    fun add(data: Notice): Boolean {
        return noticeMapper.setNotice(data)
    }

    fun list(data: ListSearch) : ResultList<Notice> {
        val list = ResultList<Notice>()
        list.list = noticeMapper.getNotice(data)
        list.totalcount = noticeMapper.getNoticeCount(data)
        return list
    }

    @Transactional
    fun edit(data: Notice): Boolean {
        return noticeMapper.editNotice(data)
    }

    @Transactional
    fun del(data: Notice): Boolean {
        return noticeMapper.delNotice(data)
    }
}