package com.example.myapplication


import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow
import kotlin.math.log


class MainActivity : AppCompatActivity(){
    enum class Operation(val s:String){
        add("+"),sub("-"),mult("*"),div("/"),mod("%"),log("log"),pow("^")
    }
    private var n2:Int? = null
    private var n1:Int? = null
    private var o:Operation? = null
    private var view = ""
    private var negative = false
    private var t:TextView?=null
    private fun updateText(){
        t!!.text = view
    }
    private fun numberCommon(n:Int){
        if(negative){
            view=view.dropLast(1)
        }
        if(o==null){
            if(n1==0){
                view = view.dropLast(1)
            }
            n1 = if(n1==null){
                if(negative){-n}else{n}
            }else{
                n1!!*10 + n
            }
            view += (n1!!%10).toString()
        }else{
            if(n2==0){
                view = view.dropLast(1)
            }
            n2 = if(n2==null){
                if(negative){-n}else{n}
            }else{
                n2!!*10 + n
            }
            view += (n2!!%10).toString()
        }
        negative=false
        updateText()
    }
    private fun opcommon(op:Operation){
        if(n1!=null && o==null){
            o=op
            view+=op.s
            updateText()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        t = findViewById<TextView>(R.id.textView)
        val b0 = findViewById<Button>(R.id.b0)
        val b1 = findViewById<Button>(R.id.b1)
        val b2 = findViewById<Button>(R.id.b2)
        val b3 = findViewById<Button>(R.id.b3)
        val b4 = findViewById<Button>(R.id.b4)
        val b5 = findViewById<Button>(R.id.b5)
        val b6 = findViewById<Button>(R.id.b6)
        val b7 = findViewById<Button>(R.id.b7)
        val b8 = findViewById<Button>(R.id.b8)
        val b9 = findViewById<Button>(R.id.b9)
        b1.setOnClickListener{numberCommon(1)}
        b2.setOnClickListener{numberCommon(2)}
        b3.setOnClickListener{numberCommon(3)}
        b0.setOnClickListener{numberCommon(0)}
        b4.setOnClickListener{numberCommon(4)}
        b5.setOnClickListener{numberCommon(5)}
        b6.setOnClickListener{numberCommon(6)}
        b7.setOnClickListener{numberCommon(7)}
        b8.setOnClickListener{numberCommon(8)}
        b9.setOnClickListener{numberCommon(9)}
        val bplus = findViewById<Button>(R.id.bplus)
        val bmin = findViewById<Button>(R.id.bmin)
        val bmult = findViewById<Button>(R.id.bmult)
        val bdiv = findViewById<Button>(R.id.bdiv)
        val bmod = findViewById<Button>(R.id.bmod)
        val blog = findViewById<Button>(R.id.blog)
        val bpow = findViewById<Button>(R.id.bpow)
        bplus.setOnClickListener{opcommon(Operation.add)}
        bdiv.setOnClickListener{opcommon(Operation.div)}
        bmod.setOnClickListener{opcommon(Operation.mod)}
        blog.setOnClickListener{opcommon(Operation.log)}
        bmult.setOnClickListener{opcommon(Operation.mult)}
        bpow.setOnClickListener{opcommon(Operation.pow)}
        bmin.setOnClickListener{
            if(n1==null || (o!=null &&n2==null)){
                if(negative){
                    view = view.dropLast(1)
                }else{
                    view += "-"
                }
                negative = !negative
            }else{
                opcommon(Operation.sub)
            }
            updateText()
        }
        val beq = findViewById<Button>(R.id.beq)
        val bdel = findViewById<Button>(R.id.bdel)
        val bclear = findViewById<Button>(R.id.bclear)
        bclear.setOnClickListener{
            n1=null
            n2=null
            view=""
            negative=false
            updateText()
        }
        bdel.setOnClickListener{
            if(n1!=null){
                if(o==null){
                    n1 = n1!!/10
                    if(n1==0){
                        n1=null
                    }
                    view = view.dropLast(1)
                }else if(n2==null){
                    o=null
                    view = if(view[view.length-1]=='g'){
                        view.dropLast(3)
                    }else{
                        view.dropLast(1)
                    }
                }else{
                    n2 = n2!!/10
                    if(n2==0){
                        n2=null
                    }
                    view.dropLast(1)
                }
            }
            updateText()
        }
        beq.setOnClickListener{
            if(n2 != null){
                val r = when(o){
                    Operation.add -> n1!!+n2!!
                    Operation.sub -> n1!!-n2!!
                    Operation.div -> if(n2 != 0){n1!!/n2!!}else{0}
                    Operation.mult-> n1!!*n2!!
                    Operation.mod -> n1!!%n2!!
                    Operation.pow -> n1!!.toDouble().pow(n2!!).toInt()
                    Operation.log -> log(n1!!.toDouble(),n2!!.toDouble()).toInt()
                    else-> 0
                }
                n1=r
                n2=null
                o=null
                view=n1.toString()
                updateText()
            }
        }
    }
}
