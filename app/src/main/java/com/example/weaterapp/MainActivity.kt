package com.example.weaterapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import com.example.weaterapp.util.Constants.IMAGE_URL
import com.example.weaterapp.util.Helper.getLocationData
import com.example.weaterapp.viewmodel.DataStoreViewModel
import com.example.weaterapp.viewmodel.WeatherViewModel
import com.example.weaterapp.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/*
TODO: 1. Change temp with the change of Unit
2. save last known location in DataStore
3. Add network connectivity check
4. Add Push Notification(Also update CV)
*/
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var dataViewModel: DataStoreViewModel
    private lateinit var recyclerView: RecyclerView
    val PERMISSION_CODE = 2

    @Inject
    lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        weatherViewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]
        dataViewModel = ViewModelProvider(this, factory)[DataStoreViewModel::class.java]
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )

        // Get current city lat long
        val (lat, lon) = getLocationData(this)
        Log.d("TAGY", "Current city: Lat $lat Lon $lon")
        weatherViewModel.getCurrentWeather(lat.toString(), lon.toString())
        weatherViewModel.getWeatherForecast(lat.toString(), lon.toString())

        // Get Temperature Unit
        dataViewModel.getTempUnit()
        dataViewModel.tempUnit.observe(this) { unit ->
            Log.d("TAGY", "Temperature Unit: $unit")
            if (unit != null) {
                if (unit) {
                    binding.toggleSwitch.isChecked = true
                    binding.toggleSwitch.text = "\u2103"
                } else {
                    binding.toggleSwitch.isChecked = false
                    binding.toggleSwitch.text = "\u2109"
                }
            }
        }

        // After click Search Icon, call for weather forecast
        binding.searchIcon.setOnClickListener {
            val city = binding.cityET.text.trim().toString()
            if (city.isNotEmpty()) {
                Log.d("TAGY", "City Name: $city")
                weatherViewModel.getCurrentWeather(city)
                weatherViewModel.getWeatherForecast(city)
            }
        }

        // Clicking on toggle switch button to change Temp Unit
        binding.toggleSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Log.d("TAGY", "Temp is now showing in C")
                dataViewModel.saveTempUnit(true)
            } else {
                Log.d("TAGY", "Temp is now showing in F") //Default value
                dataViewModel.saveTempUnit(false)
            }
            dataViewModel.getTempUnit()
        }

        // Get current weather data
        weatherViewModel.currentWeather.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let {
                when (it) {
                    is ApiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is ApiState.Success -> {
                        Log.d("TAGY", "Current Weather City Name: " + it.data!!.name)

                        binding.progressBar.visibility = View.GONE
                        binding.countryName.text = it.data.name
                        binding.weatherTV.text = it.data.weather[0].main
                        // Set the Temperature
                        var temp = String.format("%.0f", (it.data.main.temp - 273.15))
                        binding.tempTV.text = temp + " \u2103"
                        // Set the Icon
                        Glide.with(this@MainActivity).load(
                            IMAGE_URL + it.data.weather[0].icon + ".png"
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
        weatherViewModel.weatherForecast.observe(this, Observer {
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