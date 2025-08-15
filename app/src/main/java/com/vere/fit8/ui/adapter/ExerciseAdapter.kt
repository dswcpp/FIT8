package com.vere.fit8.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vere.fit8.R
import com.vere.fit8.data.model.ExerciseTemplate
import com.vere.fit8.databinding.ItemExerciseBinding

/**
 * 训练动作适配器
 */
class ExerciseAdapter(
    private val onExerciseClick: (ExerciseTemplate) -> Unit,
    private val onStartExerciseClick: (ExerciseTemplate) -> Unit,
    private val onCompleteClick: (ExerciseTemplate) -> Unit,
    private val getExerciseProgress: (String) -> com.vere.fit8.data.model.TrainingExercise? = { null }
) : ListAdapter<ExerciseTemplate, ExerciseAdapter.ExerciseViewHolder>(ExerciseDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExerciseViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ExerciseViewHolder(
        private val binding: ItemExerciseBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(exercise: ExerciseTemplate) {
            binding.apply {
                // 动作名称
                tvExerciseName.text = exercise.name
                
                // 组数
                tvSets.text = "${exercise.sets}组"
                
                // 次数或时长 - 根据开发手册区分个数、次数、时间
                if (exercise.reps > 0) {
                    tvReps.visibility = View.VISIBLE
                    tvDuration.visibility = View.GONE

                    // 根据动作名称判断是"个数"还是"次数"
                    val unit = when {
                        exercise.name.contains("卷腹") || exercise.name.contains("转体") -> "次"
                        exercise.name.contains("/边") || exercise.name.contains("/腿") -> "次"
                        else -> "个" // 俯卧撑、深蹲等默认为"个"
                    }
                    tvReps.text = "${exercise.reps}${unit}"
                } else if (exercise.durationSec > 0) {
                    tvReps.visibility = View.GONE
                    tvDuration.visibility = View.VISIBLE
                    tvDuration.text = "${exercise.durationSec}秒"
                } else {
                    tvReps.visibility = View.VISIBLE
                    tvDuration.visibility = View.GONE
                    tvReps.text = "自由"
                }
                
                // 休息时间
                if (exercise.restSec > 0) {
                    tvRestTime.text = "组间休息 ${exercise.restSec}秒"
                    tvRestTime.visibility = View.VISIBLE
                } else {
                    tvRestTime.visibility = View.GONE
                }
                
                // 目标肌群
                if (exercise.targetMuscles.isNotEmpty()) {
                    tvTargetMuscles.text = "目标：${exercise.targetMuscles.joinToString("、")}"
                    tvTargetMuscles.visibility = View.VISIBLE
                } else {
                    tvTargetMuscles.visibility = View.GONE
                }
                
                // 动作要点
                if (exercise.tips.isNotEmpty()) {
                    tvTips.text = exercise.tips
                    tvTips.visibility = View.VISIBLE
                } else {
                    tvTips.visibility = View.GONE
                }
                
                // 难度等级
                updateDifficultyStars(exercise.difficulty)
                
                // 动作图标
                updateExerciseIcon(exercise.name)
                
                // 点击事件
                root.setOnClickListener {
                    onStartExerciseClick(exercise)
                }

                btnExerciseDetail.setOnClickListener {
                    onExerciseClick(exercise)
                }
                
                // 完成进度显示（模拟数据，实际应该从数据库获取）
                updateExerciseProgress(exercise)
            }
        }

        private fun updateExerciseProgress(exercise: ExerciseTemplate) {
            // 从数据库获取真实的训练进度
            val trainingRecord = getExerciseProgress(exercise.name)

            if (trainingRecord != null && trainingRecord.completedSets > 0) {
                binding.layoutProgress.visibility = View.VISIBLE
                val completedSets = trainingRecord.completedSets
                val totalSets = exercise.sets
                val progress = (completedSets * 100) / totalSets

                binding.tvProgressText.text = "进度: ${completedSets}/${totalSets}组完成"
                binding.progressExercise.progress = progress

                // 如果全部完成，显示完成状态
                if (trainingRecord.completed) {
                    binding.ivCompletionStatus.visibility = View.VISIBLE
                    binding.tvProgressText.text = "✅ 已完成"
                    binding.progressExercise.progress = 100
                } else {
                    binding.ivCompletionStatus.visibility = View.GONE
                }
            } else {
                // 没有训练记录，显示未开始状态
                binding.layoutProgress.visibility = View.GONE
                binding.ivCompletionStatus.visibility = View.GONE
            }
        }

        private fun updateDifficultyStars(difficulty: Int) {
            val stars = listOf(
                binding.ivDifficulty1,
                binding.ivDifficulty2,
                binding.ivDifficulty3,
                binding.ivDifficulty4,
                binding.ivDifficulty5
            )
            
            stars.forEachIndexed { index, star ->
                star.setColorFilter(
                    if (index < difficulty) {
                        itemView.context.getColor(R.color.warning)
                    } else {
                        itemView.context.getColor(R.color.gray_300)
                    }
                )
            }
        }
        
        private fun updateExerciseIcon(exerciseName: String) {
            // 根据动作名称设置对应图标
            val iconRes = when {
                exerciseName.contains("俯卧撑") -> R.drawable.ic_pushup
                exerciseName.contains("深蹲") -> R.drawable.ic_squat
                exerciseName.contains("平板支撑") -> R.drawable.ic_plank
                exerciseName.contains("跳跃") -> R.drawable.ic_jump
                exerciseName.contains("卷腹") -> R.drawable.ic_crunch
                exerciseName.contains("波比") -> R.drawable.ic_burpee
                exerciseName.contains("开合跳") -> R.drawable.ic_jumping_jack
                exerciseName.contains("登山") -> R.drawable.ic_mountain_climber
                else -> R.drawable.ic_training
            }
            
            binding.ivExerciseIcon.setImageResource(iconRes)
        }
    }
    
    private class ExerciseDiffCallback : DiffUtil.ItemCallback<ExerciseTemplate>() {
        override fun areItemsTheSame(oldItem: ExerciseTemplate, newItem: ExerciseTemplate): Boolean {
            return oldItem.name == newItem.name
        }
        
        override fun areContentsTheSame(oldItem: ExerciseTemplate, newItem: ExerciseTemplate): Boolean {
            return oldItem == newItem
        }
    }
}
