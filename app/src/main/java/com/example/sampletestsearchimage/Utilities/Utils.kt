package com.example.sampletestsearchimage.Utilities

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.regex.Pattern

object Utils {

    fun isValidMobileNo(num: String): Boolean {
        val MOBILE_NUMBER = "^[7-9][0-9]{9}$"
        val pattern1 = Pattern.compile(MOBILE_NUMBER)
        return pattern1.matcher(num).matches()
    }

    @JvmStatic
    fun isNullOrBlank(text: String?): Boolean {
        return text == null || text.trim { it <= ' ' }.equals(
                "",
                ignoreCase = true
        ) || text.trim { it <= ' ' }.equals("null", ignoreCase = true)
    }

    fun isNullOrEmptyList(list: List<*>?): Boolean {
        return if (list == null || list.size <= 0) true else false
    }

}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}