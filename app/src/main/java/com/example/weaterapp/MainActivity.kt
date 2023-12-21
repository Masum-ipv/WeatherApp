package com.example.weaterapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weaterapp.adapter.ForecastAdapter
import com.example.weaterapp.databinding.ActivityMainBinding
import com.example.weaterapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var locationManager: LocationManager
    private var lat: String? = null
    private var lon: String? = null
    private val PERMISSION_CODE = 2

    // https://stackoverflow.com/questions/55024079/getting-user-current-location-using-fused-location-provider
    // https://blog.devgenius.io/using-fused-location-provider-api-for-getting-location-in-android-f01034296bb
    // https://sachankapil.medium.com/latest-method-how-to-get-current-location-latitude-and-longitude-in-android-give-support-for-c5132474c864

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )

        getLocationData()

        if (!lat.isNullOrEmpty() && !lon.isNullOrEmpty()) {
            getCurrentWeather(lat!!, lon!!)
            getWeatherForecast(lat!!, lon!!)
        }

        // Make the Edit Text Clickable
        binding.cityET.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        getWeatherForecast(binding.cityET.text.trim().toString())
                }
                return p0?.onTouchEvent(event) ?: true
            }
        })
    }

    private fun getLocationData() {

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSION_CODE
            )
        }

        // Need to handle the permission Deny case
        var location: Location? =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (location != null) {
            lat = location.latitude.toString()
            lon = location.longitude.toString()
            Log.d("TAGY", "Lat: $lat Lon: $lon")
        } else {
            Log.e("TAGY", "No location data found")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please provide the permissions", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun getCurrentWeather(lat: String, lon: String) {
        GlobalScope.launch(Dispatchers.Main) {
            weatherViewModel.getCurrentWeather(lat, lon).observe(this@MainActivity, Observer {
                binding.progressBar.visibility = View.GONE
                if (it != null) {

                    Log.d("TAGY", "Current Weather City Name: $it.name")
                    binding.countryName.text = it.name

                    binding.weatherTV.text = it.weather[0].main

                    // Set the Temperature
                    var temp = String.format("%.2f", (it.main.temp - 273.15))
                    binding.tempTV.text = temp + " \u2103"

                    // Set the Icon
                    Glide.with(this@MainActivity).load(
                        "https://openweathermap.org/img/wn/" +
                                it.weather[0].icon + ".png"
                    ).into(binding.weatherIcon)
                }
            })
        }
    }

    private fun getWeatherForecast(lat: String, lon: String) {
        GlobalScope.launch(Dispatchers.Main) {
            weatherViewModel.getWeatherForecast(lat, lon).observe(this@MainActivity, Observer {
                binding.progressBar.visibility = View.GONE
                if (it != null) {

                    Log.d("TAGY", "Weather Forecast City Name: ${it.city.name}")
                    binding.countryName.text = it.city.name

                    binding.recyclerView.adapter =
                        ForecastAdapter(this@MainActivity, it.list)
                }
            })
        }
    }

    private fun getWeatherForecast(cityName: String) {
        if (cityName.isNotEmpty()) {
            Log.d("TAGY", "Cite Name: $cityName")
            GlobalScope.launch(Dispatchers.Main) {
                weatherViewModel.getWeatherForecast(cityName).observe(this@MainActivity, Observer {
                    binding.progressBar.visibility = View.GONE
                    binding.cityET.text.clear()
                    if (it != null) {

                        Log.d("TAGY", "Weather Forecast City Name: ${it.city.name}")
                        binding.countryName.text = it.city.name

                        binding.recyclerView.adapter =
                            ForecastAdapter(this@MainActivity, it.list)
                    }
                })
            }
        }
    }
}