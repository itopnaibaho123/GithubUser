package com.example.githubuser.View.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.API.Response.ItemsItem
import com.example.githubuser.R
import com.example.githubuser.RecycleViewAdapter.User
import com.example.githubuser.RecycleViewAdapter.UserAdapter
import com.example.githubuser.UserDetailsActivity
import com.example.githubuser.View.Favorites.FavoritesUserActivity
import com.example.githubuser.View.Settings.SettingsPreferences
import com.example.githubuser.View.Settings.SettingsViewModelFactory
import com.example.githubuser.View.Settings.ThemeSettingsActivity
import com.example.githubuser.ViewModel.MainViewModel
import com.example.githubuser.databinding.ActivityMainBinding


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
private lateinit var mainViewModel: MainViewModel
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    private val mainViewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingsPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(this, SettingsViewModelFactory(pref)).get(MainViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)
        binding.rvGithubUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvGithubUser.addItemDecoration(itemDecoration)

        mainViewModel.listItem.observe(this) { items ->
            setGithubData(items)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBar)
        }
        mainViewModel.status.observe(this) { status ->
            status?.let {
                Toast.makeText(this, status.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        mainViewModel.getThemeSettings().observe(this) { isLightModeActive: Boolean ->
            if (isLightModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.findGithubByQuery(query)
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false;
            }
        })
        return true
    }
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.theme_setting -> {
                val intent = Intent(this@MainActivity, ThemeSettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.favorites -> {
                val intent = Intent(this@MainActivity, FavoritesUserActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }

    }

    private fun setGithubData(items: List<ItemsItem>) {
        val listGithub = ArrayList<User>()
        for (i in items) {
            val item = User(i.login, i.avatarUrl);
            listGithub.add(item)
        }

        val adapter = UserAdapter(listGithub)
        binding.rvGithubUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(data: User) {
        val moveWithParcelableIntent = Intent(this@MainActivity, UserDetailsActivity::class.java)
        moveWithParcelableIntent.putExtra(UserDetailsActivity.EXTRA_USER, data.login)
        startActivity(moveWithParcelableIntent)
    }
    fun showLoading(isLoading: Boolean, view: View) {
        if (isLoading) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }
}