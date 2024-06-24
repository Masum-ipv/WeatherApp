package com.example.weaterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weaterapp.R
import com.example.weaterapp.databinding.WeatherItemBinding
import com.example.weaterapp.util.Constants

class ForecastAdapter(
    private val context: Context,
    private val weatherForecastList: List<com.example.weaterapp.model.List>
) :
    RecyclerView.Adapter<ForecastAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: WeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, weatherForecast: com.example.weaterapp.model.List) {
            binding.timeTV.text = weatherForecast.dtTxt

            val temp = String.format("%.0f", (weatherForecast.main.temp - 273.15))
            binding.tempTV.text = temp + " \u2103"

            binding.windTV.text = weatherForecast.wind.speed.toString() + " Km/h"

            Glide.with(context).load(
                Constants.IMAGE_URL + weatherForecast.weather.get(0).icon + ".png"
            ).into(binding.weatherIcon)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: WeatherItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.weather_item,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weatherForecastList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(context, weatherForecastList[position])
    }
}