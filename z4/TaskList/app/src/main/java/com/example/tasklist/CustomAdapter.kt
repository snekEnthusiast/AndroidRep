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
import com.example.tasklist.databinding.ActivityMainBinding
import com.example.tasklist.databinding.TaskBinding


class CustomAdapter : RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private var host: Activity? = null
    private var taskList: ArrayList<Task> = arrayListOf()
    constructor(h:Activity,tl:ArrayList<Task>){
        host=h
        technicallyDataBinding.list=tl
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textViewName: TextView
        val textViewDesc: TextView
        val textViewDate: TextView
        val buttonDel: Button
        val buttonDone: Button
        var binding: TaskBinding? = null
        init {
            // Define click listener for the ViewHolder's View
            textViewName = view.findViewById(R.id.task_name)
            textViewDesc = view.findViewById(R.id.task_desc)
            textViewDate = view.findViewById(R.id.task_date)
            buttonDel = view.findViewById(R.id.del_button)
            buttonDone = view.findViewById(R.id.done_button)
            buttonDone.setOnClickListener{
                view.setBackgroundColor(Color.GREEN)
                view.findViewById<Button>(R.id.done_button).visibility = View.GONE
            }
        }
        fun addBinding(tb:TaskBinding):ViewHolder{
            binding=tb
            return this
        }
    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = DataBindingUtil.inflate<TaskBinding>(
            host!!.layoutInflater,R.layout.task,viewGroup,false
        )
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task, viewGroup, false)
        return ViewHolder(view).addBinding(binding)
    }
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your taskList at this position and replace the
        // contents of the view with that element
        viewHolder.textViewName.text = technicallyDataBinding.list[position].name
        viewHolder.textViewDesc.text = technicallyDataBinding.list[position].description
        viewHolder.textViewDate.text = technicallyDataBinding.list[position].dtime
        viewHolder.buttonDel.setOnClickListener {
            remove(Task(viewHolder.textViewName.text.toString(),
                viewHolder.textViewDesc.text.toString(),
                viewHolder.textViewDate.text.toString()))
        }
    }
    // Return the size of your taskList (invoked by the layout manager)
    override fun getItemCount() = technicallyDataBinding.list.size
    fun removeAt(i:Int){
        technicallyDataBinding.list.removeAt(i)
        notifyItemRemoved(i)
        notifyItemRangeChanged(i, taskList.size)
    }
    fun add(name:String, desc:String, dt: String?){
        technicallyDataBinding.list.add(Task(name,desc,dt))
        notifyItemInserted(taskList.size-1)
    }
    fun remove(t:Task){
        var l = technicallyDataBinding.list
        var i = technicallyDataBinding.list.indexOf(t)
        technicallyDataBinding.list.removeAt(i)
        notifyItemRemoved(i)
        notifyItemRangeChanged(i, taskList.size)
    }
}
