package com.example.weaterapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weaterapp.adapter.ForecastAdapter
import com.example.weaterapp.databinding.ActivityMainBinding
import com.example.weaterapp.util.ApiState
import com.example.weaterapp.util.Helper.getLocationData
import com.example.weaterapp.viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel
    private lateinit var recyclerView: RecyclerView
    val PERMISSION_CODE = 2


    /*
    //TODO:
       4. Check network connectivity in okkhttp interceptor
       https://stackoverflow.com/questions/51141970/check-internet-connectivity-android-in-kotlin

       5. Update location library
           // https://stackoverflow.com/questions/55024079/getting-user-current-location-using-fused-location-provider
    // https://developer.android.com/develop/sensors-and-location/location/retrieve-current
    // https://blog.devgenius.io/using-fused-location-provider-api-for-getting-location-in-android-f01034296bb
    // https://sachankapil.medium.com/latest-method-how-to-get-current-location-latitude-and-longitude-in-android-give-support-for-c5132474c864
       6. Add offline caching
       7. Implement DI
       8. Fix UI issues
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )

        val (lat, lon) = getLocationData(this)
        if (lat.isNotEmpty() && lon.isNotEmpty()) {
            viewModel.getCurrentWeather(lat, lon)
            viewModel.getWeatherForecast(lat, lon)
        }

        // Make the Edit Text Clickable, call for weather forecast
        binding.cityET.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val city = binding.cityET.text.trim().toString()
                        if (!city.isNullOrEmpty()) {
                            Log.d("TAGY", "City Name: $city")
                            viewModel.getWeatherForecast(city)
                        }
                    }
                }
                return p0?.onTouchEvent(event) ?: true
            }
        })

        // Get current weather data
        viewModel.currentWeather.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                when (it) {
                    is ApiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is ApiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("TAGY", "Current Weather City Name: " + it.data!!.name)
                        binding.countryName.text = it.data.name

                        binding.weatherTV.text = it.data.weather[0].main

                        // Set the Temperature
                        var temp = String.format("%.2f", (it.data.main.temp - 273.15))
                        binding.tempTV.text = temp + " \u2103"

                        // Set the Icon
                        Glide.with(this@MainActivity).load(
                            "https://openweathermap.org/img/wn/" +
                                    it.data.weather[0].icon + ".png"
                        ).into(binding.weatherIcon)
                    }

                    is ApiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })

        // Get weather forecast data
        viewModel.weatherForecast.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                when (it) {
                    is ApiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is ApiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.cityET.text.clear()
                        Log.d("TAGY", "Weather Forecast City Name: ${it.data!!.city.name}")
                        binding.countryName.text = it.data.city.name
                        binding.recyclerView.adapter =
                            ForecastAdapter(this@MainActivity, it.data.list)
                    }

                    is ApiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
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
}