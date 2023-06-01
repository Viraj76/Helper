package com.example.bottomnavigation.client.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bottomnavigation.models.ClientPosts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeViewModel : ViewModel() {

    private lateinit var databaseReference: DatabaseReference
    private  var allClientPostsLiveData =MutableLiveData<List<ClientPosts>>()

    fun getAllClientPosts() {
        databaseReference = FirebaseDatabase.getInstance().getReference("All Posts")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                     val allClientsDataList= ArrayList<ClientPosts>()
                    for (allClientsData in snapshot.children) {
                        val clientsData = allClientsData.getValue(ClientPosts::class.java)
                        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
                        if (currentUser != clientsData?.userId) {
                            allClientsDataList.add(clientsData!!)
                        }
                    }
                    allClientPostsLiveData.postValue(allClientsDataList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Error",error.message)
            }
        })
    }

    fun observeAllClientPostLiveData(): LiveData<List<ClientPosts>>{
        return allClientPostsLiveData
    }

    fun logoutToSignUpActivity(){
        FirebaseAuth.getInstance().signOut()
    }
}