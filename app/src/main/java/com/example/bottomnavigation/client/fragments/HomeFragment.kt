package com.example.bottomnavigation.client.fragments


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.R
import com.example.bottomnavigation.auth.SIgnInActivity
import com.example.bottomnavigation.auth.SplashActivity
import com.example.bottomnavigation.client.activity.ClientMainActivity
import com.example.bottomnavigation.client.adapter.PostAdapter
import com.example.bottomnavigation.client.viewModel.HomeViewModel
import com.example.bottomnavigation.databinding.FragmentHomeBinding
import com.example.bottomnavigation.models.ClientLocations
import com.example.bottomnavigation.models.ClientPosts
import com.example.bottomnavigation.models.LocationData
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    private companion object {
        const val REQUEST_LOCATION_PERMISSION = 1001
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var postAdapter: PostAdapter
    private lateinit var allClientsDataList: ArrayList<ClientPosts>
    private lateinit var shimmer: ShimmerFrameLayout
    private lateinit var viewModel: HomeViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        initializations()

//        getUserLocation()

        prepareClientPostsRecyclerView()

        getAllClientPost()

        observeClientPostLivedata()

        return binding.root
    }

    private fun getUserLocation() {
        var existedClientId = "random"
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val clientDatabaseReference = FirebaseDatabase.getInstance().getReference("Clients Location")
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
                                    val databaseReference = FirebaseDatabase.getInstance().getReference("Clients Location")
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
                            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), HomeFragment.REQUEST_LOCATION_PERMISSION
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

    private fun initializations() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        allClientsDataList = ArrayList()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

    }

    private fun observeClientPostLivedata() {
        viewModel.observeAllClientPostLiveData().observe(viewLifecycleOwner) { clientsPosts ->
            postAdapter.setPostList(clientsPosts as ArrayList<ClientPosts>)
        }
    }

    private fun getAllClientPost() {
        viewModel.getAllClientPosts()
    }

    private fun prepareClientPostsRecyclerView() {
        postAdapter = PostAdapter(requireContext())
        binding.postRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = postAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.homeToolbar.inflateMenu(R.menu.client_main_activity)
        binding.homeToolbar.setOnMenuItemClickListener() {
            when (it.itemId) {
                R.id.logOut -> {
                    val builder = AlertDialog.Builder(requireContext())
                    val alertDialog = builder.create()
                    builder.setTitle("Log Out").setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes") { dialogInterface, which ->
                            viewModel.logoutToSignUpActivity()
                            val intent = Intent(requireContext(), SIgnInActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }.setNegativeButton("No") { dialogInterface, which ->
                            alertDialog.dismiss()
                        }.show().setCancelable(false)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }


}