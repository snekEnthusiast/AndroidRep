package com.example.tasklist

import androidx.lifecycle.ViewModel

/*
* completely unnecessary, db guarantees persistance
*/
class CustomViewModel(
    val name:String,
    val description:String,
    val dtime:String,
    val done:Boolean) : ViewModel() {
        fun getTask():Task{
            return Task(name, description, dtime,done)
        }
}