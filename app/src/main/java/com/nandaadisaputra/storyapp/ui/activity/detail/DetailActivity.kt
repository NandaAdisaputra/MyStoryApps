package com.nandaadisaputra.storyapp.ui.activity.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.nandaadisaputra.storyapp.R
import com.nandaadisaputra.storyapp.base.activity.BaseActivity
import com.nandaadisaputra.storyapp.data.constant.Const
import com.nandaadisaputra.storyapp.data.story.StoryEntity
import com.nandaadisaputra.storyapp.databinding.ActivityDetailBinding
import com.nandaadisaputra.storyapp.ui.utils.ForGlide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity :
    BaseActivity<ActivityDetailBinding, DetailViewModel>(R.layout.activity_detail) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        swipeRefresh()
        darkMode()
        initView()
        setActionBar()
        binding.lifecycleOwner = this
        binding.activity = this
    }

    private fun initView() {
        val story = intent.getParcelableExtra<StoryEntity>(EXTRA_USER) as StoryEntity
        val desc = story.description
        val photo = story.photoUrl
        val name = story.name
        val date = story.createdAt
        val lat = story.lat
        val lon = story.lon

        binding.apply {
            ForGlide.loadImage(ivDetailImage, photo)
            tvDetailName.text = name
            tvDetailDescription.text = desc
            tvDetailDate.text = date
            if (lat != null || lon != null) {
                tvLatLon.text = "$lat, $lon"
                layLatLon.visibility = View.VISIBLE
            }
        }
    }
    private fun darkMode() {
        viewModel.getTheme.observe(this) { isDarkMode ->
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
    private fun swipeRefresh() {
        binding.swipeLayout.setOnRefreshListener {
            initView()
            binding.swipeLayout.isRefreshing = false
        }
    }

    private fun setActionBar() {
        supportActionBar?.title = Const.TITLE.DETAIL
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
    companion object {
        const val EXTRA_USER = "extra_user"
    }
}
