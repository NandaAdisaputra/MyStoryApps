package com.nandaadisaputra.storyapp.ui.fragment.location

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.nandaadisaputra.storyapp.R
import com.nandaadisaputra.storyapp.base.fragment.BaseFragment
import com.nandaadisaputra.storyapp.databinding.FragmentMapBinding
import com.nandaadisaputra.storyapp.ui.fragment.gallery.StoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map) {
    private lateinit var viewModel: MapViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this@MapFragment)[MapViewModel::class.java]
        darkMode()

    }

    private fun darkMode() {
        viewModel = ViewModelProvider(this@MapFragment)[MapViewModel::class.java]
        viewModel.getTheme.observe(viewLifecycleOwner) { isDarkMode ->
            checkDarkMode(isDarkMode)
        }
    }

    private fun checkDarkMode(isDarkMode: Boolean) {
        when (isDarkMode) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    companion object {
        fun newInstance(): MapFragment {
            val fragment = MapFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
