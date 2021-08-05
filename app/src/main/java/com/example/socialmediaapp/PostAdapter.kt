package com.example.socialmediaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediaapp.model.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter(options: FirestoreRecyclerOptions<Post>, private val listener: IPostAdapter) :
    FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(options) {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userImage: ImageView = itemView.findViewById(R.id.user_image)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val createdAt: TextView = itemView.findViewById(R.id.created_at)
        val postTitle: TextView = itemView.findViewById(R.id.post_title)
        val likeButton: ImageView = itemView.findViewById(R.id.like_button)
        val likesCount: TextView = itemView.findViewById(R.id.likes_count)
        val deletePostButton: ImageView = itemView.findViewById(R.id.delete_post_button)
        val likedByNames: TextView = itemView.findViewById(R.id.liked_by_names)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val postViewHolder = PostViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.post_item, parent, false)
        )

        postViewHolder.likeButton.setOnClickListener {
            listener.onItemLiked(snapshots.getSnapshot(postViewHolder.adapterPosition).id)
        }
        postViewHolder.deletePostButton.setOnClickListener {
            MaterialAlertDialogBuilder(parent.context)
                .setTitle("Do you want to delete this post ?")
                .setMessage("")
                .setCancelable(false)
                .setNegativeButton("Cancel") { _, _ -> }
                .setPositiveButton("Delete") { _, _ ->
                    listener.onItemDeleted(snapshots.getSnapshot(postViewHolder.adapterPosition).id)
                    Toast.makeText(parent.context, "post deleted !", Toast.LENGTH_SHORT).show()
                }
                .show()

        }

        return postViewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.postTitle.text = model.text
        holder.userName.text = model.createdBy.displayName
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop()
            .into(holder.userImage)
        holder.likesCount.text = model.likedBy.size.toString()
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)
        holder.likedByNames.text = "Created by ${model.createdBy.displayName}"
        holder.deletePostButton
            .setImageDrawable(
                ContextCompat.getDrawable(
                    holder.deletePostButton.context,
                    R.drawable.ic_delete_post
                )
            )


        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currentUserId)


        if (isLiked) {
            holder.likeButton
                .setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.likeButton.context,
                        R.drawable.ic_liked_1
                    )
                )
        } else {
            holder.likeButton
                .setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.likeButton.context,
                        R.drawable.ic_unliked
                    )
                )
        }
    }
}

interface IPostAdapter {
    fun onItemLiked(postId: String)
    fun onItemDeleted(postId: String)
}