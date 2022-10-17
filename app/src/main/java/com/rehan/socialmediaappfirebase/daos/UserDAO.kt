package com.rehan.socialmediaappfirebase.daos

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.rehan.socialmediaappfirebase.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDAO {

    // Creating Firebase Firestore instance
    private val db = FirebaseFirestore.getInstance()

    // Adding label to Firestore collection
    private val userCollection = db.collection("users")

    // Adding data into Firestore database
    fun addUser(user: User?) {
        user?.let {
            // Firestore structure i.e. collection -> document -> data(key, value)
            // Passing userId to document of firestore
            GlobalScope.launch(Dispatchers.IO) {
                userCollection.document(user.userId).set(it)
            }
        }
    }

    fun getUserById(userId: String): Task<DocumentSnapshot>{    // Document snapshot is used to get data from firestore database
        return userCollection.document(userId).get()
    }
}