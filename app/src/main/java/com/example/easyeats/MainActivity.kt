 package com.example.easyeats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

 class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth

    }
     //Might replace with a logout button at some point
     override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         val inflater = menuInflater
         inflater.inflate(R.menu.main_menu, menu)
         return super.onCreateOptionsMenu(menu)
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when(item.itemId) {
             R.id.logout_btn -> {
                 auth.signOut()
                 Toast.makeText(this, "Logged Out", Toast.LENGTH_LONG)
                 Log.d("Auth", "User Was Logged Out")
                 return true
             }
             else -> {
                 return super.onOptionsItemSelected(item)
             }
         }
     }
}
