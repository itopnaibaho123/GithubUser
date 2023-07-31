package com.example.githubuser.View.follow

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.githubuser.model.database.FavoriteUser
import com.app.githubuserapplication.model.repository.FavoriteUserRepository
import com.example.githubuser.API.ApiConfig
import com.example.githubuser.API.Response.ItemsItem
import com.example.githubuser.API.Response.UserDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel: ViewModel() {



    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing: LiveData<List<ItemsItem>> = _listFollowing

    private val _isLoadingFollowings = MutableLiveData<Boolean>()
    val isLoadingFollowings: LiveData<Boolean> = _isLoadingFollowings




    companion object{
        internal const val TAG = "FollowingViewModel"
    }



    internal fun getFollowing(username: String) {
        _isLoadingFollowings.value = true
        val client = ApiConfig.getApiService().getUserFollowings(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoadingFollowings.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowing.value = response.body()
                    }
                    else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoadingFollowings.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

}