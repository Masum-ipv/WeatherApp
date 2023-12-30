package com.example.weaterapp.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat

object Helper {

    fun getLocationData(context: Context): Pair<String, String> {
        val PERMISSION_CODE = 2
        var lat = "0"
        var lon = "0"

        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE)!! as LocationManager
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSION_CODE
            )
        }
        // Need to handle the permission Deny case
        val location: Location? =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (location != null) {
            lat = location.latitude.toString()
            lon = location.longitude.toString()
            Log.d("TAGY", "Lat: $lat Lon: $lon")
        } else {
            Log.e("TAGY", "No location data found")
        }
        return Pair(lat, lon)
    }

    val BASE_URL: String
        get() = "https://api.openweathermap.org/data/2.5/"
}