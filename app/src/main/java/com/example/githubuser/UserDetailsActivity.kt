package com.example.githubuser

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.app.githubuser.model.database.FavoriteUser
import com.bumptech.glide.Glide
import com.example.githubuser.API.Response.UserDetailResponse
import com.example.githubuser.View.ViewModelFactory
import com.example.githubuser.View.follow.TabAdapter
import com.example.githubuser.ViewModel.DetailViewModel
import com.example.githubuser.databinding.ActivityUserDetailsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailsActivity : AppCompatActivity() {
    private var _binding: ActivityUserDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel

    private var favoriteUser: FavoriteUser? = null
    private var buttonState: Boolean = false
    private var detailUser = UserDetailResponse()
    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FRAGMENT = "extra_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Detail User Github"

        _binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

       detailViewModel = obtainViewModel(this)


        val username = intent.getStringExtra(EXTRA_USER) as String;
        setTabLayoutView(username);

        detailViewModel.detail.observe(this) { detail ->
            detailUser = detail
            setDataToView(detail)
            favoriteUser = detailUser.id?.let { FavoriteUser(it, detailUser.login) }
            detailViewModel.getAllFavorites().observe(this, {favoriteList ->
                if(favoriteList != null){
                    for(data in favoriteList){
                        if(detailUser.id == data.id){
                            buttonState = true
                            binding?.fabFavorite?.setImageDrawable(getResources().getDrawable( R.drawable.ic_favorite))
                        }
                    }
                }
            })
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBar)
        }



        binding?.fabFavorite?.setOnClickListener {
            if (!buttonState) {
                buttonState = true
                binding?.fabFavorite?.setImageDrawable(getResources().getDrawable( R.drawable.ic_favorite))
                insertToDatabase(detailUser)
            } else {
                buttonState = false
                binding?.fabFavorite?.setImageDrawable(getResources().getDrawable( R.drawable.ic_unfavorite))
                detailUser.id?.let { it1 -> detailViewModel.delete(it1) }


                showToast(this, "Favorite user has been deleted.")
            }
        }

    }

    private fun setTabLayoutView(username: String) {
        detailViewModel.getDetailUser(username);

        val login = Bundle()
        login.putString(EXTRA_FRAGMENT, username)

        val sectionPagerAdapter = TabAdapter(this, login)
        val viewPager: ViewPager2 = binding.viewPager

        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        val tabTitle = resources.getStringArray(R.array.tab_title)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToView(detailList: UserDetailResponse) {
        binding.apply {
            Glide.with(this@UserDetailsActivity)
                .load(detailList.avatarUrl)
                .circleCrop()
                .into(imageView)
            login.text = detailList.login
            name.text = detailList.name ?: "No name."
            followers.text = "${detailList.followers} Followers"
            followings.text = "${detailList.following} Following"
        }
    }
    fun showLoading(isLoading: Boolean, view: View) {
        if (isLoading) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }
    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }
    private fun insertToDatabase(detailList: UserDetailResponse) {
        favoriteUser.let { favoriteUser ->
            favoriteUser?.id = detailList.id!!
            favoriteUser?.login = detailList.login
            favoriteUser?.htmlUrl = detailList.htmlUrl
            favoriteUser?.avatarUrl = detailList.avatarUrl
            detailViewModel.insert(favoriteUser as FavoriteUser)
            showToast(this, "User has been favorited.")
        }
    }
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}