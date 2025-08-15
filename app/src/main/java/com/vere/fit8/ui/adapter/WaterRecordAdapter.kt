package com.vere.fit8.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vere.fit8.data.model.WaterRecord
import com.vere.fit8.databinding.ItemWaterRecordBinding
import java.time.format.DateTimeFormatter

class WaterRecordAdapter(
    private val onDeleteClick: (WaterRecord) -> Unit
) : ListAdapter<WaterRecord, WaterRecordAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWaterRecordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(
        private val binding: ItemWaterRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(record: WaterRecord) {
            binding.apply {
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                tvTime.text = record.recordTime.format(timeFormatter)
                tvAmount.text = "${record.amount}ml"
                
                btnDelete.setOnClickListener {
                    onDeleteClick(record)
                }
            }
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<WaterRecord>() {
        override fun areItemsTheSame(oldItem: WaterRecord, newItem: WaterRecord): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: WaterRecord, newItem: WaterRecord): Boolean {
            return oldItem == newItem
        }
    }
}
