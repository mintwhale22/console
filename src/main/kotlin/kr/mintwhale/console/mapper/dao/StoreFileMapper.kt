package kr.mintwhale.console.mapper.dao

import kr.mintwhale.console.data.model.ListSearch
import kr.mintwhale.console.data.model.StoreFile

interface StoreFileMapper {
    fun setFile(data: StoreFile) : Boolean
    fun getFile(search: ListSearch) : ArrayList<StoreFile>
    fun editFile(data: StoreFile) : Boolean
    fun delFile(data: StoreFile) : Boolean
}