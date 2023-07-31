package com.example.githubuser.API

import com.example.githubuser.API.Response.GithubResponse
import com.example.githubuser.API.Response.ItemsItem
import com.example.githubuser.API.Response.UserDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getGithub(
        @Query("q") q: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getGithubUser(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getUserFollowings(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}