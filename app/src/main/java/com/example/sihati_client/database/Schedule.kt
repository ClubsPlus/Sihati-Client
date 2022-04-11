package com.example.sihati_client.database


data class Schedule (
    var id:String? = "",
    var date:String? = "",
    var laboratory_id:String? = "",
    var limite:Int?=-1,
    var person:Int?=0,
    var time_Start:String? = "",
    var time_end:String? = "",
    var laboratory_name:String? = ""
)