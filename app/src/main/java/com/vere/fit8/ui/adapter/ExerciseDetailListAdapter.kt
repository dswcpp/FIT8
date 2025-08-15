package com.vere.fit8.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vere.fit8.data.model.ExerciseDetail
import com.vere.fit8.databinding.ItemExerciseDetailListBinding

/**
 * 动作详情列表适配器
 */
class ExerciseDetailListAdapter(
    private val onItemClick: (ExerciseDetail) -> Unit
) : ListAdapter<ExerciseDetail, ExerciseDetailListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExerciseDetailListBinding.inflate(
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
        private val binding: ItemExerciseDetailListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(exercise: ExerciseDetail) {
            binding.apply {
                // 设置动作名称
                tvExerciseName.text = exercise.name

                // 设置分类
                tvCategory.text = exercise.category

                // 设置难度
                tvDifficulty.text = exercise.difficulty

                // 设置描述
                tvDescription.text = exercise.description
            }
        }


    }

    class DiffCallback : DiffUtil.ItemCallback<ExerciseDetail>() {
        override fun areItemsTheSame(oldItem: ExerciseDetail, newItem: ExerciseDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExerciseDetail, newItem: ExerciseDetail): Boolean {
            return oldItem == newItem
        }
    }
}
