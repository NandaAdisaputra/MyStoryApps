package com.nandaadisaputra.storyapp.ui.activity.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.crocodic.core.extension.openActivity
import com.nandaadisaputra.storyapp.R
import com.nandaadisaputra.storyapp.base.activity.BaseActivity
import com.nandaadisaputra.storyapp.data.preference.LoginPreference
import com.nandaadisaputra.storyapp.databinding.ActivitySplashScreenBinding
import com.nandaadisaputra.storyapp.ui.activity.login.LoginActivity
import com.nandaadisaputra.storyapp.ui.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SplashScreenActivity: BaseActivity<ActivitySplashScreenBinding,SplashScreenViewModel>(R.layout.activity_splash_screen) {

    @Inject
    lateinit var preference: LoginPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        darkMode()
        playAnimation()
        if (preference.getBoolean(LoginPreference.IS_LOGIN)) {
            openActivity<MainActivity>()
            finish()
        }else{
            openActivity<LoginActivity> {  }
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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivSplashScreen, View.ROTATION, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val animeTop = ObjectAnimator.ofFloat(binding.animeTop, View.ALPHA, -30f, 30f).apply {
            duration = 6000
            repeatMode = ObjectAnimator.REVERSE
        }
        val animeTopTwo = ObjectAnimator.ofFloat(binding.animeTop, View.TRANSLATION_X, -70f, 5f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        val animeBottomTwo = ObjectAnimator.ofFloat(binding.animeBottom, View.TRANSLATION_X, 5f, -70f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        val animeBottom = ObjectAnimator.ofFloat(binding.animeBottom, View.ALPHA, -30f, 30f).apply {
            duration = 6000
            repeatMode = ObjectAnimator.REVERSE
        }
        AnimatorSet().apply {
            playSequentially(
                AnimatorSet().apply { playTogether(animeTop, animeBottom,animeTopTwo,animeBottomTwo) }
            )
            start()
        }
        val imageView =
            ObjectAnimator.ofFloat(binding.ivSplashScreen, View.ALPHA, 1f)
                .setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                imageView
            )
            startDelay = 500
        }.start()
    }
}