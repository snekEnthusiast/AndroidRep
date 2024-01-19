package com.example.viewer

import com.google.gson.Gson
import org.http4k.client.ApacheClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response

data class Product(val id:Int, val name:String, val type:String, val used:Boolean)
data class Type(val id:String, val specs:String, val perishable:Boolean, val plimit:Int)
data class State(val products:ArrayList<Product>, val types:ArrayList<Type>)
private val mockStart = State(arrayListOf(
    Product(0,"product1","type1",false),
    Product(1,"product2","type1",false),
    Product(2,"product3","type1",false)
), arrayListOf(
    Type("type1","insertSpecs",false,1000),
    Type("type2","someSpecs",false,2000)
))

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
        const val url = "http://192.168.86.156:8080"
        private var mock = true //mock responses for ease of use

        var State = if(mock) mockStart else State(arrayListOf(), arrayListOf())
        private val gson = Gson()
        fun get(){ //save remote state to state
            if(mock){
                return
            }
            var strFromRequest = "{\"products\":[],\"types\":[]}"
            val client = ApacheClient()
            val request = Request(Method.GET, url+"/db_all")
            val c = client(request)
            strFromRequest = c.bodyString()
            State = gson.fromJson(strFromRequest,State.javaClass)
        }
        fun add(p:Product){//save remote state from local
            if(mock){
                if(p.type in State.types.stream().map{t->t.id}.toList())
                State.products.add(p)
                return
            }
            val client = ApacheClient()
            val request = Request(Method.POST, url+"/db/product").body(gson.toJson(p))
            client(request)
            get()
        }
        fun setMock(){
            mock = true
            State = mockStart
        }
        fun disableMock(){
            mock = false
            get()
        }
        fun isMocked():Boolean{
            return mock
        }
    }
}