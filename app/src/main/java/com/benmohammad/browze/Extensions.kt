package com.benmohammad.browze

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import mozilla.components.browser.session.SessionManager

fun Context.isConnected(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M ) {
        val n = cm.activeNetwork
        if(n != null) {
            val nc = cm.getNetworkCapabilities(n)
            return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            )
        }
        return false
    } else {
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}

fun SessionManager.onBackPressed(): Boolean = when (this.selectedSession?.canGoBack) {
    true -> true.also { this.getOrCreateEngineSession(this.selectedSession!!).goBack()}
    else -> false
}


val Context.app: App get() = applicationContext as App