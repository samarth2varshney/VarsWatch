package com.example.drive

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.drive.Query.getQuery
import com.example.drive.SharedData.observableArray
import com.example.drive.databinding.ActivityMain2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity2 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var signOutBtn: Button
    lateinit var viewPagerAdapter: ImageSlideAdapter
    lateinit var indicator: CircleIndicator
    private lateinit var binding: ActivityMain2Binding
    val db = Firebase.firestore
    var arrayList = arrayListOf("")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val links = db.document("/videoLinks/links")
        links.get()
            .addOnSuccessListener { document ->
                if (document != null && document.data!=null){
                    document.data!!.keys
                    arrayList = document.data!!["arr1"] as ArrayList<String>
                    initialize(arrayList)
                    binding.recyclerView2.layoutManager = LinearLayoutManager(this)
                    val adapter = video_list_adapter(this, document.data)
                    binding.recyclerView2.adapter = adapter

                }
            }

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val result = getQuery(query)
                    sumbit(result)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })

//        auth = FirebaseAuth.getInstance()
//        auth.signOut()

//        auth = FirebaseAuth.getInstance()
//        signOutBtn = findViewById(R.id.signOutBtn)
//
//        signOutBtn.setOnClickListener {
//
//            startActivity(Intent(this, SignInEmail::class.java))
//        }

    }

    private fun sumbit(query: String?) {
        SharedData.query = query!!
        startActivity(Intent(this,SearchResult::class.java))
    }

    private fun initialize(arrayList: ArrayList<String>) {
        viewPagerAdapter = ImageSlideAdapter(this,arrayList)
        findViewById<ViewPager>(R.id.viewpager).adapter = viewPagerAdapter
        indicator = findViewById(R.id.indicator) as CircleIndicator
        indicator.setViewPager(findViewById(R.id.viewpager))
    }

}