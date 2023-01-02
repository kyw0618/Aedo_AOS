package com.aedo.my_heaven.model.restapi.base

import android.net.Uri
import com.aedo.my_heaven.model.list.Deceased
import com.aedo.my_heaven.model.list.Resident

data class CreateModel(
    var img : String? = null,
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

data class Resident (
    var relation : String? = null,
    var name : String? = null,
    var phone : String? = null,
)

data class Deceased(
    var name: String? = null,
    var age: String? = null,
)

data class Place(
    val name : String? = null,
    val number : String? = null
)

data class Eod(
    val date: String? = null,
    val time: String? = null
)

data class Coffin(
    val date: String? = null,
    val time: String? = null
)

data class Dofp(
    val date: String? = null,
    val time: String? = null
)
