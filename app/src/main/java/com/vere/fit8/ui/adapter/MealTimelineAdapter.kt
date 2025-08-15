package com.vere.fit8.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vere.fit8.data.model.MealRecord
import com.vere.fit8.databinding.ItemMealTimelineBinding
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * 饮食记录时间线适配器
 * 按时间线显示用户的进食记录
 */
class MealTimelineAdapter(
    private val onItemClick: (MealRecord) -> Unit = {},
    private val onDeleteClick: (MealRecord) -> Unit = {}
) : ListAdapter<MealRecord, MealTimelineAdapter.TimelineViewHolder>(MealRecordDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val binding = ItemMealTimelineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TimelineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.bind(getItem(position), position == 0, position == itemCount - 1)
    }

    inner class TimelineViewHolder(
        private val binding: ItemMealTimelineBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(record: MealRecord, isFirst: Boolean, isLast: Boolean) {
            binding.apply {
                // 设置食物信息
                tvFoodName.text = record.foodName
                tvAmount.text = record.amount
                tvCalories.text = "${record.calories} kcal"
                
                // 设置时间
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                tvTime.text = record.createdAt.format(timeFormatter)
                
                // 设置营养信息
                val nutritionInfo = buildString {
                    if (record.protein > 0) append("蛋白质 ${record.protein}g  ")
                    if (record.carbs > 0) append("碳水 ${record.carbs}g  ")
                    if (record.fat > 0) append("脂肪 ${record.fat}g")
                }
                tvNutrition.text = nutritionInfo.ifEmpty { "营养信息未记录" }
                
                // 设置餐次标签
                tvMealType.text = when (record.mealType) {
                    "BREAKFAST" -> "早餐"
                    "LUNCH" -> "午餐"
                    "DINNER" -> "晚餐"
                    "SNACK" -> "加餐"
                    else -> record.mealType
                }
                
                // 设置备注（如果有）
                if (record.notes.isNotEmpty()) {
                    tvNotes.text = record.notes
                    tvNotes.visibility = android.view.View.VISIBLE
                } else {
                    tvNotes.visibility = android.view.View.GONE
                }
                
                // 设置记录类型图标
                when (record.recordType) {
                    "PHOTO" -> {
                        ivRecordType.setImageResource(com.vere.fit8.R.drawable.ic_camera)
                        ivRecordType.setColorFilter(ContextCompat.getColor(itemView.context, com.vere.fit8.R.color.info))
                    }
                    "SCAN" -> {
                        ivRecordType.setImageResource(com.vere.fit8.R.drawable.ic_qr_code)
                        ivRecordType.setColorFilter(ContextCompat.getColor(itemView.context, com.vere.fit8.R.color.warning))
                    }
                    "PLAN" -> {
                        ivRecordType.setImageResource(com.vere.fit8.R.drawable.ic_restaurant)
                        ivRecordType.setColorFilter(ContextCompat.getColor(itemView.context, com.vere.fit8.R.color.primary))
                    }
                    "CUSTOM" -> {
                        ivRecordType.setImageResource(com.vere.fit8.R.drawable.ic_edit)
                        ivRecordType.setColorFilter(ContextCompat.getColor(itemView.context, com.vere.fit8.R.color.secondary))
                    }
                    else -> {
                        ivRecordType.setImageResource(com.vere.fit8.R.drawable.ic_edit)
                        ivRecordType.setColorFilter(ContextCompat.getColor(itemView.context, com.vere.fit8.R.color.on_surface_variant))
                    }
                }
                
                // 设置时间线样式
                setupTimelineStyle(isFirst, isLast)
                
                // 设置点击事件
                root.setOnClickListener { onItemClick(record) }
                btnDelete.setOnClickListener { onDeleteClick(record) }
            }
        }
        
        private fun setupTimelineStyle(isFirst: Boolean, isLast: Boolean) {
            binding.apply {
                // 时间线连接线的显示逻辑
                when {
                    isFirst && isLast -> {
                        // 只有一条记录
                        viewTimelineTop.visibility = android.view.View.INVISIBLE
                        viewTimelineBottom.visibility = android.view.View.INVISIBLE
                    }
                    isFirst -> {
                        // 第一条记录
                        viewTimelineTop.visibility = android.view.View.INVISIBLE
                        viewTimelineBottom.visibility = android.view.View.VISIBLE
                    }
                    isLast -> {
                        // 最后一条记录
                        viewTimelineTop.visibility = android.view.View.VISIBLE
                        viewTimelineBottom.visibility = android.view.View.INVISIBLE
                    }
                    else -> {
                        // 中间的记录
                        viewTimelineTop.visibility = android.view.View.VISIBLE
                        viewTimelineBottom.visibility = android.view.View.VISIBLE
                    }
                }
            }
        }
    }

    class MealRecordDiffCallback : DiffUtil.ItemCallback<MealRecord>() {
        override fun areItemsTheSame(oldItem: MealRecord, newItem: MealRecord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MealRecord, newItem: MealRecord): Boolean {
            return oldItem == newItem
        }
    }
}
