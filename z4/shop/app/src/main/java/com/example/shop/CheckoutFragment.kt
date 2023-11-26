package com.example.shop

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CheckoutFragment : Fragment(R.layout.checkout){
    companion object{
        var items = HashMap<Pair<String,String>,Int>()
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
        val list = ArrayList<Item>()
        items.forEach {e -> list.add(Item(e.key.first,e.key.second,e.value))}
        val itemAdapter=CustomAdapter(list)
        val recyclerView: RecyclerView =view.findViewById(R.id.shoplist)
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
                button.text="buy 1"
                button.setBackgroundColor(Color.GREEN)
            }
        }
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.litem, viewGroup, false)
            return ViewHolder(view)
        }
        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.textView.text = dataSet[position].name + ": " + dataSet[position].amount.toString()
            viewHolder.button.setOnClickListener{
                dataSet[position].amount -= 1
                if(dataSet[position].amount == 0){
                    dataSet.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position,dataSet.size)
                } else {
                    notifyItemChanged(position)
                }
            }
        }
        override fun getItemCount() = dataSet.size
    }
}