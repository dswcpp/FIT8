package com.vere.fit8.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vere.fit8.R
import com.vere.fit8.data.model.ExerciseTemplate
import com.vere.fit8.databinding.ActivityExerciseExecutionBinding
import com.vere.fit8.ui.viewmodel.ExerciseExecutionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 训练动作执行Activity
 * 专门用于单个动作的计数或计时执行
 */
@AndroidEntryPoint
class ExerciseExecutionActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityExerciseExecutionBinding
    private val viewModel: ExerciseExecutionViewModel by viewModels()
    
    companion object {
        private const val EXTRA_EXERCISE = "extra_exercise"
        private const val EXTRA_CURRENT_SET = "extra_current_set"
        
        fun createIntent(context: Context, exercise: ExerciseTemplate, currentSet: Int = 1): Intent {
            return Intent(context, ExerciseExecutionActivity::class.java).apply {
                putExtra(EXTRA_EXERCISE, exercise)
                putExtra(EXTRA_CURRENT_SET, currentSet)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseExecutionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupUI()
        observeViewModel()
        
        // 获取传入的动作信息
        val exercise = intent.getSerializableExtra(EXTRA_EXERCISE) as? ExerciseTemplate
        val currentSet = intent.getIntExtra(EXTRA_CURRENT_SET, 1)
        
        if (exercise != null) {
            viewModel.initializeExercise(exercise, currentSet)
        } else {
            finish()
        }
    }
    
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            showExitConfirmDialog()
        }
    }
    
    private fun setupUI() {
        // 计数按钮
        binding.btnPlus.setOnClickListener {
            viewModel.incrementCount()
        }
        
        binding.btnMinus.setOnClickListener {
            viewModel.decrementCount()
        }
        
        // 控制按钮
        binding.btnSkipSet.setOnClickListener {
            viewModel.skipCurrentSet()
        }
        
        binding.btnCompleteSet.setOnClickListener {
            viewModel.completeCurrentSet()
        }
    }
    
    private fun observeViewModel() {
        // 观察动作信息
        lifecycleScope.launch {
            viewModel.exercise.collect { exercise ->
                updateExerciseInfo(exercise)
            }
        }
        
        // 观察计数状态
        lifecycleScope.launch {
            viewModel.currentCount.collect { count ->
                binding.tvCurrentCount.text = count.toString()
            }
        }
        
        lifecycleScope.launch {
            viewModel.targetCount.collect { target ->
                binding.tvTargetCount.text = target.toString()
            }
        }
        
        // 观察计时状态
        lifecycleScope.launch {
            viewModel.currentTime.collect { seconds ->
                val minutes = seconds / 60
                val secs = seconds % 60
                binding.tvTimerDisplay.text = String.format("%02d:%02d", minutes, secs)
            }
        }
        
        lifecycleScope.launch {
            viewModel.timerProgress.collect { progress ->
                binding.progressTimer.progress = progress
            }
        }
        
        // 观察执行模式
        lifecycleScope.launch {
            viewModel.isCountingMode.collect { isCounting ->
                binding.layoutCounting.visibility = if (isCounting) View.VISIBLE else View.GONE
                binding.layoutTiming.visibility = if (!isCounting) View.VISIBLE else View.GONE
            }
        }
        
        // 观察组完成状态
        lifecycleScope.launch {
            viewModel.isSetCompleted.collect { completed ->
                if (completed) {
                    showSetCompletedDialog()
                }
            }
        }
    }
    
    private fun updateExerciseInfo(exercise: ExerciseTemplate?) {
        exercise?.let {
            binding.tvExerciseName.text = it.name

            val currentSet = viewModel.currentSet.value
            val info = if (it.reps > 0) {
                // 根据动作名称判断是"个数"还是"次数"
                val unit = when {
                    it.name.contains("卷腹") || it.name.contains("转体") -> "次"
                    it.name.contains("/边") || it.name.contains("/腿") -> "次"
                    else -> "个" // 俯卧撑、深蹲等默认为"个"
                }

                // 更新计数界面的标题和单位
                val countingTitle = when (unit) {
                    "次" -> "动作计次"
                    "个" -> "动作计个"
                    else -> "动作计数"
                }
                binding.tvCountingTitle.text = countingTitle
                binding.tvCountUnit.text = unit

                "第${currentSet}/${it.sets}组 · ${it.reps}${unit}"
            } else {
                "第${currentSet}/${it.sets}组 · ${it.durationSec}秒"
            }
            binding.tvExerciseInfo.text = info

            if (it.durationSec > 0) {
                binding.tvTargetTime.text = "目标: ${it.durationSec}秒"
            }
        }
    }
    
    private fun showSetCompletedDialog() {
        val exercise = viewModel.exercise.value ?: return
        val currentSet = viewModel.currentSet.value
        
        if (currentSet >= exercise.sets) {
            // 所有组都完成了
            MaterialAlertDialogBuilder(this)
                .setTitle("动作完成！")
                .setMessage("🎉 恭喜完成 ${exercise.name}！\n\n所有${exercise.sets}组都已完成。")
                .setPositiveButton("返回") { _, _ ->
                    setResult(RESULT_OK)
                    finish()
                }
                .setCancelable(false)
                .show()
        } else {
            // 还有组数未完成，显示休息对话框
            showRestDialog()
        }
    }
    
    private fun showRestDialog() {
        val exercise = viewModel.exercise.value ?: return
        val restTime = exercise.restSec
        
        MaterialAlertDialogBuilder(this)
            .setTitle("组间休息")
            .setMessage("💪 第${viewModel.currentSet.value}组完成！\n\n休息${restTime}秒后继续下一组。")
            .setPositiveButton("开始休息") { _, _ ->
                viewModel.startRest(restTime)
            }
            .setNegativeButton("直接继续") { _, _ ->
                viewModel.startNextSet()
            }
            .show()
    }
    
    private fun showExitConfirmDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("确认退出")
            .setMessage("确定要退出当前训练吗？进度将不会保存。")
            .setPositiveButton("退出") { _, _ ->
                finish()
            }
            .setNegativeButton("继续训练", null)
            .show()
    }
    
    override fun onBackPressed() {
        showExitConfirmDialog()
    }
}
