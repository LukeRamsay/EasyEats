package com.example.easyeats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.easyeats.databinding.FragmentLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLogInBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        auth = Firebase.auth
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_in, container, false)

        binding.loginSubmit.setOnClickListener {
            auth.signInWithEmailAndPassword(binding.loginEmail.text.toString().trim(), binding.loginPassword.text.toString().trim())
                .addOnSuccessListener {
                    Log.d("Auth", "The user has been properly logged in")
                    findNavController().navigate(R.id.action_logInFragment_to_homeFragment)
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed Login Attempt: ${it.message}", Toast.LENGTH_SHORT)
                }
        }
        binding.gotoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_registerFragment)
        }

        return binding.root

    }

}