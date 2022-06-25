package com.example.android.nextreminder.ui.main

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android.nextreminder.R
import com.example.android.nextreminder.databinding.FragmentRandomBinding
import com.example.android.nextreminder.ui.detail.DetailActivity
import com.example.android.nextreminder.utils.ShakeDetector
import com.example.android.nextreminder.utils.ShakeListener
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RandomFragment : Fragment() {

    private val viewModel: MainViewModel by sharedViewModel()
    private lateinit var binding: FragmentRandomBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var shakeDetector: ShakeDetector

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setObservers()

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        shakeDetector = ShakeDetector(
            ShakeListener {
                viewModel.getRandomBookmark()
            }
        )

        // Make the phone image to shake (vibrate) indefinitely as a cue to the user
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        binding.randomShakingImage.startAnimation(animation)

        return binding.root
    }

    private fun setObservers() {
        viewModel.moveToDetail.observe(viewLifecycleOwner) { randomItem ->
            if (randomItem == null) return@observe
            DetailActivity.startActivity(requireContext(), randomItem)
            viewModel.moveFinished()
        }

        viewModel.displayErrorToast.observe(viewLifecycleOwner) { messageStringResource ->
            if (messageStringResource == null) return@observe
            Toast.makeText(requireContext(), messageStringResource, Toast.LENGTH_SHORT).show()
            viewModel.toastDisplayed()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(shakeDetector)
    }
}