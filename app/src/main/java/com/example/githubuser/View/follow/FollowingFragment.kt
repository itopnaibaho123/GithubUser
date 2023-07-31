package com.example.githubuser.View.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.API.Response.ItemsItem
import com.example.githubuser.RecycleViewAdapter.FollowingAdapter
import com.example.githubuser.UserDetailsActivity
import com.example.githubuser.ViewModel.DetailViewModel
import com.example.githubuser.ViewModel.FollowerViewModel
import com.example.githubuser.databinding.FragmentFollowingBinding


class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var followingViewModel: FollowingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followingViewModel.isLoadingFollowings.observe(viewLifecycleOwner) {
            showLoading(it, binding.progressBarFollowing)
        }
        followingViewModel.listFollowing.observe(viewLifecycleOwner) { listFollowing ->
            setDataToFragment(listFollowing)
        }

        followingViewModel.getFollowing(arguments?.getString(UserDetailsActivity.EXTRA_FRAGMENT).toString())
    }


    private fun setDataToFragment(listFollowing: List<ItemsItem>) {
        val listUser = ArrayList<ItemsItem>()
        with(binding) {
            for (user in listFollowing) {
                listUser.clear()
                listUser.addAll(listFollowing)
            }
            rvFollowing.layoutManager = LinearLayoutManager(context)
            val adapter = FollowingAdapter(listFollowing)
            rvFollowing.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    fun showLoading(isLoading: Boolean, view: View) {
        if (isLoading) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }
}