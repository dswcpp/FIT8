package com.vere.fit8.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vere.fit8.databinding.FragmentTrainingBinding
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
        loadTodayTraining()
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
    }
    
    private fun setupRecyclerView() {
        exerciseAdapter = ExerciseAdapter(
            onExerciseClick = { exercise ->
                showExerciseDetail(exercise)
            },
            onCompleteClick = { exercise ->
                viewModel.completeExercise(exercise)
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
                exerciseAdapter.submitList(exercises)
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
        // 显示动作详情对话框
        // 包含动作说明、视频、图片等
    }
    
    private fun showTrainingCompleteDialog(duration: Int, calories: Int) {
        // 显示训练完成对话框
        // 显示训练时长、消耗卡路里、成就等
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
