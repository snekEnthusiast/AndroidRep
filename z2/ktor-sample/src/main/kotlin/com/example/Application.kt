package com.example

import com.example.plugins.DatabaseFactory
import com.example.plugins.configureRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.Routing.Plugin.install
import java.io.File


fun main() {
	embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::module).start(wait = true)
}

fun Application.module() {
	install(CORS){
		anyHost()
	}
	
	//create database
	val filecheck = false
	if(!File("/home/mikolaj/IdeaProjects/ktor-sample/data/data.db").exists() && filecheck){
		throw Exception("project/build/db/data.db not found")
	}
	DatabaseFactory.init()
	configureRouting()
}
