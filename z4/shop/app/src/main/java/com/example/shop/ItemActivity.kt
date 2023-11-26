package com.example.shop

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import kotlin.math.min

class ItemActivity() : AppCompatActivity(){
    companion object{
        var i: Item=Item("non-initialized item","you should not see this",1)
    }
    private fun goBack(){
        finish()
    }
    private fun putInCheckout(i:Item){
        CheckoutFragment.items[Pair(i.name,i.desc)] = i.amount
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item)
        val name:TextView  = findViewById(R.id.name)
        val desc:TextView  = findViewById(R.id.desc)
        val abuy:EditText  = findViewById(R.id.buy_amount)
        val aleft:TextView = findViewById(R.id.amount_left)
        val back:Button    = findViewById(R.id.button_back)
        val buy:Button     = findViewById(R.id.button_buy)

        name.text = i.name
        desc.text = i.desc
        abuy.text = SpannableStringBuilder("0")
        aleft.text =  SpannableStringBuilder(i.amount.toString())
        back.setOnClickListener{
            goBack()
        }
        buy.setOnClickListener{
            var amount:Int = Integer.parseInt(abuy.text.toString())
            amount = min(amount,i.amount)
            putInCheckout(Item(i.name,i.desc,amount))
            i.amount -= amount
            if(i.amount==0){
                ShopFragment.list.remove(i)
            }
            goBack()
        }
    }
}