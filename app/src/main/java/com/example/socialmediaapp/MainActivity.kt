package com.example.socialmediaapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmediaapp.daos.PostDao
import com.example.socialmediaapp.model.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IPostAdapter {

    private lateinit var adapter: PostAdapter
    private lateinit var postDao: PostDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        postDao = PostDao()

        //getting recyclerViewOptions ready
        val postCollections = postDao.postCollection
        val query = postCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions, this)

        recyclerView.adapter = adapter
    }

    /**
     * Adding listeners to the adapter
     */
    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onItemLiked(postId: String) {
        postDao.updateLikes(postId)
    }

    override fun onItemDeleted(postId: String) {
        postDao.deletePost(postId)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        showSignOutDialog()
        return super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        Firebase.auth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    private fun showSignOutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Are you sure ?")
            .setMessage("You will be signed out from the app.")
            .setCancelable(false)
            .setNegativeButton("Cancel") { _, _ -> }
            .setPositiveButton("Yes") { _, _ -> signOut() }
            .show()
    }
}