package com.nandaadisaputra.storyapp.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.nandaadisaputra.storyapp.R
import com.nandaadisaputra.storyapp.data.constant.Const

class DescriptionEditText: AppCompatEditText, View.OnTouchListener {

    private lateinit var clearButtonImage: Drawable
    private lateinit var descIcon: Drawable
    private lateinit var bg: Drawable
    private lateinit var warning: Drawable

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
        background = bg
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        bg = ContextCompat.getDrawable(context, R.drawable.custom_input) as Drawable
        descIcon = ContextCompat.getDrawable(context, R.drawable.custom_desc) as Drawable
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
        warning = ContextCompat.getDrawable(context, R.drawable.warning_description) as Drawable
        setOnTouchListener(this)

        setButtonDrawables(startOfTheText = descIcon)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (text?.isEmpty() == true) {
                    error = Const.ERROR.DESCRIPTION
                }
                if (s.toString().length < 8){
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
        setButtonDrawables(endOfTheText = clearButtonImage, bottomOfTheText = warning)
    }

    private fun hideWarning() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = descIcon,
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
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage =
                            ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearButtonImage =
                            ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }


}