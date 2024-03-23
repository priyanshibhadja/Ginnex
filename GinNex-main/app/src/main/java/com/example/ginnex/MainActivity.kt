package com.example.ginnex

import com.example.ginnex.AuthActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ginnex.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.rpc.context.AttributeContext.Auth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding // Declare your View Binding variable
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Initialize View Binding
        setContentView(binding.root)

        binding.btnAdduser.setOnClickListener{
            val intent = Intent(this,AuthActivity::class.java)
            startActivity(intent)

        }
        binding.btnProfile.setOnClickListener{
            val intent = Intent(this,Profileadmin::class.java)
            startActivity(intent)

        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()


//        if (userId != null) {
//            db.collection("users").document(userId).get()
//                .addOnSuccessListener { document ->
//                    if (document != null) {
//                        val username = document.getString("Username")
//                        val fullName = document.getString("Name")
//
//                        binding.etUsername.setText(username)
//                        binding.etFullName.setText(fullName)
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    // Handle failures here
//                }
//        }

        }
    }
}