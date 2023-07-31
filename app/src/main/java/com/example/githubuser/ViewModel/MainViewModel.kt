package com.example.githubuser.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubuser.API.ApiConfig
import com.example.githubuser.API.Response.GithubResponse
import com.example.githubuser.API.Response.ItemsItem
import com.example.githubuser.Event
import com.example.githubuser.View.Settings.SettingsPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingsPreferences): ViewModel() {

    private val _listItem = MutableLiveData<List<ItemsItem>>()
    val listItem: LiveData<List<ItemsItem>> = _listItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    companion object{
        private const val TAG = "MainViewModel"
        private const val GITHUB_USER = "itopnaibaho"
    }

    init {
        findGithub()
    }
    private fun findGithub() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGithub(GITHUB_USER)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listItem.value = response.body()?.items as List<ItemsItem>?
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun findGithubByQuery(q: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGithub(q);
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listItem.value = response.body()?.items as List<ItemsItem>?
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

}