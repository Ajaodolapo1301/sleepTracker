package com.example.android.trackmysleepquality.sleeptracker

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.R
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString

//import com.example.android.trackmysleepquality.sleeptracker.MyViewHolder.Companion
class SleepTrackerAdapter:RecyclerView.Adapter<MyViewHolder>(){


           var data = listOf<SleepNight>()
        set(value) {
            field = value
        notifyDataSetChanged()
        }

    override fun getItemCount() =data.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var item = data[position]
        holder.bind(item)
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }




}



class MyViewHolder   private constructor( ItemView: View) : RecyclerView.ViewHolder(ItemView){
    var sleep_length:TextView = itemView.findViewById(R.id.sleep_length)
    var sleepQuality:TextView = itemView.findViewById(R.id.quality_string)
    var qualityImage:ImageView = itemView.findViewById(R.id.quality_image)


    fun bind(item: SleepNight) {
        val res=   itemView.context.resources
        sleep_length.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
        sleepQuality.text =  convertNumericQualityToString(item.sleepQuality, res)



        qualityImage.setImageResource(when (item.sleepQuality) {
            0 -> R.drawable.ic_sleep_0
            1 -> R.drawable.ic_sleep_1
            2 -> R.drawable.ic_sleep_2
            3 -> R.drawable.ic_sleep_3
            4 -> R.drawable.ic_sleep_4
            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_launcher_sleep_tracker_background
        })
    }

    companion object {
        fun from(parent: ViewGroup): MyViewHolder {
            val layoutInflator = LayoutInflater.from(parent.context)
            val view = layoutInflator.inflate(R.layout.my_text_view, parent, false)
            return MyViewHolder(view)
        }
    }
}


class  SleepDiff: DiffUtil.ItemCallback<SleepNight>() {
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
      return  oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
 return  oldItem == newItem
    }

}


