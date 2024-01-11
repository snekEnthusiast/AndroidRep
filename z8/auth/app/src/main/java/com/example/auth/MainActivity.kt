package com.example.auth

import android.content.Intent
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import org.http4k.client.ApacheClient
import org.http4k.core.Method
import org.http4k.core.Request
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider

/*
important note - sign in with google may not work due to issues with google.
For unknown reasons google's signInIntent Activity instantly fails at random

also, testing without the flask server will return "" for content
*/

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
    fun loginWithSecret(data:String){
        val client = ApacheClient()
        val request = Request(Method.GET, MainFragment.url+"/oauth/supersecretvalue/"+data)
        val c = client(request)
        infoFragment.text = "returned content:\n"+c.bodyString()
        replaceFragment(infoFragment())
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("mytag","activityReturn "+requestCode+" , "+ resultCode)
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MainFragment.G_SIGN_IN){
            Log.i("mytag","correctSignInCode")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {

                Log.i("mytag","incorrectSignIn")
                // Google Sign In failed, update UI appropriately
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, /*accessToken=*/ null)
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.i("mytag","signInSuccess")
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    loginWithSecret(user.toString())
                } else {
                    // If sign in fails, display a message to the user.
                }
            }
    }
}
class MainFragment : Fragment(R.layout.main){
    companion object{
        val G_SIGN_IN = 9001
        val url = "http://192.168.86.156:5000" //default flask port
    }
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
        val google =   v.findViewById<SignInButton>(R.id.google)
        val github =   v.findViewById<Button>(R.id.github)
        val previous = v.findViewById<Button>(R.id.saved)
        login.setOnClickListener{
            val client = ApacheClient()
            val request = Request(Method.GET, url+"/account/"+username.text+"/"+password.text)
            val c = client(request)
            if(c.status.code != 200){
                login.setBackgroundColor(Color.RED)
            }else{
                infoFragment.text = "returned content:\n"+c.bodyString()
                (activity as MainActivity).replaceFragment(infoFragment())
            }
        }
        register.setOnClickListener{
            val client = ApacheClient()
            val request = Request(Method.GET, url+"/register/"+username.text+"/"+password.text)
            val c = client(request)
            if(c.status.code != 200){
                register.setBackgroundColor(Color.RED)
            }else{
                register.setBackgroundColor(Color.GREEN)
            }
        }
        google.setOnClickListener{
            Log.i("mytag","googleclick")
            val provider = OAuthProvider.newBuilder("google.com")
            val firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth
                    .startActivityForSignInWithProvider(requireActivity(), provider.build())
                    .addOnSuccessListener {
                        Log.i("mytag","loginsuccess")
                        /* User is signed in.
                        // IdP data available in
                        // authResult.getAdditionalUserInfo().getProfile().
                        // The OAuth access token can also be retrieved:
                        // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                        // The OAuth secret can be retrieved by calling:
                        // ((OAuthCredential)authResult.getCredential()).getSecret().*/
                        (activity as MainActivity).loginWithSecret(it.getAdditionalUserInfo()!!.getProfile().toString())
                    }
        }
        github.setOnClickListener{
            val provider = OAuthProvider.newBuilder("github.com")
            val firebaseAuth = FirebaseAuth.getInstance()
            val pendingResultTask = firebaseAuth.pendingAuthResult
            if (pendingResultTask != null) {
                pendingResultTask
                    .addOnSuccessListener {
                        (activity as MainActivity).loginWithSecret(FirebaseAuth.getInstance().currentUser.toString())
                    }
            } else {
                firebaseAuth
                    .startActivityForSignInWithProvider(requireActivity(), provider.build())
                    .addOnSuccessListener {
                        (activity as MainActivity).loginWithSecret(it.getAdditionalUserInfo()!!.getProfile().toString())
                    }
            }
        }
        previous.setOnClickListener{
            if(infoFragment.text != "defaultString"){
                (activity as MainActivity).replaceFragment(infoFragment())
            }else{
                previous.setBackgroundColor(Color.RED)
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