package com.vere.fit8.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vere.fit8.data.model.DietPlan
import com.vere.fit8.databinding.ItemDietPlanBinding

/**
 * 饮食计划适配器
 */
class DietPlanAdapter(
    private val onItemClick: (DietPlan) -> Unit,
    private val onCheckInClick: (DietPlan) -> Unit
) : ListAdapter<DietPlan, DietPlanAdapter.DietPlanViewHolder>(DietPlanDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietPlanViewHolder {
        val binding = ItemDietPlanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DietPlanViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: DietPlanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class DietPlanViewHolder(
        private val binding: ItemDietPlanBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(dietPlan: DietPlan) {
            binding.apply {
                tvMealType.text = when (dietPlan.mealType) {
                    "BREAKFAST" -> "早餐"
                    "LUNCH" -> "午餐"
                    "DINNER" -> "晚餐"
                    "SNACK" -> "加餐"
                    else -> dietPlan.mealType
                }
                
                tvFoodName.text = dietPlan.foodName
                tvAmount.text = dietPlan.amount
                tvCalories.text = "${dietPlan.calories}kcal"
                
                // 显示营养成分
                tvNutrition.text = "蛋白质${dietPlan.protein}g · 碳水${dietPlan.carbs}g · 脂肪${dietPlan.fat}g"
                
                if (dietPlan.description.isNotEmpty()) {
                    tvDescription.text = dietPlan.description
                    tvDescription.visibility = android.view.View.VISIBLE
                } else {
                    tvDescription.visibility = android.view.View.GONE
                }
                
                root.setOnClickListener {
                    onItemClick(dietPlan)
                }

                // 打卡按钮点击事件
                btnCheckIn.setOnClickListener {
                    onCheckInClick(dietPlan)
                }
            }
        }
    }
    
    private class DietPlanDiffCallback : DiffUtil.ItemCallback<DietPlan>() {
        override fun areItemsTheSame(oldItem: DietPlan, newItem: DietPlan): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: DietPlan, newItem: DietPlan): Boolean {
            return oldItem == newItem
        }
    }
}
