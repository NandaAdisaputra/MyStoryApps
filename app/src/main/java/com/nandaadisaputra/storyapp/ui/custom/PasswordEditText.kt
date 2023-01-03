package com.nandaadisaputra.storyapp.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.nandaadisaputra.storyapp.R
import com.nandaadisaputra.storyapp.data.constant.Const

class PasswordEditText : AppCompatEditText, View.OnTouchListener {

    private lateinit var passwordIcon: Drawable
    private lateinit var bg: Drawable
    private lateinit var warning: Drawable
    private lateinit var hideOrShowTextButtonImage: Drawable
    private var isPasswordHide = true
    var isValidate = false


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        hint = context.getString(R.string.enter_password)
        background =
            ContextCompat.getDrawable(context, R.drawable.bg_custom_rounded_input_edit_text)
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        transformationMethod = PasswordTransformationMethod.getInstance()
        hideOrShowTextButtonImage =
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_eye) as Drawable
        bg = ContextCompat.getDrawable(context, R.drawable.custom_input) as Drawable
        passwordIcon = ContextCompat.getDrawable(context, R.drawable.custom_password) as Drawable
        warning = ContextCompat.getDrawable(context, R.drawable.warning_password) as Drawable
        setOnTouchListener(this)

        setButtonDrawables(startOfTheText = passwordIcon, endOfTheText = hideOrShowTextButtonImage)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (text?.isEmpty() == true) {
                    error = Const.ERROR.PASSWORD
                }
                if (s.toString().length < 8) {
                    showWarning()
                } else {
                    hideWarning()
                }
            }


            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun showWarning() {
        setButtonDrawables(
            endOfTheText = hideOrShowTextButtonImage,
            bottomOfTheText = warning
        )
    }

    private fun hideWarning() {
        setButtonDrawables(endOfTheText = hideOrShowTextButtonImage)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = passwordIcon,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val hideOrShowTextButton: Float
            var isHideOrShowTextButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                hideOrShowTextButton =
                    (hideOrShowTextButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < hideOrShowTextButton -> isHideOrShowTextButtonClicked = true
                }
            } else {
                hideOrShowTextButton =
                    (width - paddingEnd - hideOrShowTextButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > hideOrShowTextButton -> isHideOrShowTextButtonClicked = true
                }
            }

            return if (isHideOrShowTextButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        if (isPasswordHide) {
                            isPasswordHide = false
                            transformationMethod = HideReturnsTransformationMethod.getInstance()
                        } else {
                            isPasswordHide = true
                            transformationMethod = PasswordTransformationMethod.getInstance()
                        }
                        true
                    }
                    else -> false
                }
            } else false
        }
        return false
    }
}