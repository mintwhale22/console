package kr.mintwhale.console.mapper.dao

import kr.mintwhale.console.data.model.ListSearch
import kr.mintwhale.console.data.model.Notice

interface NoticeMapper {
    fun setNotice(data: Notice): Boolean
    fun getNotice(data: ListSearch) : ArrayList<Notice>
    fun getNoticeCount(data: ListSearch): Int
    fun editNotice(data: Notice): Boolean
    fun delNotice(data: Notice): Boolean
}