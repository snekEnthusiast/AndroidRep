package com.example.tasklist

import android.R
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Task{
    var name:String = ""
    var description:String = ""
    var dtime:String = "1970-01-01"//LocalDateTime.parse("1970-01-01",DateTimeFormatter.ofPattern("yyyy-mm-dd"))
    var completed:Boolean = false
    constructor(n:String?,d:String?,dt:String?){
        if(n!=null){
            name=n
        }
        if(d!=null){
            description=d
        }
        if(dt!=null){
            dtime = dt
        }
    }
    override fun equals(other: Any?): Boolean {
        if(other is Task){
            return name == other.name && description == other.description && dtime == other.dtime
        }
        return false
    }
}