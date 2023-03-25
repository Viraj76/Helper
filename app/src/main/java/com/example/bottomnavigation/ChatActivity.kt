package com.example.bottomnavigation

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.bottomnavigation.contractor.activity.ContractorMainActivity
import com.example.bottomnavigation.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding:ActivityChatBinding
    private lateinit var chatToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingUpToolBar()

    }

    private fun settingUpToolBar() {
        binding.chatToolBar.title = "NAME"
        binding.chatToolBar.setTitleTextColor(resources.getColor(R.color.white))


        setSupportActionBar(binding.chatToolBar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back) // replace with your own back button icon
            // Set the color filter to change the color of the icon
            val color = ContextCompat.getColor(this@ChatActivity, R.color.white)
            val backArrow = ContextCompat.getDrawable(this@ChatActivity, R.drawable.back)
            backArrow?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            setHomeAsUpIndicator(backArrow)


        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                val intent  = Intent(this,ContractorMainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }
}