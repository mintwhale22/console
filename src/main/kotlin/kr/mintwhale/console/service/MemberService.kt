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

        return if(result.size > 0) { result[0] } else { null }
    }

    @Transactional
    fun edit(data: Member): Boolean? {
        val result = memberMapper.editMember(data)

        if(result && data.arrStore != null) {
            for(field in data.arrStore!!) {
                val result2 = if(field.intSeq!! > 0) {
                    storeMapper.editStore(field)
                } else {
                    storeMapper.setStore(field)
                }

                if(result2 && field.arrFiles != null) {
                    var sort = 1

                    val delfile = StoreFile()
                    delfile.intMSeq = data.intSeq
                    delfile.intSSeq = field.intSeq
                    delfile.intType = 99

                    val listSearch = ListSearch()
                    val storeFile = StoreFile()
                    storeFile.intMSeq = data.intSeq
                    storeFile.intSSeq = field.intSeq
                    listSearch.search = storeFile

                    val result3 = storeFileMapper.getFile(listSearch)

                    for (nowfile in result3) {
                        delfile.intSeq = nowfile.intSeq
                        storeFileMapper.editFile(delfile)
                    }

                    for (field2 in field.arrFiles!!) {
                        field2.intType = 1
                        field2.intMSeq = data.intSeq
                        field2.intSSeq = field.intSeq
                        field2.intSort = sort
                        storeFileMapper.editFile(field2)
                        sort++
                    }

                    delfile.intSeq = null
                    storeFileMapper.delFile(delfile)
                }

            }
        }

        return result
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

    fun listStore(data: ListSearch): ArrayList<Store> {
        val result2 = storeMapper.getStore(data)
        for(sstore in result2) {
            val storefile = StoreFile()
            storefile.intSSeq = sstore.intSeq
            val listSearch3 = ListSearch()
            listSearch3.search = storefile
            sstore.arrFiles = storeFileMapper.getFile(listSearch3)
        }
        return result2
    }

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