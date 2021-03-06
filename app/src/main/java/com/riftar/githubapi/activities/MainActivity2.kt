package com.riftar.githubapi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.riftar.githubapi.R
import com.riftar.githubapi.adapters.SearchPageListAdapter
import com.riftar.githubapi.db.entities.User
import com.riftar.githubapi.repository.NetworkState
import com.riftar.githubapi.rest.API
import com.riftar.githubapi.rest.APIClient
import com.riftar.githubapi.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var userPagedListAdapter: SearchPageListAdapter
    lateinit var apiService:API
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        apiService = APIClient.getClient()
        mainActivityViewModel = getViewModel()
        userPagedListAdapter = SearchPageListAdapter()
        val linearLayoutManager = LinearLayoutManager(this)

        rvUsers2.layoutManager = linearLayoutManager
        rvUsers2.setHasFixedSize(true)
        rvUsers2.adapter = userPagedListAdapter
        userPagedListAdapter.setOnItemClickCallback(object :
            SearchPageListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User?) {
                Toast.makeText(this@MainActivity2, "${data?.login}", Toast.LENGTH_SHORT).show()
            }
        })

        mainActivityViewModel.isFetched.observe(this, Observer {
            if (it){
                observeData()
            }
        })
    }

    fun fetchData(keyword:String){
        mainActivityViewModel.fetchLiveUserPagedList(keyword)
        observeData()
    }

    private fun observeData() {
        mainActivityViewModel.userPagedList.observe(this, Observer {
            if (it != null)
            {
                userPagedListAdapter.submitList(it)
            }
        })
        mainActivityViewModel.getNetworkState().observe(this, Observer {
            progressBar.visibility = if (mainActivityViewModel.listIsEmpty() && it == NetworkState.LOADING) View.GONE else View.GONE
            tvError.visibility = if ( it == NetworkState.ERROR) View.VISIBLE else View.GONE
            ivNotFound.visibility = if (mainActivityViewModel.listIsEmpty() && it == NetworkState.LOADED) View.VISIBLE else  View.GONE

            userPagedListAdapter.setNetworkState(it)
        })
    }

    private fun getViewModel(): MainActivityViewModel{
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainActivityViewModel( apiService ) as T
            }
        })[MainActivityViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchItem = menu?.findItem(R.id.search)
        if (searchItem != null){
            val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
            searchView.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        fetchData(query)
                    }
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                   return false
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }
}