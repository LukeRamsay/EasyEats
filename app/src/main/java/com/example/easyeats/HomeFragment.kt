package com.example.easyeats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.easyeats.data.RecipePost
import com.example.easyeats.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_write_post.*
import kotlin.math.log

import kotlinx.android.synthetic.main.fragment_all_posts.*


class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        db = Firebase.firestore
        auth = Firebase.auth
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        var adapter = GroupAdapter<GroupieViewHolder>()
        binding.allpostsRecyclerView.adapter = adapter

        db.collection("posts").get()
            .addOnSuccessListener {
                for (post in it){
                    val postResult = post.toObject<RecipePost>()
                    Log.d("Post", "${postResult}")
                    adapter.add(PostItem(postResult))
                }
            }

        auth.addAuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null ){
                Log.d("Authentication", "User is not logged in")
                findNavController().navigate(R.id.logInFragment)
            }
        }

        binding.createPost.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_writePostFragment)
        }

        return binding.root
    }
}

class PostItem(private val postItem: RecipePost) : Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.PTitle.text = postItem.title
        viewHolder.pDescription.text = postItem.description
        viewHolder.timeStamp.text = postItem.timestamp.toDate().toString()
    }

    override fun getLayout(): Int = R.layout.fragment_all_posts
}