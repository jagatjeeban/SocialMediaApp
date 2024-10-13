package com.example.socialmediaapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmediaap.databinding.ActivityCreatePostBinding
import com.example.socialmediaapp.daos.PostDao

class CreatePostActivity : AppCompatActivity() {

    private lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postDao = PostDao()

        binding.postButton.setOnClickListener {
            val input = binding.postInput.text.toString().trim()

            if (input.isNotEmpty()) {
                postDao.addPost(input)
                binding.postInput.text = null
                finish()
            } else {
                Toast.makeText(this, "Write something !", Toast.LENGTH_SHORT).show()
            }
        }

        binding.postCancelButton.setOnClickListener {
            finish()
        }
    }
}