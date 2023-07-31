package com.example.githubuser.View.Favorites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.RecycleViewAdapter.FavoriteUserAdapter
import com.example.githubuser.View.ViewModelFactory
import com.example.githubuser.ViewModel.FavoriteUserViewModel
import com.example.githubuser.databinding.ActivityFavoriteUserBinding

class FavoritesUserActivity : AppCompatActivity() {
    private var _binding: ActivityFavoriteUserBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: FavoriteUserAdapter

    private lateinit var favoriteUserViewModel: FavoriteUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteUserBinding.inflate(layoutInflater)

        setContentView(binding?.root)
        supportActionBar?.title = getString(R.string.favoriteduser)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.


        favoriteUserViewModel = obtainViewModel(this@FavoritesUserActivity)
        favoriteUserViewModel.getAllFavorites().observe(this, { favoriteList ->
            if (favoriteList != null) {
                adapter.setFavorites(favoriteList)
            }
        })
        adapter = FavoriteUserAdapter()
        binding?.rvFavorites?.layoutManager = LinearLayoutManager(this)
        binding?.rvFavorites?.setHasFixedSize(false)
        binding?.rvFavorites?.adapter = adapter
    }

    /**
     * Function to return View Model Factory
     */
    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteUserViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}