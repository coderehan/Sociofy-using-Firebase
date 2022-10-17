package com.rehan.socialmediaappfirebase.daos

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.rehan.socialmediaappfirebase.models.Post
import com.rehan.socialmediaappfirebase.models.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDAO {
    // Creating Firebase Firestore instance
    private val db = FirebaseFirestore.getInstance()

    // Adding label to Firestore collection
    val postCollection = db.collection("posts")

    private val auth = Firebase.auth

    // Adding data into Firestore database
    fun addPost(text: String){
        val currentUserId = auth.currentUser!!.uid      // !! denotes it is not null for sure and here we are getting current user id
        GlobalScope.launch {
            val userDAO = UserDAO()
            val user = userDAO.getUserById(currentUserId).await().toObject(User::class.java)!!
            val currentTime = System.currentTimeMillis()
            val post = Post(text, user, currentTime)
            postCollection.document().set(post)
        }
    }

    // Whenever we want something from database, we use Task (snapshot)
    fun getPostById(postId: String): Task<DocumentSnapshot>{
        return postCollection.document(postId).get()    // Now we got the document id
    }

    fun updateLikes(postId: String){
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid      // Getting current user id
            val post = getPostById(postId).await().toObject(Post::class.java)!!
            val isLiked = post.likedBy.contains(currentUserId)

            if(isLiked){
                post.likedBy.remove(currentUserId)
            }else{
                post.likedBy.add(currentUserId)
            }
            postCollection.document(postId).set(post)
        }

    }
}