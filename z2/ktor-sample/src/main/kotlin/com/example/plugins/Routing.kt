package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.plugins.cors.routing.CORS
import com.google.gson.Gson

fun Application.configureRouting() {
	install(ContentNegotiation) {
		json()
	}
	
	data class State(val products:List<Product>, val types:List<Type>)
	routing {
		get("/helloworld"){
			call.respond("Hello World!")
		}
		get("/db_all"){
			val f = FImpl()
			val ps = f.allProducts()
			val ts = f.allTypes()
			val state = State(ps,ts)
			call.respond(Gson().toJson(state))
		}
		get("/db/{table}"){
			val f = FImpl()
			if(call.parameters["table"] == "product"){
				val d = f.allProducts()
				call.respond(d)
			}
			if(call.parameters["table"] == "type"){
				val d = f.allTypes()
				call.respond(d)
			}
		}
		post("/db/{table}"){
			val f = FImpl()
			println("post")
			if(call.parameters["table"].equals("product")){
				println("product")
				val c = call.receiveText()
				val d = Gson().fromJson(c,Product::class.java)
				println(d.id.toString()+" "+d.name.toString()+" "+d.type.toString()+" "+d.used.toString())
				f.addProduct(d.id,d.name,d.type,d.used)
			}
			if(call.parameters["table"].equals("type")){
				val d = call.receive<Type>()
				f.addType(d.id,d.specs,d.perishable,d.plimit)
			}
		}
		delete("/db/{table}"){
			val f = FImpl()
			if(call.parameters["table"].equals("product")){
				val d = call.receive<Int>()
				f.deleteProduct(d)
			}
			if(call.parameters["table"].equals("type")){
				val d = call.receive<String>()
				f.deleteType(d)
			}
		}
		patch("/db/products"){
			val f = FImpl()
			if(call.parameters["table"].equals("product")){
				val d = call.receive<Product>()
				f.updateProduct(d.id,d.name,d.type,d.used)
			}
			if(call.parameters["table"].equals("product")){
				val d = call.receive<Type>()
				f.updateType(d.id,d.specs,d.perishable,d.plimit)
			}
		}
	}
}
