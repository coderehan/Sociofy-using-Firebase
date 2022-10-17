package com.rehan.socialmediaappfirebase.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.DataBindingUtil
import com.rehan.socialmediaappfirebase.R
import com.rehan.socialmediaappfirebase.daos.PostDAO
import com.rehan.socialmediaappfirebase.databinding.ActivityCreatePostBinding
import com.rehan.socialmediaappfirebase.databinding.ActivitySignInBinding

class CreatePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePostBinding
    private lateinit var postDAO: PostDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_post)

        postDAO = PostDAO()

        binding.btnPost.setOnClickListener {
            validation()
        }
    }

    private fun validation() {
        val post = binding.etPost.text.toString().trim()

        if(TextUtils.isEmpty(post)){
            binding.etPost.error = "Please write post"
            binding.etPost.requestFocus()
        }else{
            postDAO.addPost(post)
            finish()
        }
    }
}