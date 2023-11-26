package com.example.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShopFragment : Fragment(R.layout.shop){
    companion object {
        var c: Context? = null
        var list: ArrayList<Item> = arrayListOf(
            Item("name1", "desc1", 1),
            Item("name2", "desc2", 100),
            Item("n3", "d3", 3)
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.shop, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemAdapter=CustomAdapter(list)
        val recyclerView:RecyclerView=view.findViewById(R.id.shoplist)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter
    }
    inner class CustomAdapter(private val dataSet: ArrayList<Item>) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView
            val button: Button
            init {
                textView = view.findViewById(R.id.textView)
                button = view.findViewById(R.id.button)
            }
        }
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.litem, viewGroup, false)
            return ViewHolder(view)
        }
        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.textView.text = dataSet[position].name
            viewHolder.button.setOnClickListener{
                goToItem(dataSet[position])
            }
        }
        override fun getItemCount() = dataSet.size
        private fun goToItem(i:Item){
            ItemActivity.i=i
            var i = Intent(c,ItemActivity::class.java)
            startActivity(i)
        }
    }
}