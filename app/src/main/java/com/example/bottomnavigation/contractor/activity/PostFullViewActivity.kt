package com.example.bottomnavigation.contractor.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.bottomnavigation.R
import com.example.bottomnavigation.databinding.ActivityPostFullViewBinding

class PostFullViewActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostFullViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityPostFullViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val budget = intent.getStringExtra("budget")
        val postTime = intent.getStringExtra("postTime")
        val address = intent.getStringExtra("address")
        val size = intent.getStringExtra("size")
        val desc = intent.getStringExtra("desc")
        val imageUrisList = intent.getStringArrayListExtra("imagesUris")

        val imageViewList = listOf(
            binding.iv1, binding.iv2, binding.iv3, binding.iv4
        )
        binding.apply {
            titleTextView.text = title.toString()
            tvBudget.text = budget.toString()
            tvDate.text = postTime.toString()
            tvAddress.text =  address.toString()
            tvSize.text = size.toString()
            workDesc.text = desc.toString()
        }
        for (i in 0 until minOf(imageUrisList!!.size, imageViewList.size)) {
            val currentImageView = imageViewList[i]
            Glide.with(this)
                .load(imageUrisList[i])
                .into(currentImageView)
        }


        for (i in 0 until minOf(imageUrisList.size, imageViewList.size)) {
            val imageUri = imageUrisList.getOrNull(i)
            imageViewList[i].setOnClickListener {
                imageUri?.let {
                    showFullScreenImageDialog(it)
                }
            }
        }

    }
    private fun showFullScreenImageDialog(imageUri: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_full_screen)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val imageView = dialog.findViewById<ImageView>(R.id.imageViewFullScreen)
        Glide.with(this)
            .load(imageUri)
            .into(imageView)

        dialog.show()
    }

}