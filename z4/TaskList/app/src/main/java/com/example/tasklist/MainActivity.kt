package com.example.tasklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(){
    private fun add(c:CustomAdapter){
        val tTitle = findViewById<EditText>(R.id.input_name)
        val tDesc = findViewById<EditText>(R.id.input_desc)
        val tDate = findViewById<EditText>(R.id.input_date)
        c.add(tTitle.text.toString(),tDesc.text.toString(),tDate.text.toString())
        tTitle.setText("")
        tDesc.setText("")
        tDate.setText("")
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DataBinding.read(this)
        val customAdapter = CustomAdapter(this)

        val recyclerView: RecyclerView = findViewById(R.id.tasks)
        recyclerView.adapter = customAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        findViewById<Button>(R.id.add).setOnClickListener{
            add(customAdapter)
        }
    }

    override fun onStop() {
        DataBinding.write(this)
        super.onStop()
    }
}