package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.R
import androidx.recyclerview.widget.DiffUtil

 import    androidx.recyclerview.widget.ListAdapter
import com.example.android.trackmysleepquality.databinding.MyTextViewBinding


class SleepTrackerAdapter : ListAdapter<SleepNight, SleepTrackerAdapter.MyViewHolder>(SleepDiff()){
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var item = getItem(position)
        holder.bind(item)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }





    class MyViewHolder   private constructor(val binding: MyTextViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: SleepNight) {
            binding.sleep = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflator = LayoutInflater.from(parent.context)
              var  binding = MyTextViewBinding.inflate(layoutInflator, parent, false)
                return MyViewHolder(binding)
            }
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


