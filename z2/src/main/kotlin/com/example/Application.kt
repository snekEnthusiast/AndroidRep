package com.example

import com.example.plugins.*
import io.ktor.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.plugins.*
import java.io.File

fun main() {
	embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
			.start(wait = true)
}

fun Application.module() {
	//create database
	if(!File("/home/mikolaj/IdeaProjects/ktor-sample/data/data.db").exists()){
		throw Exception("project/build/db/data.db not found")
	}
	DatabaseFactory.init()
	configureRouting()
}
