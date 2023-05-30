package com.example.bottomnavigation.client.fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.bottomnavigation.MyData
import com.example.bottomnavigation.R
import com.example.bottomnavigation.models.ClientPosts
import com.example.bottomnavigation.databinding.FragmentPostBinding
import com.example.bottomnavigation.utils.Config
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageViews: Array<ImageView>

    //    private lateinit var selectedImageUris: Array<Uri?>
    private var imageUris: MutableList<Uri> = mutableListOf()
    private val selectImages =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            imageUris.clear()
            imageUris.addAll(uris)
            updateImageViews()
        }

    private fun updateImageViews() {
        // Clear existing images
        for (i in 0 until 4) {
            imageViews[i].setImageURI(null)
        }
        // Load selected images
        for (i in imageUris.indices) {
            if (i >= 4) break // Limit to 4 images
            imageViews[i].setImageURI(imageUris[i])
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater)
        binding.apply {
            ivPic1.clipToOutline = true
            ivPic2.clipToOutline = true
            ivPic3.clipToOutline = true
            ivPic4.clipToOutline = true
        }
        binding.postToolbar.apply {
            title = "Post"
            (activity as AppCompatActivity).setSupportActionBar(this)
        }
        firebaseAuth = FirebaseAuth.getInstance()
        imageViews = arrayOf(binding.ivPic1, binding.ivPic2, binding.ivPic3, binding.ivPic4)

        binding.btnPickImages.setOnClickListener { selectImages.launch("image/*") }
//        selectedImageUris = arrayOfNulls(4)
        binding.btnPost.setOnClickListener { uploadImagesToFirebaseStorage() }
        return binding.root
    }

    private fun clearImageViews() {
        for (imageView in imageViews) {
            imageView.setImageDrawable(null)
        }
    }

    private fun postingNeed(uploadedImages: MutableList<String>) {
        Log.d("hh", uploadedImages.toString())
            val firstName = binding.tvIFirstName.text.toString()
            val pinCode = binding.tvPinCode.text.toString()
            val state = binding.tvState.text.toString()
            val city = binding.tvCity.text.toString()
            val briefDetail = binding.tvBriefDetail.text.toString()
            val name = firstName.trim()
            val address = "$pinCode, $state, $city, $briefDetail"
            val description = binding.tvDescription.text.toString()
            val currentUserId = firebaseAuth.currentUser?.uid
            val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())  //here dont keep date as dd/MM/yyyy ow firebase will break the date while storing
            val currentTime: String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())
            databaseReference = FirebaseDatabase.getInstance().getReference("All Posts")
            val clientDetail = ClientPosts(currentUserId,name, address, description,"$currentDate($currentTime)",uploadedImages)
            if (firstName.isNotEmpty()
                && pinCode.isNotEmpty() && state.isNotEmpty()
                && city.isNotEmpty() && description.isNotEmpty()
                && description.isNotEmpty()
            ) {
                if (currentUserId != null) {
                    databaseReference
                        .push()
                        .setValue(clientDetail)
                        .addOnSuccessListener {
                            binding.tvIFirstName.text?.clear()
                            binding.tvPinCode.text?.clear()
                            binding.tvState.text?.clear()
                            binding.tvCity.text?.clear()
                            binding.tvBriefDetail.text?.clear()
                            binding.tvDescription.text?.clear()
                            Config.hideDialog()
                            Toast.makeText(requireContext(), "Your Need is Posted!", Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(binding.root).navigate(R.id.action_postFragment_to_homeFragment)
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                                .show()

                        }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please Fill All Required Fields",
                    Toast.LENGTH_SHORT
                ).show()
            }


    }

    private fun uploadImagesToFirebaseStorage() {
        //        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
//        val storageRef = FirebaseStorage.getInstance().getReference("Posts Images").child(currentUser!!)
//        var count = 0 // Start from 0 instead of 1
//        val uploadedImages = mutableListOf<String>()
//
//        for (image in imageUris) {
//            count++ // Increment count before each image upload task
//
//            val uploadTask = storageRef.child("$count").putFile(image)
//            uploadTask.addOnSuccessListener { taskSnapshot ->
//                storageRef.child("$count").downloadUrl.addOnSuccessListener { url ->
//                    val imageUrl = url.toString()
//                    Log.d("ImageUpload", "Image $count uploaded successfully. URL: $imageUrl")
//                    uploadedImages.add(imageUrl)
//
//                }.addOnFailureListener { exception ->
//                    Log.e("ImageUpload", "Failed to get download URL for image $count: ${exception.message}")
//                    count-- // Decrement count if download URL retrieval fails
//                }
//            }.addOnFailureListener { exception ->
//                Log.e("ImageUpload", "Failed to upload image $count: ${exception.message}")
//                count-- // Decrement count if image upload fails
//            }
//        }
//        postingNeed(uploadedImages)
        Config.showDialog(requireContext())
// Initialize Firebase Storage
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference

// Initialize Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("myData")
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid

        val downloadUrls = mutableListOf<String>()

        val uploadTasks = mutableListOf<Task<Uri>>()

        imageUris.forEach { uri ->
            val imageRef = storageReference.child(currentUserID!!).child("images/${UUID.randomUUID()}")
            val uploadTask = imageRef.putFile(Uri.parse(uri.toString()))
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    imageRef.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result.toString()
                        downloadUrls.add(downloadUrl)

                        // Check if all download URLs have been obtained
                        if (downloadUrls.size == imageUris.size) {
                            // Create an instance of your data class with the download URLs
                            val myData = MyData(downloadUrls)

                            // Save the data to the Realtime Database
                            databaseReference.setValue(myData)
                            postingNeed(downloadUrls)
                        }
                    } else {
                        // Handle any errors during upload or URL retrieval
                    }
                }

            uploadTasks.add(uploadTask)
        }

        Tasks.whenAllComplete(uploadTasks)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // All images uploaded successfully
                } else {
                    // Handle errors during upload
                }
            }


    }


}

