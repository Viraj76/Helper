package com.example.bottomnavigation.client.fragments


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.R
import com.example.bottomnavigation.auth.SIgnInActivity
import com.example.bottomnavigation.client.adapter.PostAdapter
import com.example.bottomnavigation.client.viewModel.HomeViewModel
import com.example.bottomnavigation.databinding.FragmentHomeBinding
import com.example.bottomnavigation.models.ClientPosts
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var postAdapter: PostAdapter
    private lateinit var allClientsDataList: ArrayList<ClientPosts>
    private lateinit var shimmer: ShimmerFrameLayout
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        initializations()

        prepareClientPostsRecyclerView()

        getAllClientPost()

        observeClientPostLivedata()

        return binding.root
    }


    private fun initializations() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        allClientsDataList = ArrayList()
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
        binding.homeToolbar.setOnMenuItemClickListener(){
                when(it.itemId){
                    R.id.logOut ->{
                        val builder = AlertDialog.Builder(requireContext())
                        val alertDialog = builder.create()
                        builder
                            .setTitle("Log Out")
                            .setMessage("Are you sure you want to log out?")
                            .setPositiveButton("Yes"){dialogInterface,which->
                                viewModel.logoutToSignUpActivity()
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



}