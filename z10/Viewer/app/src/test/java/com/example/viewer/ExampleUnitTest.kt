package com.example.viewer

import org.http4k.client.ApacheClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.junit.Test

import org.junit.Assert.*

/*
blackbox tests for connector module
 */
class ExampleUnitTest {
    @Test
    fun mockConnectTestBaseState(){
        connector.setMock()
        val s = connector.State
        assert(s.products.size==3)
        var p = s.products[0]
        assert(p.name=="product1")
        assert(p.type=="type1")
        assert(!p.used)
        p = s.products[1]
        assert(p.name=="product2")
        assert(p.type=="type1")
        assert(!p.used)
        p = s.products[2]
        assert(p.name=="product3")
        assert(p.type=="type1")
        assert(!p.used)

        assert(s.types.size==2)
        var t = s.types[0]
        assert(t.id=="type1")
        assert(t.specs=="insertSpecs")
        assert(!t.perishable)
        assert(t.plimit==1000)
        t = s.types[1]
        assert(t.id=="type2")
        assert(t.specs=="someSpecs")
        assert(!t.perishable)
        assert(t.plimit==2000)
    }
    @Test
    fun mockConnectTestAddCorrect(){
        connector.setMock()
        connector.add(Product(565,"testName","type1",true))
        val s = connector.State
        val p = s.products.stream().filter{p->p.id==565}.findFirst()
        assert(p.isPresent)
        assert(p.get().name=="testName")
        assert(p.get().type=="type1")
        assert(p.get().used)
    }
    @Test
    fun mockConnectTestAddIncorrect(){
        connector.setMock()
        connector.add(Product(565,"testName","type3",true))
        val s = connector.State
        val p = s.products.stream().filter{p->p.id==565}.findFirst()
        assert(!p.isPresent)
    }
    @Test
    fun realConnectPreset(){
        val client = ApacheClient()
        val request = Request(Method.GET, connector.url +"/db_restore")
        client(request)
        connector.disableMock()

        val S = connector.State
        assert(S.products.size>=3)
        var p = S.products.stream().filter{p->p.id==1}.findFirst()
        assert(p.isPresent)
        assert(p.get().name=="product1")
        assert(!p.get().used)
        assert(p.get().type=="type1")
        p = S.products.stream().filter{p->p.id==2}.findFirst()
        assert(p.isPresent)
        assert(p.get().name=="productno2")
        assert(p.get().used)
        assert(p.get().type=="type1")
        p = S.products.stream().filter{p->p.id==3}.findFirst()
        assert(p.isPresent)
        assert(p.get().name=="3rdprod")
        assert(!p.get().used)
        assert(p.get().type=="type1")
        assert(S.types.size>=2)
        var t = S.types.stream().filter{t->t.id=="type1"}.findFirst()
        assert(t.isPresent)
        assert(t.get().specs=="specs1")
        assert(!t.get().perishable)
        assert(t.get().plimit==100)
        t = S.types.stream().filter{t->t.id=="type2"}.findFirst()
        assert(t.isPresent)
        assert(t.get().specs=="specs2")
        assert(!t.get().perishable)
        assert(t.get().plimit==200)
    }
    @Test
    fun realConnectorTest(){
        val client = ApacheClient()
        val request = Request(Method.GET, connector.url +"/db_restore")
        client(request)

        connector.disableMock()
        val S1 = connector.State
        val i = S1.products.stream().mapToInt{p->p.id}.max().orElse(0)+1
        connector.add(Product(i,"testname","type1",true))
        val S2 = connector.State
        val p =S2.products.stream().filter{p->p.id==i}.findFirst()
        assert(p.isPresent)
        assert(p.get().name=="testname")
        assert(p.get().type=="type1")
        assert(p.get().used)
        connector.add(Product(i+1,"testname","NoType",false))
        val S3 = connector.State
        assert(!S3.products.stream().anyMatch{p->p.id==i+1})
    }
}