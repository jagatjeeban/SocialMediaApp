package com.example.socialmediaapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmediaapp.daos.PostDao
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePostActivity : AppCompatActivity() {

    private lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        postDao = PostDao()

        post_button.setOnClickListener {
            val input = post_input.text.toString().trim()

            if (input.isNotEmpty()) {
                postDao.addPost(input)
                post_input.text = null
                finish()
            } else {
                Toast.makeText(this, "Write something !", Toast.LENGTH_SHORT).show()
            }
        }

        post_cancel_button.setOnClickListener {
            finish()
        }
    }
}