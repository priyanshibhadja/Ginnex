package com.example.ginnex

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ginnex.databinding.ActivityProfileadminBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class Profileadmin : AppCompatActivity() {
    private lateinit var binding: ActivityProfileadminBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileadminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        val profileContainer: LinearLayout = findViewById(R.id.profileContainer)

        // Query Firestore for user profiles
        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val name = document.getString("name")
                    val email = document.getString("email")
                    val contactInfo = document.getString("contactInfo")
                    // Create TextViews dynamically and set user data
                    val profileName = TextView(this)
                    profileName.text = "Name: $name"

                    val profileEmail = TextView(this)
                    profileEmail.text = "Email: $email"

                    val profileContact = TextView(this)
                    profileContact.text = "Contact: $contactInfo"
                    val profileImage = ImageView(this)
                    val profilePicUrl = document.getString("profilePic")

                    if (profilePicUrl != null) {
                        Picasso.get().load(profilePicUrl).into(profileImage)
                    }


                    // Add these TextViews to your layout
                    profileContainer.addView(profileName)
                    profileContainer.addView(profileEmail)
                    profileContainer.addView(profileContact)
                    profileContainer.addView(profileImage)

                }
            }
            .addOnFailureListener { e ->
                // Handle the error
            }
    }
}
