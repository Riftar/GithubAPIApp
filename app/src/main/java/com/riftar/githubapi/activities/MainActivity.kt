package com.riftar.githubapi.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.riftar.githubapi.R
import com.riftar.githubapi.adapters.UsersAdapter
import com.riftar.githubapi.db.entities.User
import com.riftar.githubapi.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UsersAdapter
    private val userViewModel: UserViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = UsersAdapter()
        fetchData()
    }

    private fun fetchData() {
       userViewModel.getUserByQuery("rif", 1, this).observe(this, Observer { items ->
         setupRecyclerView(items)
       })
    }

    private fun setupRecyclerView(items:List<User>) {
        adapter.setItem(items)
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                selectUser(data)
            }
        })

        rvUsers.layoutManager = LinearLayoutManager(this)
        rvUsers.setHasFixedSize(true)
        rvUsers.isNestedScrollingEnabled = false
        rvUsers.adapter = adapter
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