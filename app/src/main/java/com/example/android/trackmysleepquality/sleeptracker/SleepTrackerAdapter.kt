package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import androidx.recyclerview.widget.DiffUtil

 import    androidx.recyclerview.widget.ListAdapter
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.databinding.MyTextViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class SleepTrackerAdapter(val clickListener: SleepNightListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepDiff()){
//coroutineScope
    private val adapterScope = CoroutineScope(Dispatchers.Default)



    // getting the index
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

//        var item = getItem(position)
//        holder.bind(getItem(position)!!, clickListener)
        when (holder) {
            is MyViewHolder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(nightItem.sleepNight, clickListener)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> MyViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }


    }


//    for submiting the header nd body

    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map {     DataItem.SleepNightItem(it)
            }
        }
        withContext(Dispatchers.Main) {
            submitList(items)
        }
    }
}







}




//Text for header
    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }



// view class fro the display
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






//
//differenciation
class  SleepDiff: DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
      return  oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
 return  oldItem.id == newItem.id
    }

}

class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}



// Seal class for picking either header or body
 sealed class DataItem{
    data class SleepNightItem(val sleepNight: SleepNight): DataItem(){
        override val id = sleepNight.nightId
    }


        object Header: DataItem(){
            override val id = Long.MIN_VALUE
        }
        abstract val id:Long

}


