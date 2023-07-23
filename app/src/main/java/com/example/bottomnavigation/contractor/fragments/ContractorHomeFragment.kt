package com.example.bottomnavigation.contractor.fragments


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.bottomnavigation.contractor.RvListenerCategory
import com.example.bottomnavigation.contractor.activity.FullScreenActivity
import com.example.bottomnavigation.contractor.activity.PostFullViewActivity
import com.example.bottomnavigation.contractor.adapter.CategoryAdapter
import com.example.bottomnavigation.contractor.adapter.ContractorPostAdapter
import com.example.bottomnavigation.models.ClientPosts
import com.example.bottomnavigation.databinding.FragmentHome2Binding
import com.example.bottomnavigation.models.Category
import com.example.bottomnavigation.models.ClientLocations
import com.example.bottomnavigation.models.LocationData
import com.example.bottomnavigation.utils.Config
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

        retrievedPosts = arrayListOf()
        loadCategories()
        prepareContractorPostRecyclerView()

        loadPost("All")
        binding.searchEt.addTextChangedListener(object  : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    val query = s.toString()
                    contractorPostAdapter.filter.filter(query)
                }catch (e : java.lang.Exception){
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
//        showingPostsToContractor()
        binding.contractorHomeToolbar.inflateMenu(R.menu.client_main_activity)
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
    private fun onPostClick(postDetails : ClientPosts){
        val intent = Intent(requireContext(),PostFullViewActivity::class.java)
        intent.putExtra("title",postDetails.name)
        intent.putExtra("budget",postDetails.budget)
        intent.putExtra("postTime",postDetails.postTime)
        intent.putExtra("address",postDetails.address)
        intent.putExtra("size",postDetails.size)
        intent.putExtra("desc",postDetails.description)
        intent.putStringArrayListExtra("imagesUris",
            postDetails.imageUrls as java.util.ArrayList<String>?
        )

        startActivity(intent)

    }
    private fun loadPost(category: String) {

        databaseReference = FirebaseDatabase.getInstance().getReference("All Posts")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                retrievedPosts.clear()
                if (snapshot.exists()) {
                    for (allClientsData in snapshot.children) {
                        val clientsData = allClientsData.getValue(ClientPosts::class.java)
                        if(category == "All"){
                            retrievedPosts.add(clientsData!!)
                        }
                        else{
                            if(clientsData!!.category.equals(category)){
                                retrievedPosts.add(clientsData)
                            }
                        }
                    }
                    contractorPostAdapter.setPosts(retrievedPosts)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadCategories() {
        val categoriesList = ArrayList<Category>()
        for(i in 0 until  Config.categories.size){
            categoriesList.add(Category(Config.categories[i], Config.categoriesImages[i]))
        }
        val categoryAdapter = CategoryAdapter(requireContext(),categoriesList,object : RvListenerCategory{
            override fun onCategoryClick(category: Category) {
                loadPost(category.category)
            }

        })
        binding.rvCategories.adapter = categoryAdapter
    }

    private fun prepareContractorPostRecyclerView() {
        contractorPostAdapter = ContractorPostAdapter(requireContext(),::onPostClick)
        binding.rvContractorPost.apply {
            layoutManager  = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = contractorPostAdapter
        }
    }
}