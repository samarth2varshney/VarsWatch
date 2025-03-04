package com.example.varswatch.motionLayout

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.varswatch.R
import com.example.varswatch.databinding.FragmentPlayerBinding
import kotlinx.coroutines.flow.MutableStateFlow

private const val URL = "url"
private const val TITLE = "youtubetitle"

class PlayerFragment : Fragment() {

    companion object {

        val playerProgress = MutableStateFlow(0f)
        var motionLayout: MotionLayout? = null

        fun newInstance(url:String,youtubeTitle:String): PlayerFragment = PlayerFragment().also { f ->
            f.arguments = Bundle().also { b ->
                b.putString(URL, url)
                b.putString(TITLE,youtubeTitle)
            }
        }
    }

    private var _binding: FragmentPlayerBinding? = null

    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            adapter = SimpleAdapter(layoutResId = R.layout.view_item_main)
            layoutManager = LinearLayoutManager(requireContext())
        }

        motionLayout = binding.motionLayout

        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {

            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
//                if(youtubePlayer.currentMusicState!=null && progress>=0.90) {
//                    Log.i("samarth", youtubePlayer.currentMusicState!!.isPlaying.toString() + "," + progress.toString())
//                    isPlaying = youtubePlayer.currentMusicState!!.isPlaying
//                }
                playerProgress.value = progress
            }
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                when (currentId) {
                    binding.motionLayout.startState -> {
                        playerProgress.value = 0f
                    }
                    binding.motionLayout.endState -> {
                        playerProgress.value = 1f
                    }
                    else -> {
                    }
                }
            }
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }
        })

        binding.motionLayout.transitionToEnd()

        binding.videoControl.setOnClickListener {
            binding.motionLayout.transitionToEnd()
        }

        binding.closeImageView.setOnClickListener {
        }

    }

}