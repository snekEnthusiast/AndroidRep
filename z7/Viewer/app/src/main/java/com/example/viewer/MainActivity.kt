package com.example.viewer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connector.get()

        val IAdapter = ItemAdapter()
        val ItemRView = findViewById<RecyclerView>(R.id.Items)
        ItemRView.adapter = IAdapter
        ItemRView.layoutManager = LinearLayoutManager(this)

        val TAdapter = TypeAdapter()
        val TypeRView = findViewById<RecyclerView>(R.id.Types)
        TypeRView.adapter = TAdapter
        TypeRView.layoutManager = LinearLayoutManager(this)

        val addButton = findViewById<Button>(R.id.button)
        val type = findViewById<EditText>(R.id.type)
        val name = findViewById<EditText>(R.id.name)
        val used = findViewById<EditText>(R.id.used)
        addButton.setOnClickListener{
            try{
                val p = Product(
                    connector.State.products.stream().mapToInt{p:Product->p.id}.max().asInt+1,
                    name.text.toString(),
                    type.text.toString(),
                    used.text.toString().equals("used")
                )
                connector.add(p)
                connector.get()
                type.setText("")
                name.setText("")
                used.setText("")
                IAdapter.notifyItemRangeChanged(0,IAdapter.itemCount)
            }catch(e:Exception){}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        var fOut = openFileOutput("local data", Context.MODE_PRIVATE);
        val data = Gson().toJson(connector.State)
        fOut.write(data.toByteArray())
        fOut.close()
    }
}

class ItemAdapter() : RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textView: TextView
        init {
            textView = view.findViewById(R.id.textView)
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.element, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val elem = connector.State.products[position]
        viewHolder.textView.text = elem.name + "\n" + elem.type + "\n" +
                if (elem.used) "used" else "new"
    }
    override fun getItemCount() = connector.State.products.size
}
class TypeAdapter() : RecyclerView.Adapter<TypeAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        init {
            textView = view.findViewById(R.id.textView)
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.element, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val elem = connector.State.types[position]
        viewHolder.textView.text = elem.id

    }
    override fun getItemCount() = connector.State.types.size
}