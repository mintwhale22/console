package kr.mintwhale.console.service

import kr.mintwhale.console.data.model.*
import kr.mintwhale.console.mapper.dao.MemberMapper
import kr.mintwhale.console.mapper.dao.StoreFileMapper
import kr.mintwhale.console.mapper.dao.StoreMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService {
    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    lateinit var memberMapper: MemberMapper

    @Autowired
    lateinit var storeMapper: StoreMapper

    @Autowired
    lateinit var storeFileMapper: StoreFileMapper

    fun info(data: Member): Member? {
        val listSearch = ListSearch()
        listSearch.search = data
        listSearch.limit = 0
        listSearch.length = 1
        val result = memberMapper.getMember(listSearch)

        if(result.size > 0) {
            if(result[0].intType == 2) {
                val store = Store()
                store.intMSeq = result[0].intSeq
                val listSearch2 = ListSearch()
                listSearch2.search = store
                val result2 = storeMapper.getStore(listSearch2)
                for(sstore in result2) {
                    val storefile = StoreFile()
                    storefile.intSSeq = sstore.intSeq
                    val listSearch3 = ListSearch()
                    log.debug(listSearch3.toString())
                    sstore.arrFiles = storeFileMapper.getFile(listSearch3)
                }
            }
        }

        return if(result.size > 0) { result[0] } else { null }
    }

    @Transactional
    fun edit(data: Member): Boolean? {
        return memberMapper.editMember(data)
    }

    @Transactional
    fun add(data: Member): Boolean? {
        val result = memberMapper.setMember(data)

        if(result && data.arrStore != null) {
            for(field in data.arrStore!!) {
                field.intMSeq = data.intSeq
                val result2 = storeMapper.setStore(field)
                if(result2 && field.arrFiles != null) {
                    var sort = 1
                    for (field2 in field.arrFiles!!) {
                        field2.intType = 1
                        field2.intMSeq = data.intSeq
                        field2.intSSeq = field.intSeq
                        field2.intSort = sort
                        storeFileMapper.editFile(field2)
                        sort++
                    }
                }
            }
        }

        return result
    }

    @Transactional
    fun list(data: ListSearch): ResultList<Member> {
        val result = memberMapper.getMember(data)
        val result2 = memberMapper.getMemberCount(data)
        if(result.size > 0 && data.search != null && (data.search as Member).intType == 2) {
            for(field in result) {
                val ls = ListSearch()
                val soption = Store()
                soption.intMSeq = field.intSeq
                ls.search = soption
                field.arrStore = storeMapper.getStore(ls)
            }
        }

        val list = ResultList<Member>()
        list.list = result
        list.totalcount = result2

        return list
    }
    @Transactional
    fun editStore(data: Store): Boolean? {
        val result = storeMapper.setStore(data)
        if(result && data.arrFiles != null) {
            var sort = 1
            for (field in data.arrFiles!!) {
                field.intType = 1
                field.intSSeq = data.intSeq
                field.intSort = sort
                storeFileMapper.editFile(field)
                sort++
            }
        }

        return result
    }

}