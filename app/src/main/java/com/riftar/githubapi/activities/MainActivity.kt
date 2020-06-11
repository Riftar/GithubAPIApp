package com.riftar.githubapi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riftar.githubapi.R
import com.riftar.githubapi.adapters.UsersAdapter
import com.riftar.githubapi.db.entities.User
import com.riftar.githubapi.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import java.util.ArrayList

private const val TAG = "debug"
class MainActivity : AppCompatActivity() {
    private var itemList: ArrayList<User> = ArrayList()
    private lateinit var adapter: UsersAdapter
    private val userViewModel: UserViewModel by inject()
    private lateinit var linearLayoutManager:LinearLayoutManager
    private val lastVisibleItemPosition: Int get() = linearLayoutManager.findLastVisibleItemPosition()
    var page = 1
    var isLoading = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = UsersAdapter(itemList)
        fetchData(false)
        setupRecyclerView()

    }

    private fun fetchData(isAppend : Boolean) {
       userViewModel.getUserByQuery("rif", page, this).observe(this, Observer { items ->

          if (isAppend) {
              adapter.append(items)
          } else{
              itemList.clear()
              itemList.addAll(items)
              adapter.notifyDataSetChanged()
          }
           Log.d(TAG, "fetchData: viewmodel $page")
           items.forEachIndexed { index, item ->
               Log.d(TAG, "fetchData: ${index+1} ${item.login} page: $page")
           }
           itemList.forEachIndexed { index, item ->

               Log.d(TAG, "fetchData: ${index+1} ${item.login} page: $page")
           }
           isLoading=false

       })
        onSscrolled()
    }

    fun addData(){
        for (item in 1 .. 10) {
            val new = User("namaBaru $item", item, "https://ssl.gstatic.com/ui/v1/icons/mail/rfr/logo_gmail_lockup_default_1x.png" )
            itemList.add(new)

            adapter.notifyItemInserted(itemList.size-1)
        }
        itemList.forEachIndexed { index, item ->

            Log.d(TAG, "fetchData: ${index+1} ${item.login} page: $page")
        }
        isLoading =false
        onSscrolled()
    }
    private fun setupRecyclerView() {

        adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                selectUser(data)
            }
        })

        linearLayoutManager = LinearLayoutManager(this)
        rvUsers.layoutManager = linearLayoutManager
        rvUsers.setHasFixedSize(true)
        rvUsers.isNestedScrollingEnabled = false
        rvUsers.adapter = adapter
    }

    fun onSscrolled(){
        rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = linearLayoutManager.itemCount
                val visibleItemCount = linearLayoutManager.childCount
                Log.d(TAG, "onScrolled: $totalItemCount")
                Log.d(TAG, "onScrolled: $lastVisibleItemPosition")
                if (!isLoading && lastVisibleItemPosition == totalItemCount-1) {
                    isLoading=true
                    Log.d(TAG, "onScrollStateChanged: fetching $totalItemCount")
                    Log.d(TAG, "onScrollStateChanged: isLoading $isLoading")
                    rvUsers.removeOnScrollListener(this)
                    page++
                    Log.d(TAG, "onScrolled: $page")
                    addData()

                }
            }
        })
    }

    private fun selectUser(data: User) {
        Toast.makeText(this, data.login, Toast.LENGTH_SHORT).show()
//        val intent = Intent(this, MovieDetailActivity::class.java)
//        val bundle = Bundle()
//        bundle.putSerializable("movie", data)
//        intent.putExtras(bundle)
//        startActivity(intent)
    }
}