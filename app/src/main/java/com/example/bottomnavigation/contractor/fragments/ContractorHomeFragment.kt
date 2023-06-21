package com.example.bottomnavigation.contractor.fragments


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.bottomnavigation.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.auth.SIgnInActivity
import com.example.bottomnavigation.client.activity.ClientMainActivity
import com.example.bottomnavigation.client.fragments.HomeFragment
import com.example.bottomnavigation.contractor.adapter.ContractorPostAdapter
import com.example.bottomnavigation.models.ClientPosts
import com.example.bottomnavigation.databinding.FragmentHome2Binding
import com.example.bottomnavigation.models.ClientLocations
import com.example.bottomnavigation.models.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class ContractorHomeFragment : Fragment() {

    private lateinit var binding:FragmentHome2Binding
    private lateinit var contractorPostAdapter: ContractorPostAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var retrievedPosts : ArrayList<ClientPosts>
    private companion object {
        const val REQUEST_LOCATION_PERMISSION = 1001
    }
    private lateinit var context:Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHome2Binding.inflate(inflater)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
//        getUserLocation()
        retrievedPosts = arrayListOf()
        prepareContractorPostRecyclerView()
        showingPostsToContractor()
        binding.contractorHomeToolbar.inflateMenu(R.menu.client_main_activity)
//        binding.ivSearchArrow.setOnClickListener {
//            val searchQuery = binding.etSearch.text.toString().trim().lowercase()
//            Log.d("dd",searchQuery)
//            searchPost(searchQuery)
//        }
        binding.contractorHomeToolbar.setOnMenuItemClickListener(){
            when(it.itemId){
                R.id.logOut ->{
                    val builder = AlertDialog.Builder(requireContext())
                    val alertDialog = builder.create()
                    builder
                        .setTitle("Log Out")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes"){dialogInterface,which->
                            FirebaseAuth.getInstance().signOut()
                            val intent = Intent(requireContext(), SIgnInActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                        .setNegativeButton("No"){dialogInterface, which->
                            alertDialog.dismiss()
                        }
                        .show()
                        .setCancelable(false)
                    true
                }
                else -> {false}
            }
        }
    }

    private fun searchPost(searchQuery: String) {
        val database = FirebaseDatabase.getInstance()
        val postsRef = database.getReference("All Posts")
        val query = postsRef.orderByChild("name").startAt(searchQuery).endAt(searchQuery + "\uf8ff")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear your existing list of posts
                retrievedPosts.clear()
                for (snapshot in dataSnapshot.children) {
                    val post = snapshot.getValue(ClientPosts::class.java)
                    Log.d("pp",post.toString())
                    post?.let {
                        // Additional filtering based on the address attribute
//                        if (it.address!!.contains(searchQuery, ignoreCase = true)) {
                            Log.d("pppp",retrievedPosts.toString())
                            retrievedPosts.add(it)
                    }
                }
                contractorPostAdapter.setPosts(retrievedPosts)
                // Notify the adapter that the data has changed
                contractorPostAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
            }
        })
    }
    private fun showingPostsToContractor() {
        databaseReference = FirebaseDatabase.getInstance().getReference("All Posts")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                retrievedPosts.clear()
                if (snapshot.exists()) {
                    for (allClientsData in snapshot.children) {
                        val clientsData = allClientsData.getValue(ClientPosts::class.java)
                        retrievedPosts.add(clientsData!!)
                    }
                    contractorPostAdapter.setPosts(retrievedPosts)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun prepareContractorPostRecyclerView() {
        contractorPostAdapter = ContractorPostAdapter(requireContext())
        binding.rvContractorPost.apply {
            layoutManager  = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = contractorPostAdapter
        }
    }
    private fun getUserLocation() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val clientDatabaseReference = FirebaseDatabase.getInstance().getReference("Contractors Location")
        clientDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChild(currentUserId!!)) {
                    // New user
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                            if (location != null) {
                                val latitude = location.latitude
                                val longitude = location.longitude
                                val locationName = getLocationName(requireContext(), latitude, longitude)
                                val clientAddress = ClientLocations(currentUserId,locationName)
                                val locationData = LocationData(latitude, longitude)
                                val databaseReference = FirebaseDatabase.getInstance().getReference("Contractors Location")
                                databaseReference.child(currentUserId).setValue(clientAddress)
                                    .addOnSuccessListener {
                                        startActivity(Intent(requireContext(), ClientMainActivity::class.java))
                                    }.addOnFailureListener { e ->
                                        Toast.makeText(requireContext(), "Failed to store location data: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION
                        )
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
    private fun getLocationName(context: Context, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            val addresses: List<Address> =
                geocoder.getFromLocation(latitude, longitude, 1) as List<Address>
            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                return address.getAddressLine(0)
            }
        } catch (e: IOException) {
            Log.e("Geocoding", "Error getting location name: ${e.message}")
        }
        return null
    }
}