package com.vere.fit8.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vere.fit8.MainActivity
import com.vere.fit8.databinding.ActivityTodayTaskBinding
import com.vere.fit8.ui.adapter.ExerciseSimpleAdapter
import com.vere.fit8.ui.viewmodel.TrainingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodayTaskActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityTodayTaskBinding
    private val viewModel: TrainingViewModel by viewModels()
    private lateinit var exerciseAdapter: ExerciseSimpleAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodayTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        setupRecyclerView()
        observeViewModel()
        loadTodayTask()
    }
    
    private fun setupUI() {
        // 设置工具栏
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // 设置按钮点击事件
        binding.btnStartTraining.setOnClickListener {
            try {
                startTraining()
            } catch (e: Exception) {
                e.printStackTrace()
                // 如果startTraining失败，直接跳转
                try {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("switch_to_tab", 1)
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    startActivity(intent)
                    finish()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
        
        binding.btnViewDetails.setOnClickListener {
            viewTrainingDetails()
        }
    }
    
    private fun setupRecyclerView() {
        exerciseAdapter = ExerciseSimpleAdapter()
        
        binding.rvExercises.apply {
            layoutManager = LinearLayoutManager(this@TodayTaskActivity)
            adapter = exerciseAdapter
        }
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.todayPlan.collect { plan ->
                if (plan != null) {
                    showTrainingPlan(plan)
                } else {
                    showRestDay()
                }
            }
        }
        
        lifecycleScope.launch {
            viewModel.exercises.collect { exercises ->
                exerciseAdapter.submitList(exercises)
            }
        }
    }
    
    private fun loadTodayTask() {
        viewModel.loadTodayPlan()
    }
    
    private fun showTrainingPlan(plan: com.vere.fit8.data.model.WeeklyPlan) {
        binding.layoutTrainingPlan.visibility = View.VISIBLE
        binding.layoutRestDay.visibility = View.GONE
        
        binding.tvTaskStatus.text = "今日训练计划"
        binding.tvTaskSubtitle.text = "坚持就是胜利"
        
        binding.tvPlanTitle.text = plan.trainingType
        binding.tvPlanDuration.text = "预计时长: ${plan.estimatedDurationMin}分钟"
        binding.tvPlanDifficulty.text = "预计消耗: ${plan.estimatedCalories}kcal"
        binding.tvPlanDescription.text = plan.description
        
        if (plan.tips.isNotEmpty()) {
            binding.tvTips.text = plan.tips
        } else {
            binding.tvTips.text = "训练前请充分热身，训练后记得拉伸放松。"
        }
        
        binding.btnStartTraining.text = "开始训练"
        binding.btnStartTraining.visibility = View.VISIBLE
    }
    
    private fun showRestDay() {
        binding.layoutTrainingPlan.visibility = View.GONE
        binding.layoutRestDay.visibility = View.VISIBLE
        
        binding.tvTaskStatus.text = "今日休息"
        binding.tvTaskSubtitle.text = "休息也是训练的一部分"
        
        val restTips = listOf(
            "🧘‍♀️ 今天可以做一些轻松的拉伸运动",
            "🚶‍♂️ 建议进行30分钟的散步或慢走",
            "🛁 泡个热水澡，放松肌肉",
            "📚 学习一些健身知识，为明天的训练做准备",
            "💤 保证充足的睡眠，让身体充分恢复"
        )
        
        val randomTip = restTips.random()
        binding.tvRestTip.text = "休息也是训练的一部分！适当的休息有助于肌肉恢复和生长。"
        binding.tvRestSuggestion.text = randomTip
        
        binding.tvTips.text = "明天继续加油！保持良好的作息习惯。"
        
        binding.btnStartTraining.text = "查看明日计划"
        binding.btnStartTraining.visibility = View.VISIBLE
    }
    
    private fun getDifficultyText(difficulty: String): String {
        return when (difficulty.lowercase()) {
            "easy" -> "简单"
            "medium" -> "中等"
            "hard" -> "困难"
            else -> "中等"
        }
    }
    
    private fun startTraining() {
        try {
            val plan = viewModel.todayPlan.value
            if (plan != null) {
                // 跳转到训练页面
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("switch_to_tab", 1) // 训练页面
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(intent)
                finish()
            } else {
                // 休息日，查看明日计划
                viewTrainingDetails()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // 如果出现异常，直接跳转到训练页面
            try {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("switch_to_tab", 1)
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(intent)
                finish()
            } catch (ex: Exception) {
                ex.printStackTrace()
                // 最后的备选方案，直接finish
                finish()
            }
        }
    }
    
    private fun viewTrainingDetails() {
        // 跳转到训练页面查看详情
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("switch_to_tab", 1) // 训练页面
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
        finish()
    }
}
