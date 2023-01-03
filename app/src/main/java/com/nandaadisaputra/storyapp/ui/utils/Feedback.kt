package com.nandaadisaputra.storyapp.ui.utils

import android.content.Context
import android.widget.Toast


fun showError(isError: Boolean, context: Context, message: String) {
    if (isError) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}