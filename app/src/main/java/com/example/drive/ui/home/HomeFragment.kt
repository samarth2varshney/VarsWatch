package com.example.drive.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.drive.*
import com.example.drive.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import me.relex.circleindicator.CircleIndicator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    lateinit var viewPagerAdapter: ImageSlideAdapter
    lateinit var indicator: CircleIndicator
    val db = Firebase.firestore
    var arrayList = arrayListOf("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val links = db.document("/videoLinks/links")
        links.get()
            .addOnSuccessListener { document ->
                if (document != null && document.data!=null){
                    document.data!!.keys
                    arrayList = document.data!!["arr1"] as ArrayList<String>
                    initialize(arrayList)
                    binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
                    val adapter = video_list_adapter(requireContext(), document.data)
                    binding.recyclerView2.adapter = adapter

                }
            }

        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    sumbit(query)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        binding.textView6.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            startActivity(Intent(requireContext(), Login::class.java))
        }

        return root
    }

    private fun sumbit(query: String) {
        SharedData.query = query
        startActivity(Intent(requireContext(), SearchResult::class.java))
    }

    private fun initialize(arrayList: ArrayList<String>) {
        viewPagerAdapter = ImageSlideAdapter(requireContext(),arrayList)
        binding.viewpager.adapter = viewPagerAdapter
        indicator = binding.indicator
        indicator.setViewPager(binding.viewpager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}