package com.example.sampletestraman.Utilities

import java.util.regex.Pattern

object Utils {

    fun isValidMobileNo(num: String): Boolean {
        val MOBILE_NUMBER = "^[7-9][0-9]{9}$"
        val pattern1 = Pattern.compile(MOBILE_NUMBER)
        return pattern1.matcher(num).matches()
    }

    fun isNullOrBlank(text: String?): Boolean {
        return text == null || text.trim { it <= ' ' }.equals(
            "",
            ignoreCase = true
        ) || text.trim { it <= ' ' }.equals("null", ignoreCase = true)
    }
}