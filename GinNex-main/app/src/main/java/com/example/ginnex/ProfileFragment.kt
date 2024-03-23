package com.example.ginnex

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ginnex.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser

        currentUser?.let {
            db.collection("users")
                .document(it.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        val email = document.getString("email")
                        val contactInfo = document.getString("contactInfo")
                        val profilePicUrl = document.getString("profilePic")

                        binding.etName.setText(name)
                        binding.etEmail.setText(email)
                        binding.etContactInfo.setText(contactInfo)

                        if (profilePicUrl != null) {
                            Picasso.get().load(profilePicUrl).into(binding.ivProfilePic)
                        }

                    }
                }
        }
        binding.btnChangeImage.setOnClickListener {
            selectProfilePicture()
        }

        binding.btnSaveProfile.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val name = binding.etName.text.toString()
            val contactInfo = binding.etContactInfo.text.toString()

            val profilePic = binding.ivProfilePic.tag as? String // Get the new profile picture URL if available

            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userUpdate = hashMapOf<String, Any>(
                    "email" to email,
                    "name" to name,
                    "contactInfo" to contactInfo,

                )

                if (profilePic != null) {
                    userUpdate["profilePic"] = profilePic
                }

                db.collection("users")
                    .document(currentUser.uid)
                    .update(userUpdate)
                    .addOnSuccessListener {
                        // Profile updated successfully
                        Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        // Handle error
                        Toast.makeText(requireContext(), "Error updating profile: $e", Toast.LENGTH_SHORT).show()
                    }
            }
        }



        return binding.root

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            binding.ivProfilePic.setImageURI(imageUri)
            binding.ivProfilePic.tag = imageUri.toString() // Set the tag with the image URI as a string
        }
    }
    private fun selectProfilePicture() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
}

