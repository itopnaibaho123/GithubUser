package com.example.githubuser.RecycleViewAdapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.githubuser.model.database.FavoriteUser

import com.bumptech.glide.Glide
import com.example.githubuser.UserDetailsActivity
import com.example.githubuser.Utils.FavoriteDiffCallback
import com.example.githubuser.databinding.ItemRowUserBinding

class FavoriteUserAdapter  : RecyclerView.Adapter<FavoriteUserAdapter.FavoriteUserViewHolder>(){
    private val listFavorites = ArrayList<FavoriteUser>()

    fun setFavorites(favorites: List<FavoriteUser>) {
        val diffCallback = FavoriteDiffCallback(this.listFavorites, favorites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorites.clear()
        this.listFavorites.addAll(favorites)
        diffResult.dispatchUpdatesTo(this)
    }

    class FavoriteUserViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorites: FavoriteUser) {
            with(binding) {
                tvItemName.text = favorites.login

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, UserDetailsActivity::class.java)
                    intent.putExtra(UserDetailsActivity.EXTRA_USER, favorites.login)
                    itemView.context.startActivity(intent)
                }
                Glide.with(itemView.context)
                    .load(favorites.avatarUrl)
                    .circleCrop()
                    .into(imgItemPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUserViewHolder {
        val itemRowUserBinding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteUserViewHolder(itemRowUserBinding)
    }

    override fun onBindViewHolder(holder: FavoriteUserViewHolder, position: Int) {
        val favorites = listFavorites[position]
        holder.bind(favorites)
    }

    override fun getItemCount(): Int = listFavorites.size
}