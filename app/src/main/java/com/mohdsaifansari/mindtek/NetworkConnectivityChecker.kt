package com.mohdsaifansari.mindtek

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class NetworkConnectivityChecker(private val context: Context) {
    fun isNetworkAvailable(): Flow<Boolean> = callbackFlow {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.d("NetworkChecker", "Network available")
                trySend(true)
            }

            override fun onLost(network: Network) {
                Log.d("NetworkChecker", "Network lost")
                trySend(false)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                trySend(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
            }
        }

        val request =
            NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
        connectivityManager.registerNetworkCallback(request, callback)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }

    }
}
