package com.nandaadisaputra.storyapp.ui.activity.main

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.extension.openActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.nandaadisaputra.storyapp.R
import com.nandaadisaputra.storyapp.base.activity.BaseActivity
import com.nandaadisaputra.storyapp.data.preference.LoginPreference
import com.nandaadisaputra.storyapp.data.story.StoryEntity
import com.nandaadisaputra.storyapp.databinding.ActivityMainBinding
import com.nandaadisaputra.storyapp.ui.activity.add.AddStoryActivity
import com.nandaadisaputra.storyapp.ui.activity.detail.DetailActivity
import com.nandaadisaputra.storyapp.ui.activity.login.LoginActivity
import com.nandaadisaputra.storyapp.ui.activity.setting.SettingActivity
import com.nandaadisaputra.storyapp.ui.fragment.about.AboutFragment
import com.nandaadisaputra.storyapp.ui.fragment.gallery.StoryFragment
import com.nandaadisaputra.storyapp.ui.fragment.location.MapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main),
    NavigationView.OnNavigationItemSelectedListener {
    @Inject
    lateinit var preference: LoginPreference

    var content: FrameLayout? = null

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val fragment = StoryFragment.newInstance()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.location -> {
                    val fragment = MapFragment.newInstance()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.about -> {
                    val fragment = AboutFragment.newInstance()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_story, fragment, fragment.javaClass.simpleName)
            .commit()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.activity = this
        setSupportActionBar(binding.included.toolbar)
        initView()
        darkMode()
        setupListener()
        val name: String = preference.getString(LoginPreference.USERNAME).toString()
        val email: String = preference.getString(LoginPreference.EMAIL).toString()
        binding.header.nameUser.text = name
        binding.header.emailUser.text = email
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.included.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
    }
    private fun setupListener() {
        binding.header.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    when (isChecked) {
                        true -> viewModel.setTheme(true)
                        false -> viewModel.setTheme(false)
                    }
                }
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


    private fun initView() {

        binding.included.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        binding.included.navigation.circleColor = Color.RED
        val fragment = StoryFragment.newInstance()
        addFragment(fragment)

    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                preference.logOut()
                authLogoutSuccess()
                openActivity<LoginActivity> { }
                finishAffinity()
                true
            }
            R.id.setting -> {
                openActivity<SettingActivity> { }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_camera -> {
                openActivity<AddStoryActivity> { }
            }
            R.id.nav_photo -> {

            }
            R.id.nav_location -> {

            }
            R.id.nav_exit -> {
                preference.logOut()
                authLogoutSuccess()
                openActivity<LoginActivity> { }
                finishAffinity()
            }
            R.id.nav_share -> {

            }
            R.id.nav_phone -> {

            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    companion object {
        const val EXTRA_USER = "user"
    }
}
