package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import androidx.recyclerview.widget.DiffUtil

 import    androidx.recyclerview.widget.ListAdapter
import com.example.android.trackmysleepquality.databinding.MyTextViewBinding


class SleepTrackerAdapter(val clickListener: SleepNightListener) : ListAdapter<SleepNight, SleepTrackerAdapter.MyViewHolder>(SleepDiff()){



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

//        var item = getItem(position)
        holder.bind(getItem(position)!!, clickListener)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }





    class MyViewHolder   private constructor(val binding: MyTextViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = item
            binding.executePendingBindings()
            binding.sleepListener = clickListener
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

class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}


