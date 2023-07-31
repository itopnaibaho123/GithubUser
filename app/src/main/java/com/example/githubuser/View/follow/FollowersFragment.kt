package com.example.githubuser.View.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.API.Response.ItemsItem
import com.example.githubuser.RecycleViewAdapter.FollowerAdapter
import com.example.githubuser.UserDetailsActivity
import com.example.githubuser.View.ViewModelFactory
import com.example.githubuser.ViewModel.DetailViewModel
import com.example.githubuser.ViewModel.FollowerViewModel
import com.example.githubuser.databinding.FragmentFollowersBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var followersViewModel: FollowerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followersViewModel.isLoadingFollowers.observe(viewLifecycleOwner) {
            showLoading(it, binding.progressBarFollowers)
        }
        followersViewModel.listFollower.observe(viewLifecycleOwner) { listFollower ->
            setDataToFragment(listFollower)
        }

        followersViewModel.getFollower(arguments?.getString(UserDetailsActivity.EXTRA_FRAGMENT).toString())
    }


    private fun setDataToFragment(listFollower: List<ItemsItem>) {
        val listUser = ArrayList<ItemsItem>()
        with(binding) {
            for (user in listFollower) {
                listUser.clear()
                listUser.addAll(listFollower)
            }
            rvFollower.layoutManager = LinearLayoutManager(context)
            val adapter = FollowerAdapter(listFollower)
            rvFollower.adapter = adapter
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
    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }
}