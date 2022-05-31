package kr.mintwhale.console.mapper.dao

import kr.mintwhale.console.data.model.Member

interface MemberMapper {
    fun setMember(data: Member): Boolean
    fun getMember(data: Member): ArrayList<Member>
    fun editMember(data: Member): Boolean
}