package com.example.pay

import android.R.attr.value
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.payu.base.models.PayUPaymentParams
import org.http4k.client.ApacheClient
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.format.Jackson.auto
import java.time.LocalDateTime
import java.util.UUID


class MainActivity : AppCompatActivity() {
    companion object{
        var c:CustomAdapter? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val dataset = arrayListOf(
            Item("name1",10),
            Item("name2",20)
        )
        val rViewer = findViewById<RecyclerView>(R.id.recyclerView)
        rViewer.layoutManager = LinearLayoutManager(this)
        CustomAdapter.dataSet = dataset
        c = if (c==null) CustomAdapter(this) else c
        rViewer.adapter = c
    }
    fun purchase(item:Item):Boolean{
        amount = item.price*100
        val mock = false
        if(mock){
            val client = ApacheClient()
            val request = Request(Method.POST, "$url/mpay")
            val itemLens = Body.auto<Item>().toLens()
            val requestWithItem = itemLens(item, request)
            val c = client(requestWithItem)
            return c.status.code == 200
        }
        //here be dragons
        val myIntent = Intent(this, CheckoutActivity::class.java)
        startActivity(myIntent)
        return false
    }
}

fun buyWithPayu(price: Int):Boolean{
    val payUPaymentParams = PayUPaymentParams.Builder()
        .setAmount("$price.0")
        .setIsProduction(false)
        .setKey("")
        .setProductInfo("test")
        .setPhone("999999999")
        .setTransactionId(UUID.randomUUID().toString())
        .setFirstName("Firstname")
        .setEmail("bob@gmail.com")
        .setSurl("https://google.com")
        .setFurl("https://aseafiouaeoruvby.sd")
        .setUserCredential("")
        .build()
    return false
}

val payments: ArrayList<Pair<Item,String>> = arrayListOf()
const val url = "http://192.168.86.156:5000"
const val merchName = "testMerchName"
var amount = 0
var position = 0



data class Item(val name:String, val price:Int)

class CustomAdapter(val a:MainActivity) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    companion object{
        var dataSet: ArrayList<Item> = arrayListOf()
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val price: TextView
        val buy: Button
        val payu: Button
        init {
            name = view.findViewById(R.id.name)
            price = view.findViewById(R.id.price)
            buy = view.findViewById(R.id.buy)
            payu = view.findViewById(R.id.payu)
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = dataSet[position].name
        viewHolder.price.text = dataSet[position].price.toString()
        viewHolder.buy.setOnClickListener{
            com.example.pay.position = position
            if(a.purchase(dataSet[position])){
                payments.add(Pair(dataSet[position], LocalDateTime.now().toString()))
                deleteAt(position)
            }
        }
        viewHolder.payu.setOnClickListener{
            com.example.pay.position = position
            if(buyWithPayu(dataSet[position].price)){
                payments.add(Pair(dataSet[position], LocalDateTime.now().toString()))
                deleteAt(position)
            }
        }
    }
    override fun getItemCount() = dataSet.size
    fun deleteAt(position:Int){
        dataSet.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(0,dataSet.size)
    }
}
