package com.aedo.my_heaven.model.restapi.base

import com.aedo.my_heaven.model.list.Deceased
import com.aedo.my_heaven.model.list.Resident

data class CreateName(
    val result : List<CreateSearch>?=null
)

data class CreateSearch(
    var id: String? = null,
    var imgName: String? = null,
    var resident : Resident? = null,
    var deceased : Deceased? = null,
    var place : Place? = null,
    var eod: Eod? = null,
    var coffin: Coffin? = null,
    var dofp: Dofp? = null,
    var buried : String? = null,
    var word : String? = null,
    var created: String? = null,
)

