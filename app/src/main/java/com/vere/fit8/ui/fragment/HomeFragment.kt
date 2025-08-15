package com.vere.fit8.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vere.fit8.MainActivity
import com.vere.fit8.R
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
            startActivity(Intent(requireContext(), com.vere.fit8.ui.activity.TodayTaskActivity::class.java))
        }
        
        binding.cardTodayTask.setOnClickListener {
            startActivity(Intent(requireContext(), com.vere.fit8.ui.activity.TodayTaskActivity::class.java))
        }

        binding.cardWeight.setOnClickListener {
            startActivity(Intent(requireContext(), com.vere.fit8.ui.activity.WeightInputActivity::class.java))
        }

        // 移除不存在的cardCalories点击事件

        binding.cardWater.setOnClickListener {
            startActivity(Intent(requireContext(), com.vere.fit8.ui.activity.WaterInputActivity::class.java))
        }

        binding.cardWorkout.setOnClickListener {
            // 跳转到训练页面
            (activity as? MainActivity)?.switchToTab(1)
        }

        binding.cardDiet.setOnClickListener {
            // 跳转到饮食页面
            (activity as? MainActivity)?.switchToTab(2)
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
        // DailyRecord不包含统计数据，这些数据需要从UserStats获取
        // 暂时留空，等待添加UserStats的观察
    }
    
    private fun updateTodayTask(plan: com.vere.fit8.data.model.WeeklyPlan?) {
        if (plan != null) {
            binding.tvTaskTitle.text = plan.trainingType
            binding.tvTaskDescription.text = plan.description
            binding.tvTaskDuration.text = "${plan.estimatedDurationMin}分钟"
        } else {
            binding.tvTaskTitle.text = "休息日"
            binding.tvTaskDescription.text = "今天是休息日，好好放松一下吧！"
            binding.tvTaskDuration.text = "0分钟"
        }
    }
    
    private fun updateStats(stats: com.vere.fit8.data.model.UserStats?) {
        stats?.let {
            binding.tvConsecutiveDays.text = "${it.consecutiveDays}天"
            binding.tvTotalWorkouts.text = "${it.totalWorkouts}次"
        }
    }
    
    // 打卡功能已移至今日任务页面
    
    // 体重输入功能已移至专门页面
    
    // 体脂率和饮水输入功能已移至专门页面
    
    // 睡眠记录功能已移至专门页面

    // 今日任务功能已移至专门页面

    // 所有对话框功能已移至专门页面

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
