package com.vere.fit8.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vere.fit8.databinding.FragmentDietBinding
import com.vere.fit8.ui.adapter.DietPlanAdapter
import com.vere.fit8.ui.adapter.MealRecordAdapter
import com.vere.fit8.ui.viewmodel.DietViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * 饮食Fragment
 * 显示每日饮食计划和记录功能
 */
@AndroidEntryPoint
class DietFragment : Fragment() {
    
    private var _binding: FragmentDietBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: DietViewModel by viewModels()
    
    private lateinit var dietPlanAdapter: DietPlanAdapter
    private lateinit var mealRecordAdapter: MealRecordAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDietBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
        setupRecyclerViews()
        observeViewModel()
        loadTodayDiet()
    }
    
    private fun setupUI() {
        // 设置今日日期
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MM月dd日", Locale.CHINESE)
        binding.tvDate.text = today.format(formatter)
        
        // 设置周数
        binding.tvWeekInfo.text = "第${viewModel.getCurrentWeek()}周 · 第${today.dayOfWeek.value}天"
        
        // 设置点击事件
        binding.btnPreviousDay.setOnClickListener {
            viewModel.previousDay()
        }
        
        binding.btnNextDay.setOnClickListener {
            viewModel.nextDay()
        }
        
        binding.fabAddRecord.setOnClickListener {
            showAddMealDialog()
        }
        
        binding.btnShoppingList.setOnClickListener {
            showShoppingList()
        }
        
        // 设置Tab切换
        binding.tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showDietPlan()
                    1 -> showMealRecords()
                }
            }
            
            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })
    }
    
    private fun setupRecyclerViews() {
        // 饮食计划RecyclerView
        dietPlanAdapter = DietPlanAdapter { dietPlan ->
            showDietPlanDetail(dietPlan)
        }
        
        binding.rvDietPlan.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dietPlanAdapter
        }
        
        // 饮食记录RecyclerView
        mealRecordAdapter = MealRecordAdapter(
            onEditClick = { record ->
                showEditMealDialog(record)
            },
            onDeleteClick = { record ->
                viewModel.deleteMealRecord(record)
            }
        )
        
        binding.rvMealRecords.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mealRecordAdapter
        }
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentDate.collect { date ->
                updateDateDisplay(date)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todayDietPlans.collect { plans ->
                dietPlanAdapter.submitList(plans)
                updateNutritionSummary(plans)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todayMealRecords.collect { records ->
                mealRecordAdapter.submitList(records)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }
    
    private fun loadTodayDiet() {
        viewModel.loadTodayDiet()
    }
    
    private fun updateDateDisplay(date: LocalDate) {
        val formatter = DateTimeFormatter.ofPattern("MM月dd日", Locale.CHINESE)
        binding.tvDate.text = date.format(formatter)
        
        val dayOfWeek = when (date.dayOfWeek.value) {
            1 -> "周一"
            2 -> "周二"
            3 -> "周三"
            4 -> "周四"
            5 -> "周五"
            6 -> "周六"
            7 -> "周日"
            else -> ""
        }
        binding.tvWeekInfo.text = "第${viewModel.getCurrentWeek()}周 · $dayOfWeek"
    }
    
    private fun updateNutritionSummary(plans: List<com.vere.fit8.data.model.DietPlan>) {
        val totalCalories = plans.sumOf { it.calories }
        val totalProtein = plans.sumOf { it.protein.toDouble() }.toFloat()
        val totalCarbs = plans.sumOf { it.carbs.toDouble() }.toFloat()
        val totalFat = plans.sumOf { it.fat.toDouble() }.toFloat()
        
        binding.tvCalories.text = "${totalCalories}kcal"
        binding.tvProtein.text = "${String.format("%.1f", totalProtein)}g"
        binding.tvCarbs.text = "${String.format("%.1f", totalCarbs)}g"
        binding.tvFat.text = "${String.format("%.1f", totalFat)}g"
        
        // 更新进度条
        binding.progressCalories.progress = (totalCalories * 100 / 1700).coerceAtMost(100)
        binding.progressProtein.progress = (totalProtein * 100 / 120).toInt().coerceAtMost(100)
        binding.progressCarbs.progress = (totalCarbs * 100 / 170).toInt().coerceAtMost(100)
        binding.progressFat.progress = (totalFat * 100 / 45).toInt().coerceAtMost(100)
    }
    
    private fun showDietPlan() {
        binding.rvDietPlan.visibility = View.VISIBLE
        binding.rvMealRecords.visibility = View.GONE
        binding.fabAddRecord.visibility = View.GONE
    }
    
    private fun showMealRecords() {
        binding.rvDietPlan.visibility = View.GONE
        binding.rvMealRecords.visibility = View.VISIBLE
        binding.fabAddRecord.visibility = View.VISIBLE
    }
    
    private fun showDietPlanDetail(dietPlan: com.vere.fit8.data.model.DietPlan) {
        // 显示饮食计划详情对话框
        // 包含食材用量、做法、营养信息
    }
    
    private fun showAddMealDialog() {
        // 显示添加饮食记录对话框
        // 支持拍照或文字记录
    }
    
    private fun showEditMealDialog(record: com.vere.fit8.data.model.MealRecord) {
        // 显示编辑饮食记录对话框
    }
    
    private fun showShoppingList() {
        // 显示购物清单
        // 按周汇总食材克数
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
