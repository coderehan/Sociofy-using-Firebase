package com.rehan.socialmediaappfirebase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rehan.socialmediaappfirebase.R
import com.rehan.socialmediaappfirebase.models.Post
import com.rehan.socialmediaappfirebase.utils.Utils

class PostAdapter(options: FirestoreRecyclerOptions<Post>, val listener: LikeButtonClicked) : FirestoreRecyclerAdapter<Post, PostAdapter.ViewHolder>(
    options
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_layout, parent, false))
        viewHolder.ivLike.setOnClickListener {
            // Snapshot is used to get data from database
            // We need post id, so we use snapshot for that
            listener.onLikeButtonClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Post) {
        holder.tvUserName.text = model.createdBy.displayUserName
        holder.tvPost.text = model.text
        Glide.with(holder.ivUser.context).load(model.createdBy.userImageUrl).into(holder.ivUser)
        holder.tvLikeCount.text = model.likedBy.size.toString()
        holder.tvCreatedAt.text = Utils.getTimeAgo(model.createdAt)

        // Handling like and unlike image only
        val auth = Firebase.auth
        val currentUserID = auth.currentUser.uid
        val isLiked = model.likedBy.contains(currentUserID)
        if(isLiked){
            holder.ivLike.setImageDrawable(ContextCompat.getDrawable(holder.ivLike.context, R.drawable.icon_like))
        }else{
            holder.ivLike.setImageDrawable(ContextCompat.getDrawable(holder.ivLike.context, R.drawable.icon_unlike))
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        val tvCreatedAt: TextView = itemView.findViewById(R.id.tvCreatedAt)
        val tvPost: TextView = itemView.findViewById(R.id.tvPost)
        val tvLikeCount: TextView = itemView.findViewById(R.id.tvLikeCount)
        val ivUser: ImageView = itemView.findViewById(R.id.ivUser)
        val ivLike: ImageView = itemView.findViewById(R.id.ivLike)
    }

    interface LikeButtonClicked{
        fun onLikeButtonClicked(postID: String)
    }

}