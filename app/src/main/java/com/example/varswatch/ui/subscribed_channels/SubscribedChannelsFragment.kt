package com.example.varswatch.ui.subscribed_channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.varswatch.databinding.FragmentSubscribedChannelsBinding
import com.example.varswatch.domain.model.SearchResults.Item
import com.example.varswatch.ui.epoxy_controller.ChannelsEpoxyController
import com.example.varswatch.util.VarsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscribedChannelsFragment : VarsFragment(), ChannelsEpoxyController.OnItemClickListener {

    private var _binding: FragmentSubscribedChannelsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SubscribedChannelsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSubscribedChannelsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getSubscribedChannels()

        viewModel.channelsList.observe(viewLifecycleOwner) {
            initialize(it)
        }

        return root
    }

    private fun initialize(videoInfo: List<Item>) {
        val controller = ChannelsEpoxyController(this)

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

    override fun onItemClick(item: Item) {
        viewModel.unsubscribeToChannel(item)
    }
}