package com.example.tasklist

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.databinding.TaskBinding


class CustomAdapter : RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private var host: Activity? = null
    constructor(h:Activity){
        host=h
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textViewName: TextView
        val textViewDesc: TextView
        val textViewDate: TextView
        val buttonDel: Button
        val buttonDone: Button
        init {
            textViewName = view.findViewById(R.id.task_name)
            textViewDesc = view.findViewById(R.id.task_desc)
            textViewDate = view.findViewById(R.id.task_date)
            buttonDel = view.findViewById(R.id.del_button)
            buttonDone = view.findViewById(R.id.done_button)
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val elem = DataBinding.at(position)
        viewHolder.textViewName.text = elem.name
        viewHolder.textViewDesc.text = elem.description
        viewHolder.textViewDate.text = elem.dtime
        if(DataBinding.at(position).done){
            viewHolder.buttonDone.setBackgroundColor(Color.GREEN)
        }
        viewHolder.buttonDel.setOnClickListener {
            remove(Task(viewHolder.textViewName.text.toString(),
                viewHolder.textViewDesc.text.toString(),
                viewHolder.textViewDate.text.toString(),
                viewHolder.buttonDone.visibility == View.GONE))
        }
        viewHolder.buttonDone.setOnClickListener{
            DataBinding.at(position).done = true
            viewHolder.buttonDone.visibility = View.GONE
            viewHolder.buttonDel.setBackgroundColor(Color.parseColor("#00cf00"))
        }
        if(DataBinding.at(position).done){
            viewHolder.buttonDone.callOnClick()
        }
    }
    override fun getItemCount() = DataBinding.size()
    fun add(name:String, desc:String, dt: String){
        DataBinding.add(Task(name,desc,dt))
        notifyItemInserted(itemCount)
    }
    fun remove(t:Task){
        val i = DataBinding.indexOf(t)
        DataBinding.removeAt(i)
        notifyItemRemoved(i)
        notifyItemRangeChanged(i, itemCount)
    }
}
