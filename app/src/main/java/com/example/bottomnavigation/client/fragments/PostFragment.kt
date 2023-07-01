package com.example.bottomnavigation.client.fragments


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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var imageViews: Array<ImageView>

    private var imageUris: MutableList<Uri> = mutableListOf()


    private val selectImages =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            imageUris.clear()
            imageUris.addAll(uris)
            updateImageViews()
        }

    private fun updateImageViews() {
        for (i in 0 until 4) {
            if (i < imageUris.size) {
                imageViews[i].setImageURI(imageUris[i])
                showCutButton(i)
            } else {
                imageViews[i].setImageURI(null)
                hideCutButton(i)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater)

        initializations()
        applyingCornerRadiusToImageViews()
        binding.btnCut1.visibility = View.GONE
        binding.btnCut2.visibility = View.GONE
        binding.btnCut3.visibility = View.GONE
        binding.btnCut4.visibility = View.GONE
        binding.apply {
            btnPickImages.setOnClickListener {
                selectImages.launch("image/*")
            }
            btnPost.setOnClickListener { uploadImagesToFirebaseStorage() }
            btnCut1.setOnClickListener {
                imageViews[0].setImageResource(R.drawable.ic_baseline_image_24)
                btnCut1.visibility = View.GONE
            }
            btnCut2.setOnClickListener {
                imageViews[1].setImageResource(R.drawable.ic_baseline_image_24)
                btnCut2.visibility = View.GONE
            }
            btnCut3.setOnClickListener {
                imageViews[2].setImageResource(R.drawable.ic_baseline_image_24)
                btnCut3.visibility = View.GONE
            }
            btnCut4.setOnClickListener {
                imageViews[3].setImageResource(R.drawable.ic_baseline_image_24)
                btnCut4.visibility = View.GONE
            }
        }


        return binding.root
    }
    private fun showCutButton(index: Int) {
        when (index) {
            0 -> binding.btnCut1.visibility = View.VISIBLE
            1 -> binding.btnCut2.visibility = View.VISIBLE
            2 -> binding.btnCut3.visibility = View.VISIBLE
            3 -> binding.btnCut4.visibility = View.VISIBLE
        }
    }

    private fun hideCutButton(index: Int) {
        when (index) {
            0 -> binding.btnCut1.visibility = View.GONE
            1 -> binding.btnCut2.visibility = View.GONE
            2 -> binding.btnCut3.visibility = View.GONE
            3 -> binding.btnCut4.visibility = View.GONE
        }
    }
    private fun applyingCornerRadiusToImageViews() {
        binding.apply {
            ivPic1.clipToOutline = true
            ivPic2.clipToOutline = true
            ivPic3.clipToOutline = true
            ivPic4.clipToOutline = true
        }
    }

    private fun initializations() {
        firebaseAuth = FirebaseAuth.getInstance()
        imageViews = arrayOf(binding.ivPic1, binding.ivPic2, binding.ivPic3, binding.ivPic4)
    }

    private fun postingNeed(uploadedImages: MutableList<String>) {
        Log.d("hh", uploadedImages.toString())
        val needType = binding.tvIFirstName.text.toString()
        val budget  = binding.tvBudget.text.toString()
        val size = binding.tvSize.text.toString()
        val state = binding.tvState.text.toString()
        val city = binding.tvCity.text.toString()
        val address = "$city, ($state)"
        val name = needType.trim()
        val description = binding.tvDescription.text.toString()
        val currentUserId = firebaseAuth.currentUser?.uid
        val currentDate: String = SimpleDateFormat(
            "dd-MM-yyyy", Locale.getDefault()
        ).format(Date())  //here dont keep date as dd/MM/yyyy ow firebase will break the date while storing
        val currentTime: String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())
        databaseReference = FirebaseDatabase.getInstance().getReference("All Posts")
        val clientDetail = ClientPosts(currentUserId!!, name,budget,size, address, description, "$currentDate($currentTime)", uploadedImages)
        if (needType.isNotEmpty() && state.isNotEmpty() && city.isNotEmpty() && description.isNotEmpty() && description.isNotEmpty()) {
            databaseReference.push().setValue(clientDetail).addOnSuccessListener {
                binding.tvIFirstName.text?.clear()
                binding.tvState.text?.clear()
                binding.tvCity.text?.clear()
                binding.tvDescription.text?.clear()
                Config.hideDialog()
                Toast.makeText(requireContext(), "Your Need is Posted!", Toast.LENGTH_SHORT)
                    .show()
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_postFragment_to_homeFragment)
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(
                requireContext(), "Please Fill All Required Fields", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun uploadImagesToFirebaseStorage() {
        if (imageUris.isEmpty()) {
            // No images selected, directly call postingNeed() function
            postingNeed(mutableListOf())
            return
        }
        Config.showDialog(requireContext())
        val storageReference = FirebaseStorage.getInstance().reference
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val downloadUrls = mutableListOf<String>()
        Log.d("ii", imageUris.toString())
        imageUris.forEach { uri ->
            val imageRef =
                storageReference.child(currentUserID!!).child("images/${UUID.randomUUID()}")
            imageRef.putFile(Uri.parse(uri.toString()))
                .continueWithTask {
                    imageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result.toString()
                        downloadUrls.add(downloadUrl)
                        if (downloadUrls.size == imageUris.size) postingNeed(downloadUrls)
                    } else Toast.makeText(
                        requireContext(),
                        task.exception.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}

