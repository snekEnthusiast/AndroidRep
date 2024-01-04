package com.example.auth

import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import org.http4k.client.ApacheClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }
    fun replaceFragment(f:Fragment){
        val manager = supportFragmentManager
        manager.beginTransaction()
        manager.commit{
            replace(R.id.fragmentContainerView,f)
        }
    }
}
class MainFragment : Fragment(R.layout.main){
    val url = "http://192.168.86.156:5000" //default flask port
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val v = requireView()
        val username = v.findViewById<EditText>(R.id.username)
        val password = v.findViewById<EditText>(R.id.password)
        val login =    v.findViewById<Button>(R.id.login)
        val register = v.findViewById<Button>(R.id.register)
        login.setOnClickListener{
            val client = ApacheClient()
            val request = Request(Method.GET, url+"/account/"+username.text+"/"+password.text)
            val c = client(request)
            if(c!!.status.code != 200){
                login.setBackgroundColor(Color.RED)
            }else{
                infoFragment.text = "returned content:\n"+c!!.bodyString()
                (activity as MainActivity).replaceFragment(infoFragment())
            }
        }
        register.setOnClickListener{
            val client = ApacheClient()
            val request = Request(Method.GET, url+"/register/"+username.text+"/"+password.text)
            val c = client(request)
            if(c!!.status.code != 200){
                register.setBackgroundColor(Color.RED)
            }else{
                register.setBackgroundColor(Color.GREEN)
            }
        }
    }
}

class infoFragment : Fragment(R.layout.fragment){
    companion object{
        var text = "defaultString"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val v = getView()
        v!!.findViewById<TextView>(R.id.textView).text = text
    }
}