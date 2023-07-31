package com.example.githubuser.RecycleViewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.API.Response.ItemsItem
import com.example.githubuser.databinding.ItemRowUserBinding

class FollowingAdapter(private val listFollowing: List<ItemsItem>) : RecyclerView.Adapter<FollowingAdapter.ViewHolder>(){
    class ViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val following = listFollowing[position]

        with(holder.binding) {
            com.bumptech.glide.Glide.with(root.context)
                .load(following.avatarUrl)
                .circleCrop()
                .into(imgItemPhoto)
            tvItemName.text = following.login
        }
    }

    override fun getItemCount(): Int = listFollowing.size
}