package com.example.varswatch.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drive.ui.home.video_list_adapter
import com.example.varswatch.*
import com.example.varswatch.databinding.FragmentHomeBinding
import com.example.varswatch.util.SharedData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import me.relex.circleindicator.CircleIndicator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
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

        return root
    }

    private fun sumbit(query: String) {
        SharedData.query = query
//        startActivity(Intent(requireContext(), SearchResult::class.java))
        val controller = findNavController()
        controller.navigate(R.id.searchFragment)
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