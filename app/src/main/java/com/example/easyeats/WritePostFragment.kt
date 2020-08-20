package com.example.easyeats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import com.example.easyeats.databinding.FragmentWritePostBinding
import kotlinx.android.synthetic.*

class WritePostFragment : Fragment() {

    private lateinit var binding: FragmentWritePostBinding
    private lateinit var

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_post, container, false)
        return inflater.inflate(R.layout.fragment_write_post, container, false)
    }

}