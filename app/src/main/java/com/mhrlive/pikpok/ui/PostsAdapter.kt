package com.mhrlive.pikpok.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mhrlive.pikpok.R
import com.mhrlive.pikpok.data.Post

class PostsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileImage: ImageView = view.findViewById(R.id.profileImage)
        val username: TextView = view.findViewById(R.id.username)
        val postImage: ImageView = view.findViewById(R.id.postImage)
        val caption: TextView = view.findViewById(R.id.caption)
        val likesCount: TextView = view.findViewById(R.id.likesCount)
        val commentsCount: TextView = view.findViewById(R.id.commentsCount)
        val likeButton: ImageView = view.findViewById(R.id.likeButton)
        val commentButton: ImageView = view.findViewById(R.id.commentButton)
        val shareButton: ImageView = view.findViewById(R.id.shareButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        
        holder.username.text = post.username
        holder.caption.text = post.caption
        holder.likesCount.text = "${post.likesCount} likes"
        holder.commentsCount.text = "${post.commentsCount} comments"

        // Load profile image
        if (post.userProfilePicture.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(post.userProfilePicture)
                .circleCrop()
                .placeholder(R.drawable.ic_profile_placeholder)
                .into(holder.profileImage)
        }

        // Load post image
        Glide.with(holder.itemView.context)
            .load(post.imageUrl)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(holder.postImage)

        // Set click listeners
        holder.likeButton.setOnClickListener {
            // TODO: Implement like functionality
        }

        holder.commentButton.setOnClickListener {
            // TODO: Implement comment functionality
        }

        holder.shareButton.setOnClickListener {
            // TODO: Implement share functionality
        }
    }

    override fun getItemCount() = posts.size
}