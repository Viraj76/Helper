package com.example.bottomnavigation.contractor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bottomnavigation.contractor.RvListenerCategory
import com.example.bottomnavigation.databinding.RowCategoriesImagesBinding
import com.example.bottomnavigation.models.Category
import java.security.PrivateKey

class CategoryAdapter(
    private val context : Context,
    private val categoryArrayList: ArrayList<Category>,
    private  val rvListenerCategory: RvListenerCategory

) : Adapter<CategoryAdapter.CateViewHolder>() {
    class CateViewHolder(val binding : RowCategoriesImagesBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CateViewHolder {
        return CateViewHolder(RowCategoriesImagesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CateViewHolder, position: Int) {
        val category = categoryArrayList[position]
        holder.binding.apply {
            categoryIconIv.setImageResource(category.icon)
            categoryTv.text  = category.category
        }
        holder.itemView.setOnClickListener {
            rvListenerCategory.onCategoryClick(category)
        }
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }


}