package com.example.lavanderia_cliente.extensions

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    val inputManager = view?.let {
        activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    inputManager?.hideSoftInputFromWindow(view?.windowToken, 0)
}