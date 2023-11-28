package com.example.tasklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    private fun add(c:CustomAdapter){
        val tTitle = findViewById<EditText>(R.id.input_name)
        val tDesc = findViewById<EditText>(R.id.input_desc)
        val tDate = findViewById<EditText>(R.id.input_date)
        c.add(tTitle.text.toString(),tDesc.text.toString(),null)
        tTitle.setText("")
        tDesc.setText("")
        tDate.setText("")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val arr = arrayListOf(
            Task("name1","desc1","01-01-1970"),
            Task("name2","desc2","20-01-1970"),
            Task("name3","desc3","07,08-1990"))
        val customAdapter = CustomAdapter(this,arr)

        val recyclerView: RecyclerView = findViewById(R.id.tasks)
        recyclerView.adapter = customAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        findViewById<Button>(R.id.add).setOnClickListener{
            add(customAdapter)
        }
    }
}