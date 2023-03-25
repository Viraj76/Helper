package com.example.bottomnavigation.client.fragments


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.client.adapter.PostAdapter
import com.example.bottomnavigation.client.viewModel.HomeViewModel
import com.example.bottomnavigation.models.ClientPosts
import com.example.bottomnavigation.databinding.FragmentHomeBinding
import com.facebook.shimmer.ShimmerFrameLayout

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
        postAdapter = PostAdapter()
        binding.postRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = postAdapter
        }
    }


}