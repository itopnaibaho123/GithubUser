package com.example.githubuser.RecycleViewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.API.Response.ItemsItem
import com.example.githubuser.databinding.ItemRowUserBinding

class FollowerAdapter(private val listFollower: List<ItemsItem>) : RecyclerView.Adapter<FollowerAdapter.ViewHolder>(){
    class ViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val follower = listFollower[position]

        with(holder.binding) {
            Glide.with(root.context)
                .load(follower.avatarUrl)
                .circleCrop()
                .into(imgItemPhoto)
            tvItemName.text = follower.login
        }
    }

    override fun getItemCount(): Int = listFollower.size
}