package com.riftar.githubapi.activities

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.riftar.githubapi.R
import com.riftar.githubapi.adapters.SearchPageListAdapter
import com.riftar.githubapi.repository.NetworkState
import com.riftar.githubapi.repository.UserPagedListRepository
import com.riftar.githubapi.rest.API
import com.riftar.githubapi.rest.APIClient
import com.riftar.githubapi.viewmodel.MainActivityViewModel
import com.riftar.githubapi.viewmodel.UserViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main2.*
import org.koin.android.ext.android.inject
import kotlin.math.log

private const val TAG = "debug"
class MainActivity2 : AppCompatActivity() {
    lateinit var userPagedListRepository: UserPagedListRepository
    lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var userPagedListAdapter: SearchPageListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val apiService: API = APIClient.getClient()
        userPagedListRepository = UserPagedListRepository(apiService)
        mainActivityViewModel = getViewModel()
        userPagedListAdapter = SearchPageListAdapter()
        val linearLayoutManager = LinearLayoutManager(this)

        Log.d(TAG, "onCreate: tes")
        if(mainActivityViewModel == null){
            Log.d(TAG, "onCreate: EMPTY")
        }
        rvUsers2.layoutManager = linearLayoutManager
        rvUsers2.setHasFixedSize(true)
        rvUsers2.adapter = userPagedListAdapter

//        mainActivityViewModel.userPagedList.observe(this, Observer {
//            Log.d(TAG, "onCreate: 1 $it.")
//            userPagedListAdapter.submitList(it)
//        })
    }

    fun fetchData(keyword:String){
        mainActivityViewModel.fetchUser(keyword).observe(this, Observer { items ->
            Log.d(TAG, "onCreate: called $items")
            for (item in items){
                Log.d(TAG, "onCreate: 2 ${item.login}")
            }
            userPagedListAdapter.submitList(items)
        })

        mainActivityViewModel.networkState.observe(this, Observer {
            progressBar.visibility = if (mainActivityViewModel.listIsEmpty() && it == NetworkState.LOADING) View.GONE else View.GONE
            tvError.visibility = if (mainActivityViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (mainActivityViewModel.listIsEmpty()!!){
                userPagedListAdapter.setNetworkState(it)
                Toast.makeText(this, "Null item", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getViewModel(): MainActivityViewModel{
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainActivityViewModel(userPagedListRepository) as T
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
                        //TODO DISPLAY LOADING
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