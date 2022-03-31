package com.example.climapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.climapp.R
import com.example.climapp.ui.model.Current
import java.text.SimpleDateFormat
import java.util.*

class ByHoursAdapter(private val context: Context, private val listHours: List<Current>):
    RecyclerView.Adapter<ByHoursAdapter.HoursHolder>()  {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ByHoursAdapter.HoursHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview_hours,parent,false)
        return ByHoursAdapter.HoursHolder(view)
    }

    override fun onBindViewHolder(holder: ByHoursAdapter.HoursHolder, position: Int) {
        val hours = listHours.get(position)
        with(holder){

            val icon = hours.weather.first().icon.replace('n','d')
            val iconUrl = context.resources.getIdentifier("ic_weather_$icon","drawable", context.packageName)
            val dateFormatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
            val hour = dateFormatter.format(Date(hours.dt*1000))

            tv_temp_hours.text = hours.temp.toInt().toString() + "°"
            iv_icon_hours.load(iconUrl)
            tv_time_hours.text = hour

        }
    }

    override fun getItemCount(): Int = listHours.size

    class HoursHolder(view: View): RecyclerView.ViewHolder(view){
        val iv_icon_hours: ImageView = view.findViewById(R.id.iv_status_by_hour)
        val tv_time_hours: TextView = view.findViewById(R.id.tv_hour_by_hour)
        val tv_temp_hours: TextView = view.findViewById(R.id.tv_temp_by_hour)
    }

}