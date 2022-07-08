package kr.mintwhale.console.mapper.dao

import kr.mintwhale.console.data.model.AppSet
import kr.mintwhale.console.data.model.ListSearch

interface DefaultMapper {
    fun getPing() : String
    fun getVersion(data: ListSearch) : AppSet
    fun editVersion(data: AppSet) : Boolean
}