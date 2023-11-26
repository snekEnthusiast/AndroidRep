package com.example.shop

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ShopFragment.list = arrayListOf(
            Item("replacedFromMain","desc1",1),
            Item("name2","desc2",100),
            Item("n3","d3",3))
        val manager = supportFragmentManager
        val navi:NavigationBarView = findViewById(R.id.navigation_bar)
        ShopFragment.c = this
        var lshop: View.OnTouchListener = View.OnTouchListener { v, event ->
            manager.beginTransaction()
            manager.commit{
                replace(R.id.fragmentContainerView,ShopFragment())
            }
            true
        }
        var lcheck: View.OnTouchListener = View.OnTouchListener { v, event ->
            manager.beginTransaction()
            manager.commit{
                replace(R.id.fragmentContainerView,CheckoutFragment())
            }
            true
        }
        navi.setItemOnTouchListener(R.id.shop_navi,lshop)
        navi.setItemOnTouchListener(R.id.checkout_navi,lcheck)
    }
}