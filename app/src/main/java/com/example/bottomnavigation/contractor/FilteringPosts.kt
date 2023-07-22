package com.example.bottomnavigation.contractor

import android.widget.Adapter
import android.widget.Filter
import com.example.bottomnavigation.contractor.adapter.ContractorPostAdapter
import com.example.bottomnavigation.models.ClientPosts
import java.util.*
import kotlin.collections.ArrayList

class FilteringPosts(
     private val adapter: ContractorPostAdapter,
     private val filterList: ArrayList<ClientPosts>
) : Filter() {
     override fun performFiltering(constraint: CharSequence?): FilterResults {
          val results = FilterResults()
          if (!constraint.isNullOrEmpty()) {
               val query = constraint.toString().trim().uppercase(Locale.getDefault())
               val keywords = query.split(" ")

               val filteredPosts = ArrayList<ClientPosts>()

               for (post in filterList) {
                    if (keywords.any { keyword ->
                              post.name?.uppercase(Locale.getDefault())?.contains(keyword) == true ||
                                      post.address?.uppercase(Locale.getDefault())?.contains(keyword) == true ||
                                      post.category?.uppercase(Locale.getDefault())?.contains(keyword) == true
                         }
                    ) {
                         filteredPosts.add(post)
                    }
               }

               results.count = filteredPosts.size
               results.values = filteredPosts
          } else {
               results.count = filterList.size
               results.values = filterList
          }

          return results
     }

     override fun publishResults(constraint: CharSequence?, results: FilterResults) {
          adapter.contractorPostList = results.values as ArrayList<ClientPosts>
          adapter.notifyDataSetChanged()
     }
}
