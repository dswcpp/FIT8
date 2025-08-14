package com.vere.fit8.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.vere.fit8.databinding.FragmentHomeBinding
import com.vere.fit8.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * 首页Fragment
 * 显示今日任务、数据概览和打卡功能
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: HomeViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
        observeViewModel()
        loadTodayData()
    }
    
    private fun setupUI() {
        // 设置今日日期
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MM月dd日 EEEE", Locale.CHINESE)
        binding.tvDate.text = today.format(formatter)
        
        // 设置点击事件
        binding.btnCheckIn.setOnClickListener {
            showCheckInDialog()
        }
        
        binding.cardTodayTask.setOnClickListener {
            // 跳转到训练页面
        }
        
        binding.cardWeight.setOnClickListener {
            showWeightInputDialog()
        }
        
        binding.cardBodyFat.setOnClickListener {
            showBodyFatInputDialog()
        }
        
        binding.cardWater.setOnClickListener {
            showWaterInputDialog()
        }
        
        binding.cardSleep.setOnClickListener {
            showSleepInputDialog()
        }
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todayRecord.collect { record ->
                updateUI(record)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todayPlan.collect { plan ->
                updateTodayTask(plan)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userStats.collect { stats ->
                updateStats(stats)
            }
        }
    }
    
    private fun loadTodayData() {
        viewModel.loadTodayData()
    }
    
    private fun updateUI(record: com.vere.fit8.data.model.DailyRecord?) {
        record?.let {
            // 更新体重
            binding.tvWeightValue.text = if (it.weight != null) {
                "${it.weight} 斤"
            } else {
                "未记录"
            }
            
            // 更新体脂率
            binding.tvBodyFatValue.text = if (it.bodyFat != null) {
                "${it.bodyFat}%"
            } else {
                "未记录"
            }
            
            // 更新饮水量
            binding.tvWaterValue.text = "${it.waterMl} ml"
            binding.progressWater.progress = (it.waterMl / 25f).toInt() // 目标2500ml
            
            // 更新睡眠
            binding.tvSleepValue.text = if (it.sleepHours > 0) {
                "${it.sleepHours} 小时"
            } else {
                "未记录"
            }
            
            // 更新训练状态
            binding.tvTrainingStatus.text = if (it.trainingDurationMin > 0) {
                "已完成 ${it.trainingDurationMin}分钟"
            } else {
                "未完成"
            }
            
            // 更新饮食状态
            binding.tvDietStatus.text = if (it.dietOk) "已达标" else "未达标"
        }
    }
    
    private fun updateTodayTask(plan: com.vere.fit8.data.model.WeeklyPlan?) {
        plan?.let {
            binding.tvTaskTitle.text = it.trainingType
            binding.tvTaskDescription.text = it.description
            binding.tvTaskDuration.text = "${it.estimatedDurationMin}分钟"
        }
    }
    
    private fun updateStats(stats: com.vere.fit8.data.model.UserStats?) {
        stats?.let {
            binding.tvConsecutiveDays.text = "${it.consecutiveDays}天"
            binding.tvTotalWorkouts.text = "${it.totalWorkouts}次"
        }
    }
    
    private fun showCheckInDialog() {
        // 显示打卡对话框
        // 这里可以实现一个完整的打卡界面
    }
    
    private fun showWeightInputDialog() {
        // 显示体重输入对话框
    }
    
    private fun showBodyFatInputDialog() {
        // 显示体脂率输入对话框
    }
    
    private fun showWaterInputDialog() {
        // 显示饮水量输入对话框
    }
    
    private fun showSleepInputDialog() {
        // 显示睡眠时长输入对话框
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
