package com.example.climapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun checkForInternet(context: Context): Boolean {
    // Registrar la actividad con el servicio connectivity manager
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    // Si la versión de Android es M o mayor se usa NetworkCaoabilities para verificar
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        // Devuelve un objeto de tipo Network correspondiente a la conectividad del dispositivo
        val network = connectivityManager.activeNetwork ?: return false
        // Representation of the capabilities of an active network.
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Indica si la red usa transporte WiFi o tiene conectividad WiFi
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            // Inidca si la red tiene conectividad por datos móviles
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        // Si la versión de Android es menor a M
        @Suppress("DEPRECATION") val networkInfo =
            connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}

fun translateDate(is_sp: Boolean, date: String) : String{
    if (is_sp) {
        var date = date
        date = date.replace("January", "Enero")
        date = date.replace("February", "Febrero")
        date = date.replace("March", "Marzo")
        date = date.replace("April", "Abril")
        date = date.replace("May", "Mayo")
        date = date.replace("June", "Junio")
        date = date.replace("July", "Julio")
        date = date.replace("August", "Agosto")
        date = date.replace("September", "Septiembre")
        date = date.replace("October", "Octubre")
        date = date.replace("November", "Noviembre")
        date = date.replace("December", "Diciembre")

        date = date.replace("Monday", "Lunes")
        date = date.replace("Tuesday", "Martes")
        date = date.replace("Wednesday", "Miércoles")
        date = date.replace("Thursday", "Jueves")
        date = date.replace("Friday", "Viernes")
        date = date.replace("Saturday", "Sábado")
        date = date.replace("Sunday", "Domingo")
        return date
    } else {
        return date
    }
}