package com.example.sampletestsearchimage.Utilities

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.JsonSyntaxException
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.net.ssl.HttpsURLConnection

object NetworkHelper {
    private const val TAG = "NetworkHelper"



    fun isNetworkConnected( context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }

     fun handleNetworkError(err: Throwable?,context: Context) =
        err?.let {throwables ->
          castToNetworkError(throwables).run {
                when (this) {
                    -1 ->   Toaster.show(context,"Network error")
                    0 -> Toaster.show(context,"server connection error")
                    HttpsURLConnection.HTTP_UNAUTHORIZED -> {
                        Toaster.show(context,"permission denied")
                    }
                    HttpsURLConnection.HTTP_INTERNAL_ERROR ->
                        Toaster.show(context,"network internal error")
                    HttpsURLConnection.HTTP_UNAVAILABLE ->
                        Toaster.show(context,"network server not available")
                    else -> Toaster.show(context,"Something wemt wrong")
                }
            }
        }

    fun castToNetworkError(throwable: Throwable): Int {
        val defaultNetworkError: Int = -1
        Logger.e("NetworkHelper", throwable.toString())

        try {
            if (throwable is ConnectException) return 0
            if (throwable !is HttpException) return defaultNetworkError
            if (throwable !is SocketTimeoutException) return defaultNetworkError

            return throwable.code()
        } catch (e: IOException) {
            Logger.e(TAG, e.toString())
        } catch (e: JsonSyntaxException) {
            Logger.e(TAG, e.toString())
        } catch (e: NullPointerException) {
            Logger.e(TAG, e.toString())
        }
        return defaultNetworkError
    }


}