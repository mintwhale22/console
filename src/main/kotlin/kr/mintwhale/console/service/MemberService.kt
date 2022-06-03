package kr.mintwhale.console.service

import kr.mintwhale.console.data.model.ListSearch
import kr.mintwhale.console.data.model.Member
import kr.mintwhale.console.mapper.dao.MemberMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MemberService {
    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    lateinit var memberMapper: MemberMapper

    fun info(data: Member): Member? {
        val listSearch = ListSearch()
        listSearch.search = data
        listSearch.limit = 0
        listSearch.length = 1
        return memberMapper.getMember(listSearch)[0]
    }

    fun edit(data: Member): Boolean? {
        return memberMapper.editMember(data)
    }

}