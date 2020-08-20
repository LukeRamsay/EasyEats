package com.example.easyeats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.easyeats.databinding.FragmentWritePostBinding
import kotlinx.android.synthetic.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WritePostFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentWritePostBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        db = Firebase.firestore

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_post, container, false)
        binding.cancelPost.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_writePostFragment)
        }

        binding.submitPost.setOnClickListener {
            val post = hashMapOf(
                "userId" to auth.currentUser?.uid,
                "username" to auth.currentUser?.displayName,
                "timestamp" to Timestamp.now(),
                "title" to binding.postTitle.text.toString(),
                "descriptions" to binding.postDescription.text.toString(),
                "ingredients" to binding.postIngredients.text.toString(),
                "instructions" to binding.postInstructions.text.toString()
            )

            db.collection("posts")
                .add(post)
                .addOnFailureListener {
                    Log.d("post", "Error while posting recipe")
                    Toast.makeText(context, "An error occurred while posting your recipe please try again", Toast.LENGTH_SHORT).show()
                }
                .addOnSuccessListener {
                    Log.d("post", "Recipe added to database !")
                    Toast.makeText(context, "Your recipe has been posted go and take a look!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_writePostFragment_to_homeFragment)
                }
        }

        return binding.root
    }

}