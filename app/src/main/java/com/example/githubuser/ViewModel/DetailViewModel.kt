package com.example.githubuser.ViewModel

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

class DetailViewModel(application: Application): ViewModel() {

    private val _detail = MutableLiveData<UserDetailResponse>()
    val detail: LiveData<UserDetailResponse> = _detail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)


    companion object{
        internal const val TAG = "DetailViewModel"
    }

    fun getAllFavorites(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavorites()

    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGithubUser(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detail.value = response.body();
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


    fun insert(user: FavoriteUser) {
        mFavoriteUserRepository.insert(user)
    }

    fun delete(id: Int) {
        mFavoriteUserRepository.delete(id)
    }
}