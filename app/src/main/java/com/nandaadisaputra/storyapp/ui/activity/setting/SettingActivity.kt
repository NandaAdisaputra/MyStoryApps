package com.nandaadisaputra.storyapp.ui.activity.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.tos
import com.nandaadisaputra.storyapp.R
import com.nandaadisaputra.storyapp.base.activity.BaseActivity
import com.nandaadisaputra.storyapp.data.constant.Const
import com.nandaadisaputra.storyapp.data.preference.LoginPreference
import com.nandaadisaputra.storyapp.databinding.ActivitySettingBinding
import com.nandaadisaputra.storyapp.ui.activity.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity :
    BaseActivity<ActivitySettingBinding, SettingViewModel>(R.layout.activity_setting) {
    @Inject
    lateinit var preference: LoginPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAppbar()
        darkMode()
        setSupportActionBar(binding.toolbarSetting)
        //check Biometric
        if (hasBiometricCapability()) {
            tos(R.string.support_biometric)
        } else {
            tos(R.string.no_biometric ,false)
        }
        binding.hasBiometric = hasBiometricCapability()
        binding.enableBiometric = preference.getBoolean(Const.SESSION.BIOMETRIC)
        binding.switchBiometric.setOnCheckedChangeListener { _, isChecked ->
            preference.put(Const.SESSION.BIOMETRIC, isChecked)
        }
        binding.btnLanguage.setOnClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        binding.btnLogout.setOnClickListener {
            preference.logOut()
            authLogoutSuccess()
            openActivity<LoginActivity>()
            finishAffinity()
        }
    }

    private fun hasBiometricCapability(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun initAppbar() {
        val actionBar = supportActionBar
        actionBar?.title = Const.TITLE.SETTING
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}