package com.example.viewer

import com.google.gson.Gson
import org.http4k.client.ApacheClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.server.Undertow
import org.http4k.server.asServer

data class Product(val id:Int, val name:String, val type:String, val used:Boolean)
data class Type(val id:String, val specs:String, val perishable:Boolean, val plimit:Int)
data class State(val products:ArrayList<Product>, val types:ArrayList<Type>)

class connector{
    companion object{
        /*default db:
        Products:
        1|product1|type1|0
        2|productno2|type1|1
        3|3rdprod|type1|0

        Types:
        type1|specs1|0|100
        type2|specs2|0|200
        */
        private const val url = "http://192.168.86.156:8080"
        private const val mock = true //mock responses for ease of use
        var State = if(mock) State(arrayListOf(
            Product(0,"product1","type1",false),
            Product(1,"product2","type1",false),
            Product(2,"product3","type1",false)
        ), arrayListOf(
            Type("type1","insertSpecs",false,1000),
            Type("type2","someSpecs",false,2000)
        ))else State(arrayListOf(), arrayListOf())
        private val gson = Gson()
        fun get(){ //save remote state to state
            if(mock){
                return
            }
            var strFromRequest = "{\"products\":[],\"types\":[]}"
            var done = false
            var c:Response?=null
            val t = Thread { //android throws error on network operations in main thread.
                val client = ApacheClient()
                val request = Request(Method.GET, url+"/db_all")
                c = client(request)
                done = true
            }
            t.start()
            while(!done){/*donothing*/} //join also triggers the exception
            strFromRequest = c!!.bodyString()
            State = gson.fromJson(strFromRequest,State.javaClass)
        }
        fun add(p:Product){//save remote state from local
            if(mock){
                State.products.add(p)
                return
            }
            var done = false
            var c:Response?=null
            val t = Thread { //android throws error on network operations in main thread.
                val client = ApacheClient()
                val request = Request(Method.POST, url+"/db/product").body(gson.toJson(p))
                c = client(request)
                done = true
            }
            t.start()
            while(!done){/*donothing*/} //join also triggers the exception
        }
    }
}