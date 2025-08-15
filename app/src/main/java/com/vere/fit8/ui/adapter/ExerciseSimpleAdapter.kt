package com.vere.fit8.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vere.fit8.data.model.ExerciseTemplate
import com.vere.fit8.databinding.ItemExerciseSimpleBinding

class ExerciseSimpleAdapter : ListAdapter<ExerciseTemplate, ExerciseSimpleAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExerciseSimpleBinding.inflate(
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
        private val binding: ItemExerciseSimpleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(exercise: ExerciseTemplate) {
            binding.apply {
                tvExerciseName.text = exercise.name

                val setsText = when {
                    exercise.sets > 0 && exercise.reps > 0 -> "${exercise.sets}组 x ${exercise.reps}次"
                    exercise.durationSec > 0 -> "${exercise.sets}组 x ${exercise.durationSec}秒"
                    else -> "${exercise.sets}组"
                }
                tvExerciseSets.text = setsText
            }
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<ExerciseTemplate>() {
        override fun areItemsTheSame(oldItem: ExerciseTemplate, newItem: ExerciseTemplate): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExerciseTemplate, newItem: ExerciseTemplate): Boolean {
            return oldItem == newItem
        }
    }
}
