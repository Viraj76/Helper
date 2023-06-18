package com.example.bottomnavigation.auth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowInsets
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.bottomnavigation.R
import com.example.bottomnavigation.client.activity.ClientMainActivity
import com.example.bottomnavigation.contractor.activity.ContractorMainActivity
import com.example.bottomnavigation.models.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.IOException
import java.util.*

class SplashActivity : AppCompatActivity() {


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        hidingActionAndStatusBar()
        val currentUser = FirebaseAuth.getInstance().currentUser
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        Handler(Looper.getMainLooper()).postDelayed({
            if(currentUser != null){
                // just current user id who wants to log in
                val  currentUserId = currentUser.uid
                //initializing existedClientId and existedContractorId
                var existedClientId = "random"
                var existedContractorId = "random"
                val clientDatabaseReference = FirebaseDatabase.getInstance().getReference("Clients Id's")
                clientDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(childrenSnapshot in snapshot.children){
                            val clientsIds = childrenSnapshot.child("clientId").value.toString()
                            if(clientsIds == currentUserId){
                                existedClientId=clientsIds
                                break
                            }
                        }
                        if(existedClientId == currentUserId) {
                            val intent = Intent(this@SplashActivity, ClientMainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    override fun onCancelled(error: DatabaseError){
                        Toast.makeText(this@SplashActivity, error.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                })
                val contractorsRef = FirebaseDatabase.getInstance().getReference("Contractor Id's")
                contractorsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (childSnapshot in dataSnapshot.children) {
                            val contractorIds = childSnapshot.child("contractorId").value.toString()
                            if(contractorIds == currentUserId){
                                existedContractorId=contractorIds
                                break
                            }
                        }
                        if( existedContractorId== currentUserId){
                            val intent = Intent(this@SplashActivity, ContractorMainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(this@SplashActivity, databaseError.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else{
                startActivity(Intent(this,SIgnInActivity::class.java))
                finish()
            }
        },1000)
    }
    private fun hidingActionAndStatusBar() {
        supportActionBar?.hide()  //action  bar
        if (Build. VERSION.SDK_INT >= Build. VERSION_CODES.R) {
            val decorView = this.window.decorView
            decorView.windowInsetsController?.hide(WindowInsets. Type.statusBars())
        }
    }
    private fun getLocationName(context: Context, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1) as List<Address>
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