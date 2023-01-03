package com.nandaadisaputra.storyapp.ui.fragment.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.openActivity
import com.crocodic.core.helper.log.Log
import com.nandaadisaputra.storyapp.R
import com.nandaadisaputra.storyapp.base.fragment.BaseFragment
import com.nandaadisaputra.storyapp.data.constant.Const
import com.nandaadisaputra.storyapp.data.preference.LoginPreference
import com.nandaadisaputra.storyapp.data.story.StoryEntity
import com.nandaadisaputra.storyapp.databinding.FragmentStoryBinding
import com.nandaadisaputra.storyapp.databinding.ItemStoryBinding
import com.nandaadisaputra.storyapp.ui.activity.add.AddStoryActivity
import com.nandaadisaputra.storyapp.ui.activity.detail.DetailActivity
import com.nuryazid.core.base.adapter.CoreListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class StoryFragment : BaseFragment<FragmentStoryBinding>(R.layout.fragment_story) {
    private val storyApp = ArrayList<StoryEntity?>()

    @Inject
    lateinit var preference: LoginPreference
    @Inject
    lateinit var session: CoreSession
    private lateinit var viewModel: StoryViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        darkMode()
        viewModel = ViewModelProvider(this@StoryFragment)[StoryViewModel::class.java]
        setActionBar()
        swipeRefresh()
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        setupRecyclerView()
        viewModel.getListStories().observe(requireActivity()) {
            binding?.adapter = CoreListAdapter<ItemStoryBinding, StoryEntity>(R.layout.item_story)
                .initItem(storyApp) { _, data ->
                    val intent = Intent(activity,DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_USER, data)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(activity as Activity)
                    context?.startActivity(intent, optionsCompat.toBundle())
                }
        }
        binding?.fabAddStory?.setOnClickListener {
            context?.openActivity<AddStoryActivity> {  }
        }
    }

    private fun darkMode() {
        viewModel = ViewModelProvider(this@StoryFragment)[StoryViewModel::class.java]
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

    private fun swipeRefresh() {
        val token: String = preference.getString(LoginPreference.TOKEN).toString()
        Log.d("token cek :$token")
        binding?.swipeLayout?.setOnRefreshListener {
            showList(token)
            setupRecyclerView()
            binding?.swipeLayout?.isRefreshing = false
        }
    }


    private fun setupRecyclerView() {
        binding?.apply {
            rvStory.layoutManager = LinearLayoutManager(requireActivity())
            rvStory.setHasFixedSize(true)
        }
    }

    private fun showList(token: String) {
        showLoading(true)
        viewModel.getStories(token)
        fun setList(user: ArrayList<StoryEntity>) {
            storyApp.clear()
            storyApp.addAll(user)
            binding?.rvStory?.adapter?.notifyDataSetChanged()
            showLoading(false)
        }
        viewModel.getListStories().observe(requireActivity()) {
            if (it != null) {
                setList(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val token: String = preference.getString(LoginPreference.TOKEN).toString()
        showList(token)
    }

    override fun onDestroy() {
        super.onDestroy()
        val token: String = preference.getString(LoginPreference.TOKEN).toString()
        showList(token)
    }

    private fun setActionBar() {
        (activity as AppCompatActivity).supportActionBar?.title = Const.TITLE.HOME
    }

    private fun showLoading(state: Boolean) {
        binding?.progressBar?.isVisible = state
    }
    companion object {
        fun newInstance(): StoryFragment {
            val fragment = StoryFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
