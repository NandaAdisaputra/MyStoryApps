package com.nandaadisaputra.storyapp.ui.activity.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.textOf
import com.crocodic.core.extension.tos
import com.nandaadisaputra.storyapp.R
import com.nandaadisaputra.storyapp.api.ApiService
import com.nandaadisaputra.storyapp.base.activity.BaseActivity
import com.nandaadisaputra.storyapp.data.constant.Const
import com.nandaadisaputra.storyapp.data.login.LoginResponse
import com.nandaadisaputra.storyapp.data.preference.LoginPreference
import com.nandaadisaputra.storyapp.databinding.ActivityLoginBinding
import com.nandaadisaputra.storyapp.ui.activity.main.MainActivity
import com.nandaadisaputra.storyapp.ui.activity.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {
    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var preference: LoginPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this
        darkMode()
        binding.lifecycleOwner = this
        binding.activity = this
        playAnimation()
        binding.hasBiometric = preference.getBoolean(Const.SESSION.BIOMETRIC)
        playAnimation()

    }
    private fun initForm() {
        val email: String = binding.edtEmail.textOf()
        val password: String = binding.edtPassword.textOf()
        login(email, password)
    }

    private fun login(email: String, password: String) {
        loadingDialog.setResponse(Const.CONS.WAITING, R.drawable.ic_android_black_24dp).show()
        apiService.login(
            email, password
        ).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                loadingDialog.dismiss()
                when (response.code()) {
                    200 -> {
                        val name: String = response.body()?.loginResult!!.name
                        val token: String = response.body()?.loginResult!!.token
                        saveSession(email, password, name, token)
                        openActivity<MainActivity> { }
                    }
                    400 -> {
                        showError(Const.ERROR.EMPATRATUS)
                    }
                    401 -> {
                        showError(Const.ERROR.EMPATRATUSSATU)
                    }
                    else -> {
                        showError(Const.ERROR.ERRORSERVER)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showError(Const.ERROR.ERRORSERVERTWO)
            }

        })
    }

    private fun saveSession(email: String, password: String, name: String, token: String) {
        preference.put(LoginPreference.EMAIL, email)
        preference.put(LoginPreference.USERNAME, name)
        preference.put(LoginPreference.PASSWORD, password)
        preference.put(LoginPreference.TOKEN, token)
        preference.put(LoginPreference.IS_LOGIN, true)
    }
    private fun showError(text: String) {
        binding.tvError.text = text
        binding.tvError.visibility = View.VISIBLE
    }
    private fun showBiometricPrompt() {
        val builder = BiometricPrompt.PromptInfo.Builder()
            .setTitle(Const.BIOMETRIC.BIOMETRICAUTH)
            .setSubtitle(Const.BIOMETRIC.BIOMETRICCREDENTIALS)
            .setDescription(Const.BIOMETRIC.BIOMETRICDESCRIPTION)

        builder.apply {
            setNegativeButtonText(Const.BIOMETRIC.CANCEL)
        }

        val promptInfo = builder.build()

        val biometricPrompt = initBiometricPrompt { success ->
            loadingDialog.show(Const.CONS.WAITING)
            if (success) {
                loadingDialog.dismiss()
                preference.getString(LoginPreference.EMAIL)
                preference.getString(LoginPreference.PASSWORD)
            } else {
                loadingDialog.dismiss()
                tos(R.string.biometric_tidak_sesuai)
            }

        }

        biometricPrompt.authenticate(promptInfo)

    }

    private fun initBiometricPrompt(listener: (success: Boolean) -> Unit): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                listener(false)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                listener(true)
            }
        }

        return BiometricPrompt(this, executor, callback)

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
                hiddenUI(true)
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                hiddenUI(false)
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageLogin, View.ROTATION, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val animeTop = ObjectAnimator.ofFloat(binding.animeTop, View.ALPHA, -30f, 30f).apply {
            duration = 6000
            repeatMode = ObjectAnimator.REVERSE
        }
        val animeTopTwo =
            ObjectAnimator.ofFloat(binding.animeTop, View.TRANSLATION_X, -70f, 5f).apply {
                duration = 2000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
        val animeBottomTwo =
            ObjectAnimator.ofFloat(binding.animeBottom, View.TRANSLATION_X, 5f, -70f).apply {
                duration = 2000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
        val animeBottom =
            ObjectAnimator.ofFloat(binding.animeBottom, View.ALPHA, -30f, 30f).apply {
                duration = 6000
                repeatMode = ObjectAnimator.REVERSE
            }
        AnimatorSet().apply {
            playSequentially(
                AnimatorSet().apply {
                    playTogether(
                        animeTop,
                        animeBottom,
                        animeTopTwo,
                        animeBottomTwo
                    )
                }
            )
            start()
        }
        val imageView =
            ObjectAnimator.ofFloat(binding.imageLogin, View.ALPHA, 1f)
                .setDuration(500)
        val textLogin =
            ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f)
                .setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f)
            .setDuration(500)
        val password =
            ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f)
                .setDuration(500)
        val btnLogin =
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f)
                .setDuration(500)
        val textRegister =
            ObjectAnimator.ofFloat(binding.lyRegisterText, View.ALPHA, 1f)
                .setDuration(500)

        val title = AnimatorSet().apply {
            playTogether(imageView, textLogin)
        }
        AnimatorSet().apply {
            playSequentially(
                title, email, password, btnLogin, textRegister
            )
            startDelay = 500
        }.start()
    }

    private fun hiddenUI(state: Boolean) {
        binding.animeTop.isInvisible = state
        binding.animeBottom.isInvisible = state
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.tvRegisterHere -> openActivity<RegisterActivity>()
            binding.btnBiometric -> showBiometricPrompt()
            binding.btnLogin -> initForm()
        }
        super.onClick(v)
    }
}