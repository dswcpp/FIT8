package com.vere.fit8.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vere.fit8.R
import com.vere.fit8.data.model.MealRecord
import com.vere.fit8.databinding.ItemMealRecordBinding
import java.time.format.DateTimeFormatter

/**
 * 饮食记录适配器
 */
class MealRecordAdapter(
    private val onEditClick: (MealRecord) -> Unit,
    private val onDeleteClick: (MealRecord) -> Unit
) : ListAdapter<MealRecord, MealRecordAdapter.MealRecordViewHolder>(MealRecordDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealRecordViewHolder {
        val binding = ItemMealRecordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MealRecordViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: MealRecordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class MealRecordViewHolder(
        private val binding: ItemMealRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(record: MealRecord) {
            binding.apply {
                tvMealType.text = when (record.mealType) {
                    "BREAKFAST" -> "早餐"
                    "LUNCH" -> "午餐"
                    "DINNER" -> "晚餐"
                    "SNACK" -> "加餐"
                    else -> record.mealType
                }
                
                tvFoodName.text = record.foodName
                tvAmount.text = record.amount
                tvCalories.text = "${record.calories}kcal"
                
                // 显示营养成分
                tvNutrition.text = "蛋白质${record.protein}g · 碳水${record.carbs}g · 脂肪${record.fat}g"
                
                // 显示记录时间
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                tvTime.text = record.createdAt.format(timeFormatter)
                
                // 显示记录类型图标
                when (record.recordType) {
                    "PHOTO" -> ivRecordType.setImageResource(R.drawable.ic_camera)
                    "SCAN" -> ivRecordType.setImageResource(R.drawable.ic_scan)
                    else -> ivRecordType.setImageResource(R.drawable.ic_edit)
                }
                
                // 显示照片（如果有）
                if (record.photoPath.isNotEmpty()) {
                    ivPhoto.visibility = android.view.View.VISIBLE
                    Glide.with(itemView.context)
                        .load(record.photoPath)
                        .centerCrop()
                        .into(ivPhoto)
                } else {
                    ivPhoto.visibility = android.view.View.GONE
                }
                
                // 显示备注（如果有）
                if (record.notes.isNotEmpty()) {
                    tvNotes.text = record.notes
                    tvNotes.visibility = android.view.View.VISIBLE
                } else {
                    tvNotes.visibility = android.view.View.GONE
                }
                
                // 设置点击事件
                btnEdit.setOnClickListener {
                    onEditClick(record)
                }
                
                btnDelete.setOnClickListener {
                    onDeleteClick(record)
                }
                
                root.setOnClickListener {
                    // 可以展开显示更多详情
                }
            }
        }
    }
    
    private class MealRecordDiffCallback : DiffUtil.ItemCallback<MealRecord>() {
        override fun areItemsTheSame(oldItem: MealRecord, newItem: MealRecord): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: MealRecord, newItem: MealRecord): Boolean {
            return oldItem == newItem
        }
    }
}
