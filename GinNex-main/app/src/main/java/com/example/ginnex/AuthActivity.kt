package com.example.ginnex


import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.ginnex.databinding.ActivityAuthBinding // Import your View Binding class
import com.example.ginnex.models.User

class AuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityAuthBinding // Declare your View Binding variable
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
    private lateinit var ivProfilepic: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater) // Initialize View Binding
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        ivProfilepic = findViewById(R.id.ivProfilepic)

        binding.btnSelectProfilePic.setOnClickListener {
            selectProfilePicture()
        }
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val name = binding.etName.text.toString()
            val contactInfo = binding.etContactInfo.text.toString()
            val role = binding.etRole.text.toString()

            // Check if any field is empty
            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || contactInfo.isEmpty() || role.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userId = user?.uid

                        if (userId != null) {
                            val profilePic = ivProfilepic.tag as? String

                            uploadUserDataToFirestore(userId, name, email, contactInfo, profilePic ?: "", role)
                        }
                        val roleLowercase = role.lowercase()

                        if (roleLowercase == "admin") {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("USER_ROLE", "admin") // Pass the role as a string
                            startActivity(intent)

                        } else {
                            val intent = Intent(this, MainActivity_user::class.java)
                            intent.putExtra("USER_ROLE", "user") // Pass the role as a string
                            startActivity(intent)

                        }

                        finish()
                    } else {
                        Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun uploadUserDataToFirestore(userId: String, name: String, email: String, contactInfo: String, profilePic: String, role: String) {
        val user = User(userId, name, email, contactInfo, profilePic, role)

        db.collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(baseContext, "User data added to Firestore.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(baseContext, "Error adding user data to Firestore: $e", Toast.LENGTH_SHORT).show()
            }
    }
    private fun selectProfilePicture() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            ivProfilepic.setImageURI(imageUri)
            ivProfilepic.tag = imageUri.toString() // Set the tag with the image URI as a string
        }
    }


}








