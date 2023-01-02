package com.aedo.my_heaven.model.list

import com.aedo.my_heaven.model.restapi.base.Coffin
import com.aedo.my_heaven.model.restapi.base.Dofp
import com.aedo.my_heaven.model.restapi.base.Eod
import com.aedo.my_heaven.model.restapi.base.Place

data class RecyclerList(
    var obituary : List<Obituaray>?=null,
)
data class Obituaray(
    var id: String? = null,
    var imgName : String? = null,
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


data class Resident(
    var relation : String? = null,
    var name : String? = null,
    var phone : String? = null,
)

data class Deceased(
    var name: String? = null,
    var age: String? = null
)



