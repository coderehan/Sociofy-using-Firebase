package com.rehan.socialmediaappfirebase.activities

import android.app.DownloadManager.Query
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.rehan.socialmediaappfirebase.R
import com.rehan.socialmediaappfirebase.adapters.PostAdapter
import com.rehan.socialmediaappfirebase.daos.PostDAO
import com.rehan.socialmediaappfirebase.databinding.ActivityMainBinding
import com.rehan.socialmediaappfirebase.models.Post

class MainActivity : AppCompatActivity(), PostAdapter.LikeButtonClicked {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PostAdapter
    private lateinit var postDAO: PostDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.fab.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val postCollections = postDAO.postCollection
        // Showing latest post on top of the activity
        val query = postCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions, this)
        binding.rvShowPost.adapter = adapter
        binding.rvShowPost.layoutManager = LinearLayoutManager(this)
    }

    // Telling adapter to start listening activity when there is change in database
    override fun onStart() {
        super.onStart()
        adapter.startListening()        // If there is any changes in database, adapter will update that
    }

    // Telling adapter to stop listening activity when we close the app or moved to different activity
    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeButtonClicked(postID: String) {
        postDAO.updateLikes(postID)
    }

    // Handling options on ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId.equals("Logout")) {
            FirebaseAuth.getInstance().signOut()
        }
        return super.onOptionsItemSelected(item)
    }
}