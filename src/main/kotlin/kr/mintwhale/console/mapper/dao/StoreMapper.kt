package kr.mintwhale.console.mapper.dao

import kr.mintwhale.console.data.model.ListSearch
import kr.mintwhale.console.data.model.Store
import kr.mintwhale.console.data.model.StoreFile

interface StoreMapper {
    fun setStore(data: Store) : Boolean
    fun getStore(search: ListSearch) : ArrayList<Store>
    fun editStore(data: Store) : Boolean
    fun delStore(data: Store) : Boolean
}