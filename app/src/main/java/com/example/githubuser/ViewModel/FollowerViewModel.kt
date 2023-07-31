package com.example.githubuser.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.API.ApiConfig
import com.example.githubuser.API.Response.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel: ViewModel() {


    private val _listFollower = MutableLiveData<List<ItemsItem>>()
    val listFollower: LiveData<List<ItemsItem>> = _listFollower


    private val _isLoadingFollowers = MutableLiveData<Boolean>()
    val isLoadingFollowers: LiveData<Boolean> = _isLoadingFollowers




    companion object{
        private const val TAG = "FollowerViewModel"
    }

    internal fun getFollower(username: String) {
        _isLoadingFollowers.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoadingFollowers.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollower.value = response.body()
                    }
                    else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoadingFollowers.value = false
                Log.e(TAG,"onFailure: ${t.message}")
            }
        })
    }


}