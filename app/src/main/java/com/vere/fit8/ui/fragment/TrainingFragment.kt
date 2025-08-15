package com.vere.fit8.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vere.fit8.R
import com.vere.fit8.databinding.FragmentTrainingBinding
import com.vere.fit8.ui.activity.ExerciseExecutionActivity
import com.vere.fit8.ui.adapter.ExerciseAdapter
import com.vere.fit8.ui.viewmodel.TrainingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * 训练Fragment
 * 显示8周训练计划和训练执行功能
 */
@AndroidEntryPoint
class TrainingFragment : Fragment() {
    
    private var _binding: FragmentTrainingBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: TrainingViewModel by viewModels()
    
    private lateinit var exerciseAdapter: ExerciseAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupRecyclerView()
        observeViewModel()

        // 确保数据初始化完成后再加载训练计划
        viewLifecycleOwner.lifecycleScope.launch {
            // 等待一小段时间确保Application中的初始化完成
            kotlinx.coroutines.delay(500)
            loadTodayTraining()
        }
    }
    
    private fun setupUI() {
        // 设置今日日期
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MM月dd日 EEEE", Locale.CHINESE)
        binding.tvDate.text = today.format(formatter)
        
        // 设置点击事件
        binding.btnPreviousDay.setOnClickListener {
            viewModel.previousDay()
        }
        
        binding.btnNextDay.setOnClickListener {
            viewModel.nextDay()
        }
        
        binding.btnStartTraining.setOnClickListener {
            startTraining()
        }
        
        binding.btnPauseTraining.setOnClickListener {
            viewModel.pauseTraining()
        }
        
        binding.btnResumeTraining.setOnClickListener {
            viewModel.resumeTraining()
        }
        
        binding.btnCompleteTraining.setOnClickListener {
            viewModel.completeTraining()
        }
        
        binding.btnSkipExercise.setOnClickListener {
            viewModel.skipCurrentExercise()
        }
        
        binding.btnNextExercise.setOnClickListener {
            viewModel.nextExercise()
        }

        // 计数按钮
        binding.btnIncreaseRep.setOnClickListener {
            viewModel.incrementReps()
        }

        binding.btnDecreaseRep.setOnClickListener {
            viewModel.decrementReps()
        }

        // 查看动作详情按钮
        binding.btnViewExerciseDetails.setOnClickListener {
            try {
                val intent = com.vere.fit8.ui.activity.ExerciseDetailListActivity.createIntent(requireContext())
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                // 备选方案：直接创建Intent
                try {
                    val intent = android.content.Intent(requireContext(), com.vere.fit8.ui.activity.ExerciseDetailListActivity::class.java)
                    startActivity(intent)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    android.widget.Toast.makeText(requireContext(), "无法打开动作详情页面", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun setupRecyclerView() {
        exerciseAdapter = ExerciseAdapter(
            onExerciseClick = { exercise ->
                showExerciseDetail(exercise)
            },
            onStartExerciseClick = { exercise ->
                startExerciseExecution(exercise)
            },
            onCompleteClick = { exercise ->
                viewModel.completeExercise(exercise)
            },
            getExerciseProgress = { exerciseName ->
                viewModel.getExerciseProgress(exerciseName)
            }
        )
        
        binding.rvExercises.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = exerciseAdapter
        }
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentDate.collect { date ->
                updateDateDisplay(date)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todayPlan.collect { plan ->
                updateTrainingPlan(plan)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exercises.collect { exercises ->
                if (exercises.isEmpty()) {
                    // 如果没有数据，显示默认的训练计划
                    showDefaultTrainingPlan()
                } else {
                    exerciseAdapter.submitList(exercises)
                    hideEmptyState()
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.trainingState.collect { state ->
                updateTrainingState(state)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentExercise.collect { exercise ->
                updateCurrentExercise(exercise)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.timer.collect { time ->
                updateTimer(time)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.restTimer.collect { time ->
                updateRestTimer(time)
            }
        }

        // 观察动作计数状态
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentReps.collect { reps ->
                binding.tvCurrentRepCount.text = reps.toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.targetReps.collect { reps ->
                binding.tvTargetRepCount.text = reps.toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isCountingReps.collect { isCounting ->
                binding.layoutRepCounter.visibility = if (isCounting) View.VISIBLE else View.GONE
            }
        }

        // 观察动作计时状态
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exerciseTimer.collect { seconds ->
                val minutes = seconds / 60
                val secs = seconds % 60
                binding.tvExerciseTimer.text = String.format("%02d:%02d", minutes, secs)

                // 更新进度条
                val exercise = viewModel.currentExercise.value
                if (exercise != null && exercise.durationSec > 0) {
                    val progress = (seconds * 100) / exercise.durationSec
                    binding.progressExerciseTimer.progress = progress
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isTimingExercise.collect { isTiming ->
                binding.layoutExerciseTimer.visibility = if (isTiming) View.VISIBLE else View.GONE
            }
        }

        // 观察当前组数
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentSetNumber.collect { setNumber ->
                val exercise = viewModel.currentExercise.value
                if (exercise != null) {
                    binding.tvCurrentSets.text = "第${setNumber}/${exercise.sets}组"
                }
            }
        }
    }
    
    private fun loadTodayTraining() {
        viewModel.loadTodayTraining()
    }
    
    private fun updateDateDisplay(date: LocalDate) {
        val formatter = DateTimeFormatter.ofPattern("MM月dd日 EEEE", Locale.CHINESE)
        binding.tvDate.text = date.format(formatter)
        
        binding.tvWeekInfo.text = "第${viewModel.getCurrentWeek()}周 · 第${date.dayOfWeek.value}天"
    }
    
    private fun updateTrainingPlan(plan: com.vere.fit8.data.model.WeeklyPlan?) {
        plan?.let {
            binding.tvTrainingType.text = it.trainingType
            binding.tvTrainingDescription.text = it.description
            binding.tvEstimatedDuration.text = "${it.estimatedDurationMin}分钟"
            binding.tvEstimatedCalories.text = "${it.estimatedCalories}kcal"
            binding.tvTrainingTips.text = it.tips
        }
    }
    
    private fun updateTrainingState(state: com.vere.fit8.ui.viewmodel.TrainingState) {
        when (state) {
            is com.vere.fit8.ui.viewmodel.TrainingState.NotStarted -> {
                binding.btnStartTraining.visibility = View.VISIBLE
                binding.layoutTrainingControls.visibility = View.GONE
                binding.layoutTimer.visibility = View.GONE
            }
            is com.vere.fit8.ui.viewmodel.TrainingState.InProgress -> {
                binding.btnStartTraining.visibility = View.GONE
                binding.layoutTrainingControls.visibility = View.VISIBLE
                binding.layoutTimer.visibility = View.VISIBLE
                binding.btnPauseTraining.visibility = View.VISIBLE
                binding.btnResumeTraining.visibility = View.GONE
            }
            is com.vere.fit8.ui.viewmodel.TrainingState.Paused -> {
                binding.btnPauseTraining.visibility = View.GONE
                binding.btnResumeTraining.visibility = View.VISIBLE
            }
            is com.vere.fit8.ui.viewmodel.TrainingState.Resting -> {
                binding.layoutRestTimer.visibility = View.VISIBLE
                binding.tvRestMessage.text = "休息时间"
            }
            is com.vere.fit8.ui.viewmodel.TrainingState.Completed -> {
                binding.layoutTrainingControls.visibility = View.GONE
                binding.layoutTimer.visibility = View.GONE
                binding.layoutRestTimer.visibility = View.GONE
                showTrainingCompleteDialog(state.totalDuration, state.totalCalories)
            }
        }
    }
    
    private fun updateCurrentExercise(exercise: com.vere.fit8.data.model.ExerciseTemplate?) {
        exercise?.let {
            binding.tvCurrentExercise.text = it.name
            binding.tvCurrentSets.text = "第${viewModel.getCurrentSet()}/${it.sets}组"
            binding.tvCurrentReps.text = if (it.reps > 0) "${it.reps}次" else "${it.durationSec}秒"
        }
    }
    
    private fun updateTimer(time: String) {
        binding.tvTimer.text = time
    }
    
    private fun updateRestTimer(time: String) {
        binding.tvRestTimer.text = time
    }
    
    private fun startTraining() {
        viewModel.startTraining()
    }
    
    private fun showExerciseDetail(exercise: com.vere.fit8.data.model.ExerciseTemplate) {
        val message = buildString {
            append("动作：${exercise.name}\n")
            append("英文名：${exercise.nameEn}\n")
            if (exercise.reps > 0) {
                append("次数：${exercise.reps}次\n")
            }
            if (exercise.durationSec > 0) {
                append("时长：${exercise.durationSec}秒\n")
            }
            append("组数：${exercise.sets}组\n")
            if (exercise.restSec > 0) {
                append("组间休息：${exercise.restSec}秒\n")
            }
            append("难度：${exercise.difficulty}/5\n")
            append("器械：${exercise.equipment}\n")
            if (exercise.targetMuscles.isNotEmpty()) {
                append("目标肌群：${exercise.targetMuscles.joinToString(", ")}\n")
            }
            if (exercise.description.isNotEmpty()) {
                append("\n动作要领：\n${exercise.description}")
            }
            if (exercise.tips.isNotEmpty()) {
                append("\n小贴士：\n${exercise.tips}")
            }
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("动作详情")
            .setMessage(message)
            .setPositiveButton("知道了") { _, _ ->
                Toast.makeText(requireContext(), "开始${exercise.name}训练", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("关闭", null)
            .show()
    }
    
    private fun showTrainingCompleteDialog(duration: Int, calories: Int) {
        val message = buildString {
            append("🎉 恭喜完成今日训练！\n\n")
            append("⏱️ 训练时长：${duration}分钟\n")
            append("🔥 消耗卡路里：${calories}kcal\n")
            append("💪 坚持就是胜利！")
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("训练完成")
            .setMessage(message)
            .setPositiveButton("太棒了！") { _, _ ->
                Toast.makeText(requireContext(), "训练数据已保存", Toast.LENGTH_SHORT).show()
            }
            .setCancelable(false)
            .show()
    }
    
    private fun startExerciseExecution(exercise: com.vere.fit8.data.model.ExerciseTemplate) {
        val intent = ExerciseExecutionActivity.createIntent(requireContext(), exercise)
        startActivity(intent)
    }

    private fun showDefaultTrainingPlan() {
        // 创建默认的训练计划显示
        val defaultExercises = listOf(
            com.vere.fit8.data.model.ExerciseTemplate(
                name = "俯卧撑",
                nameEn = "Push-ups",
                sets = 3,
                reps = 10,
                durationSec = 0,
                restSec = 60,
                description = "标准俯卧撑动作，锻炼胸部和手臂力量",
                tips = "保持身体挺直，控制动作节奏",
                difficulty = 2,
                targetMuscles = listOf("胸部", "手臂"),
                equipment = "无器械"
            ),
            com.vere.fit8.data.model.ExerciseTemplate(
                name = "深蹲",
                nameEn = "Squats",
                sets = 3,
                reps = 15,
                durationSec = 0,
                restSec = 60,
                description = "标准深蹲动作，锻炼腿部和臀部力量",
                tips = "膝盖不要超过脚尖，保持背部挺直",
                difficulty = 2,
                targetMuscles = listOf("腿部", "臀部"),
                equipment = "无器械"
            ),
            com.vere.fit8.data.model.ExerciseTemplate(
                name = "平板支撑",
                nameEn = "Plank",
                sets = 3,
                reps = 0,
                durationSec = 30,
                restSec = 60,
                description = "核心力量训练，提高身体稳定性",
                tips = "保持身体一条直线，不要塌腰",
                difficulty = 2,
                targetMuscles = listOf("核心", "腹部"),
                equipment = "无器械"
            )
        )

        exerciseAdapter.submitList(defaultExercises)
        hideEmptyState()
    }

    private fun hideEmptyState() {
        // 确保所有视图都可见
        binding.rvExercises.visibility = View.VISIBLE
        // 其他视图默认就是可见的，不需要特别设置
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
