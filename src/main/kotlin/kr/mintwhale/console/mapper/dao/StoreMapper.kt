package kr.mintwhale.console.mapper.dao

import kr.mintwhale.console.data.model.ListSearch
import kr.mintwhale.console.data.model.Store
import kr.mintwhale.console.data.model.StoreFile

interface StoreMapper {
    fun setStore(data: StoreFile) : Boolean
    fun getStore(search: ListSearch) : ArrayList<Store>
    fun editStore(data: StoreFile) : Boolean
    fun delStore(data: StoreFile) : Boolean
}