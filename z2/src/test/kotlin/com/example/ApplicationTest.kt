package com.example

import com.example.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.serialization.kotlinx.json.*

class ApplicationTest {
    @Test
    fun dbtest() = testApplication {
        application {
            configureRouting()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        DatabaseFactory.init()
        val response = client.post("db/product") {
            contentType(ContentType.Application.Json)
            setBody(Product(0, "name0", "type0", false))
        }
        val v = client.get("db/product").apply{
            assertEquals(HttpStatusCode.OK, status)
        }
        System.out.println(v)
    }
    @Test
    fun con_test() = testApplication {
        application {
            configureRouting()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.get("/helloworld").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
