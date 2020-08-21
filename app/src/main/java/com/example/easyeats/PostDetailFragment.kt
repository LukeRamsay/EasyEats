package com.example.easyeats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.easyeats.data.RecipePost
import com.example.easyeats.databinding.FragmentPostDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class PostDetailFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailBinding
    private lateinit var db: FirebaseFirestore
    private val args: PostDetailFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false)
        binding.postDetailTitle.text = args.postid
        db = Firebase.firestore

        db.collection("posts").document(args.postid).get()
            .addOnSuccessListener {
                val item = it.toObject<RecipePost>()
                if(item == null) return@addOnSuccessListener
                binding.postDetailTitle.text = item?.title
                binding.postDetailDescription.text = item?.description
                binding.postDetailTimeStamp.text = item?.timestamp?.toDate().toString()
                binding.postDetailIngredients.text = item?.ingredients
                binding.postDetailInstructions.text = item?.instructions
                if(item.headerImageUrl != ""){
                    Picasso.get().load(item.headerImageUrl).fit().centerCrop().into(binding.postHeaderImage)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error opening the recipe", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_postDetailFragment_to_homeFragment)
            }

        return binding.root
    }
}