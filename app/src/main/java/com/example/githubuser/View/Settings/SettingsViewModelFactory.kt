package com.example.githubuser.View.Settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.ViewModel.MainViewModel

class SettingsViewModelFactory(private val pref: SettingsPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeSettingsViewModel::class.java)) {
            return ThemeSettingsViewModel(pref) as T
        } else if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}