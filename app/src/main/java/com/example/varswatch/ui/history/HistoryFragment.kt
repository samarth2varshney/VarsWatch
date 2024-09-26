package com.example.varswatch.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.varswatch.databinding.FragmentDashboardBinding
import com.example.varswatch.util.SharedData
import com.example.varswatch.data.remote.video_info

class HistoryFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initialize(SharedData.Array)

        return root
    }

    private fun initialize(videoInfo: ArrayList<video_info>) {
        binding.historyRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        val adapter = history_video_adapter(requireContext(), videoInfo)
        binding.historyRecyclerview.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}