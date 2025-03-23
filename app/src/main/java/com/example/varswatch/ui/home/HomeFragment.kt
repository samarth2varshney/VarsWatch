package com.example.varswatch.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.varswatch.*
import com.example.varswatch.databinding.FragmentHomeBinding
import com.example.varswatch.domain.model.SearchResults.Item
import com.example.varswatch.ui.epoxy_controller.VideosEpoxyController
import com.example.varswatch.ui.play_list.PlayListBottomFragment
import com.example.varswatch.ui.search.SearchViewModel
import com.example.varswatch.util.SharedData
import com.example.varswatch.util.SharedPrefManager
import com.example.varswatch.util.VarsFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : VarsFragment(), VideosEpoxyController.OnItemClickListener,
    PlayListBottomFragment.CardDialogListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    private val viewModel: HomeViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var dialog: PlayListBottomFragment /** BottomSheetDialogFragment for choose card */
    private lateinit var listener: PlayListBottomFragment.CardDialogListener /** Listener for choose card */

    var item: Item?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        listener = this

        binding.button2.setOnClickListener {
            signOut()
        }

        viewModel.getSubscribedChannels()

        viewModel.videoInfo.observe(viewLifecycleOwner) {
            initialize(it)
        }

        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    submit(query)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        return root
    }

    private fun submit(query: String) {
        SharedData.query = query
        val controller = findNavController()
        controller.navigate(R.id.searchFragment)
    }

    private fun initialize(videoInfo: List<Item>) {

        if(videoInfo.isEmpty()){
            binding.subscribeText.visibility = View.VISIBLE
        }else{
            binding.subscribeText.visibility = View.GONE
        }

        val controller = VideosEpoxyController(this)

        binding.apply {
            epoxyRecyclerView.setController(controller)
            controller.setData(videoInfo)
            progressBar.visibility = View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(item: Item, saveToPlayList: Boolean) {
        if(saveToPlayList){
            this.item = item
            showBottomSheetDialogFragment()
        }
        else if(item.id.videoId!="") {
            (activity as? MainActivity)?.openPlayer(item)
        }
    }

    private fun showBottomSheetDialogFragment() {
        dialog = PlayListBottomFragment(listener)
        dialog.show(parentFragmentManager, "tag")
    }

    override fun close(playListName: String) {
        searchViewModel.saveToPlayList(playListName,item!!)
        dialog.dismiss()
    }

}