package com.example.easyeats

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*

class WritePostFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentWritePostBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var uri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        db = Firebase.firestore
        storage = Firebase.storage
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_post, container, false)

        binding.cancelPost.setOnClickListener {
            findNavController().navigate(R.id.action_writePostFragment_to_homeFragment)
        }

        binding.uploadPostImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        binding.submitPost.setOnClickListener {
            val post = hashMapOf(
                "userId" to auth.currentUser?.uid,
                "username" to auth.currentUser?.displayName,
                "timestamp" to Timestamp.now(),
                "title" to binding.postTitle.text.toString(),
                "description" to binding.postDescription.text.toString(),
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
                    UploadImageToStorage(it.id)
                    Toast.makeText(context, "Your recipe has been posted go and take a look!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_writePostFragment_to_homeFragment)
                }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.data!!
            Log.d("Post", "Image link: $uri")
            binding.uploadPostImage.setImageURI(uri)
        }
    }

    private fun UploadImageToStorage(postId: String) {
        val fileName = UUID.randomUUID().toString()
        Log.d("Post", "uuid: $fileName")
        val ref = storage.getReference("/images/$fileName")
        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Post", "Image link: $it")
                    saveImageToPost(it.toString(), postId)
                }
            }
            .addOnFailureListener {
                Log.d("Post", "Error uploading: $it")
            }
    }

    private fun saveImageToPost(imageUrl: String, postId: String){
        db.collection("posts").document(postId).update("headerImageUrl", imageUrl)
            .addOnSuccessListener {
                Log.d("Post", "Doc: $it")
            }
            .addOnFailureListener {
                Log.d("Post", "failed to upload image: $it")
            }
    }

}