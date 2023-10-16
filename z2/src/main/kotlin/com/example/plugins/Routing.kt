package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import kotlinx.serialization.*
import com.example.plugins.*
	
fun Application.configureRouting() {
	install(ContentNegotiation) {
		json()
	}
	
	
	routing {
		get("/helloworld"){
			call.respond("Hello World!")
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
			if(call.parameters["table"] == "product"){
				val d = call.receive<Product>()
				f.addProduct(d.id,d.name,d.type,d.used)
			}
			if(call.parameters["table"] == "type"){
				val d = call.receive<Type>()
				f.addType(d.id,d.specs,d.perishable,d.plimit)
			}
		}
		delete("/db/{table}"){
			val f = FImpl()
			if(call.parameters["table"] == "product"){
				val d = call.receive<Int>()
				f.deleteProduct(d)
			}
			if(call.parameters["table"] == "type"){
				val d = call.receive<String>()
				f.deleteType(d)
			}
		}
		patch("/db/products"){
			val f = FImpl()
			if(call.parameters["table"] == "product"){
				val d = call.receive<Product>()
				f.updateProduct(d.id,d.name,d.type,d.used)
			}
			if(call.parameters["table"] == "product"){
				val d = call.receive<Type>()
				f.updateType(d.id,d.specs,d.perishable,d.plimit)
			}
		}
	}
}
