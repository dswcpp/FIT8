package com.vere.fit8.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vere.fit8.R
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
    private lateinit var mealTimelineAdapter: com.vere.fit8.ui.adapter.MealTimelineAdapter
    
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
        dietPlanAdapter = DietPlanAdapter(
            onItemClick = { dietPlan ->
                showDietPlanDetail(dietPlan)
            },
            onCheckInClick = { dietPlan ->
                completeDietPlan(dietPlan)
            }
        )
        
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

        // 时间线适配器
        mealTimelineAdapter = com.vere.fit8.ui.adapter.MealTimelineAdapter(
            onItemClick = { record ->
                showEditMealDialog(record)
            },
            onDeleteClick = { record ->
                viewModel.deleteMealRecord(record)
            }
        )

        binding.rvMealRecords.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mealTimelineAdapter // 使用时间线适配器
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
                android.util.Log.d("DietFragment", "Received diet plans: ${plans.size}")

                if (plans.isEmpty()) {
                    // 如果没有数据，显示一些示例数据
                    android.util.Log.d("DietFragment", "No diet plans found, showing sample data")
                    showSampleData()
                } else {
                    dietPlanAdapter.submitList(plans)
                    updateNutritionSummary(plans)
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todayMealRecords.collect { records ->
                // 按时间排序（最新的在上面）
                val sortedRecords = records.sortedByDescending { it.createdAt }

                // 如果没有记录，显示一些示例数据（仅用于演示）
                if (sortedRecords.isEmpty()) {
                    showSampleMealRecords()
                } else {
                    // 有真实数据时，显示真实数据
                    mealTimelineAdapter.submitList(sortedRecords)
                }
                android.util.Log.d("DietFragment", "Updated meal timeline with ${sortedRecords.size} records")
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

    private fun showSampleData() {
        // 创建一些示例数据来测试界面
        val sampleDietPlans = listOf(
            com.vere.fit8.data.model.DietPlan(
                id = "sample_breakfast_1",
                week = 1,
                dayOfWeek = java.time.LocalDate.now().dayOfWeek.value,
                mealType = "BREAKFAST",
                foodName = "燕麦粥",
                amount = "50g",
                calories = 190,
                protein = 6.5f,
                carbs = 32.0f,
                fat = 3.5f,
                description = "营养丰富的早餐选择"
            ),
            com.vere.fit8.data.model.DietPlan(
                id = "sample_breakfast_2",
                week = 1,
                dayOfWeek = java.time.LocalDate.now().dayOfWeek.value,
                mealType = "BREAKFAST",
                foodName = "鸡蛋",
                amount = "2个",
                calories = 140,
                protein = 12.0f,
                carbs = 1.0f,
                fat = 10.0f,
                description = "优质蛋白质来源"
            ),
            com.vere.fit8.data.model.DietPlan(
                id = "sample_lunch_1",
                week = 1,
                dayOfWeek = java.time.LocalDate.now().dayOfWeek.value,
                mealType = "LUNCH",
                foodName = "鸡胸肉",
                amount = "120g",
                calories = 198,
                protein = 37.2f,
                carbs = 0.0f,
                fat = 4.3f,
                description = "高蛋白低脂肉类"
            ),
            com.vere.fit8.data.model.DietPlan(
                id = "sample_lunch_2",
                week = 1,
                dayOfWeek = java.time.LocalDate.now().dayOfWeek.value,
                mealType = "LUNCH",
                foodName = "糙米饭",
                amount = "70g",
                calories = 245,
                protein = 5.3f,
                carbs = 50.8f,
                fat = 1.8f,
                description = "低GI碳水化合物"
            ),
            com.vere.fit8.data.model.DietPlan(
                id = "sample_dinner_1",
                week = 1,
                dayOfWeek = java.time.LocalDate.now().dayOfWeek.value,
                mealType = "DINNER",
                foodName = "蔬菜沙拉",
                amount = "200g",
                calories = 50,
                protein = 3.0f,
                carbs = 8.0f,
                fat = 1.0f,
                description = "清爽的晚餐选择"
            ),
            com.vere.fit8.data.model.DietPlan(
                id = "sample_snack_1",
                week = 1,
                dayOfWeek = java.time.LocalDate.now().dayOfWeek.value,
                mealType = "SNACK",
                foodName = "苹果",
                amount = "1个",
                calories = 52,
                protein = 0.3f,
                carbs = 14.0f,
                fat = 0.2f,
                description = "健康的加餐选择"
            )
        )

        android.util.Log.d("DietFragment", "Showing sample data: ${sampleDietPlans.size} items")
        dietPlanAdapter.submitList(sampleDietPlans)
        updateNutritionSummary(sampleDietPlans)
    }

    private fun showSampleMealRecords() {
        // 创建一些示例饮食记录，按时间线显示
        val now = java.time.LocalDateTime.now()
        val today = java.time.LocalDate.now()

        val sampleMealRecords = listOf(
            com.vere.fit8.data.model.MealRecord(
                id = "sample_record_1",
                date = today,
                mealType = "BREAKFAST",
                foodName = "苹果",
                amount = "1个 (约150g)",
                calories = 78,
                protein = 0.3f,
                carbs = 20.6f,
                fat = 0.2f,
                notes = "早餐前的水果",
                recordType = "MANUAL",
                createdAt = now.withHour(6).withMinute(45).withSecond(0)
            ),
            com.vere.fit8.data.model.MealRecord(
                id = "sample_record_2",
                date = today,
                mealType = "BREAKFAST",
                foodName = "燕麦粥",
                amount = "1碗 (200ml)",
                calories = 150,
                protein = 5.0f,
                carbs = 27.0f,
                fat = 3.0f,
                notes = "加了蜂蜜调味",
                recordType = "MANUAL",
                createdAt = now.withHour(7).withMinute(30).withSecond(0)
            ),
            com.vere.fit8.data.model.MealRecord(
                id = "sample_record_3",
                date = today,
                mealType = "SNACK",
                foodName = "饼干",
                amount = "2片",
                calories = 95,
                protein = 1.5f,
                carbs = 15.0f,
                fat = 3.5f,
                notes = "上午加餐",
                recordType = "PHOTO",
                createdAt = now.withHour(9).withMinute(21).withSecond(0)
            ),
            com.vere.fit8.data.model.MealRecord(
                id = "sample_record_4",
                date = today,
                mealType = "LUNCH",
                foodName = "鸡胸肉沙拉",
                amount = "1份",
                calories = 280,
                protein = 35.0f,
                carbs = 8.0f,
                fat = 12.0f,
                notes = "健康午餐，营养均衡",
                recordType = "MANUAL",
                createdAt = now.withHour(12).withMinute(15).withSecond(0)
            ),
            com.vere.fit8.data.model.MealRecord(
                id = "sample_record_5",
                date = today,
                mealType = "SNACK",
                foodName = "酸奶",
                amount = "1杯 (150ml)",
                calories = 120,
                protein = 8.0f,
                carbs = 12.0f,
                fat = 4.0f,
                notes = "下午茶时间",
                recordType = "SCAN",
                createdAt = now.withHour(15).withMinute(30).withSecond(0)
            ),
            com.vere.fit8.data.model.MealRecord(
                id = "sample_record_6",
                date = today,
                mealType = "DINNER",
                foodName = "蔬菜汤",
                amount = "1碗",
                calories = 85,
                protein = 3.0f,
                carbs = 15.0f,
                fat = 1.5f,
                notes = "清淡晚餐",
                recordType = "MANUAL",
                createdAt = now.withHour(18).withMinute(45).withSecond(0)
            )
        )

        // 按时间排序（最新的在上面）
        val sortedRecords = sampleMealRecords.sortedByDescending { it.createdAt }
        mealTimelineAdapter.submitList(sortedRecords)
        android.util.Log.d("DietFragment", "Showing sample meal records: ${sortedRecords.size} items")
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
        val message = buildString {
            append("🍽️ ${dietPlan.mealType}\n\n")
            append("🥘 食物：${dietPlan.foodName}\n")
            append("⚖️ 分量：${dietPlan.amount}\n")
            append("📝 描述：${dietPlan.description}\n\n")
            append("🔥 营养信息：\n")
            append("• 热量：${dietPlan.calories}kcal\n")
            append("• 蛋白质：${String.format("%.1f", dietPlan.protein)}g\n")
            append("• 碳水：${String.format("%.1f", dietPlan.carbs)}g\n")
            append("• 脂肪：${String.format("%.1f", dietPlan.fat)}g\n")
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("🍽️ 饮食计划详情")
            .setMessage(message)
            .setPositiveButton("✅ 完成打卡") { _, _ ->
                completeDietPlan(dietPlan)
            }
            .setNeutralButton("✏️ 自定义记录") { _, _ ->
                showCustomMealDialog(dietPlan)
            }
            .setNegativeButton("❌ 关闭", null)
            .show()
    }

    // 完成饮食计划打卡
    private fun completeDietPlan(dietPlan: com.vere.fit8.data.model.DietPlan) {
        // 将饮食计划转换为饮食记录
        val mealRecord = com.vere.fit8.data.model.MealRecord(
            id = "plan_${dietPlan.id}_${System.currentTimeMillis()}",
            date = viewModel.currentDate.value,
            mealType = dietPlan.mealType,
            foodName = dietPlan.foodName,
            amount = dietPlan.amount,
            calories = dietPlan.calories,
            protein = dietPlan.protein,
            carbs = dietPlan.carbs,
            fat = dietPlan.fat,
            notes = "来自推荐食谱：${dietPlan.description}",
            recordType = "PLAN", // 标记为来自计划
            createdAt = java.time.LocalDateTime.now()
        )

        // 保存到数据库
        viewModel.addMealRecord(mealRecord)

        // 显示成功提示
        val mealTypeName = when(dietPlan.mealType) {
            "BREAKFAST" -> "早餐"
            "LUNCH" -> "午餐"
            "DINNER" -> "晚餐"
            "SNACK" -> "加餐"
            else -> "饮食"
        }

        Toast.makeText(
            requireContext(),
            "✅ ${mealTypeName}打卡成功！已添加到我的记录",
            Toast.LENGTH_SHORT
        ).show()

        // 自动切换到"我的记录"标签页显示结果
        binding.tabLayout.getTabAt(1)?.select()
    }

    // 自定义记录（基于推荐食谱但允许修改）
    private fun showCustomMealDialog(dietPlan: com.vere.fit8.data.model.DietPlan) {
        // 使用自定义布局
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_meal_record, null)

        // 获取控件引用
        val btnBreakfast = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_breakfast)
        val btnLunch = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_lunch)
        val btnDinner = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_dinner)
        val btnSnack = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_snack)
        val etFoodName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_food_name)
        val etAmount = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_amount)
        val etCalories = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_calories)
        val etNotes = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_notes)

        // 餐次按钮列表
        val mealButtons = listOf(btnBreakfast, btnLunch, btnDinner, btnSnack)

        // 预填充推荐食谱的数据
        etFoodName.setText(dietPlan.foodName)
        etAmount.setText(dietPlan.amount)
        etCalories.setText(dietPlan.calories.toString())
        etNotes.setText("基于推荐食谱：${dietPlan.description}")

        // 设置默认选中的餐次
        val defaultButton = when(dietPlan.mealType) {
            "BREAKFAST" -> btnBreakfast
            "LUNCH" -> btnLunch
            "DINNER" -> btnDinner
            "SNACK" -> btnSnack
            else -> btnSnack
        }
        selectMealButton(defaultButton, mealButtons)

        var selectedMealType = when(dietPlan.mealType) {
            "BREAKFAST" -> "早餐"
            "LUNCH" -> "午餐"
            "DINNER" -> "晚餐"
            "SNACK" -> "加餐"
            else -> "加餐"
        }

        // 设置餐次按钮点击事件
        btnBreakfast.setOnClickListener {
            selectedMealType = "早餐"
            selectMealButton(btnBreakfast, mealButtons)
        }
        btnLunch.setOnClickListener {
            selectedMealType = "午餐"
            selectMealButton(btnLunch, mealButtons)
        }
        btnDinner.setOnClickListener {
            selectedMealType = "晚餐"
            selectMealButton(btnDinner, mealButtons)
        }
        btnSnack.setOnClickListener {
            selectedMealType = "加餐"
            selectMealButton(btnSnack, mealButtons)
        }

        // 创建对话框
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("📝 自定义记录（基于推荐）")
            .setView(dialogView)
            .setPositiveButton("💾 保存记录", null)
            .setNegativeButton("❌ 取消", null)
            .create()

        dialog.show()

        // 手动设置保存按钮点击事件
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val foodName = etFoodName.text.toString().trim()
            val amount = etAmount.text.toString().trim()
            val caloriesText = etCalories.text.toString().trim()
            val notes = etNotes.text.toString().trim()

            if (foodName.isNotEmpty() && amount.isNotEmpty() && caloriesText.isNotEmpty()) {
                try {
                    val calories = caloriesText.toInt()

                    // 创建饮食记录
                    val mealRecord = com.vere.fit8.data.model.MealRecord(
                        id = "custom_${dietPlan.id}_${System.currentTimeMillis()}",
                        date = viewModel.currentDate.value,
                        mealType = when(selectedMealType) {
                            "早餐" -> "BREAKFAST"
                            "午餐" -> "LUNCH"
                            "晚餐" -> "DINNER"
                            "加餐" -> "SNACK"
                            else -> "SNACK"
                        },
                        foodName = foodName,
                        amount = amount,
                        calories = calories,
                        protein = dietPlan.protein, // 保留原推荐的营养信息
                        carbs = dietPlan.carbs,
                        fat = dietPlan.fat,
                        notes = notes,
                        recordType = "CUSTOM", // 标记为自定义记录
                        createdAt = java.time.LocalDateTime.now()
                    )

                    // 保存到数据库
                    viewModel.addMealRecord(mealRecord)
                    Toast.makeText(requireContext(), "✅ 自定义${selectedMealType}记录已保存", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()

                    // 自动切换到"我的记录"标签页
                    binding.tabLayout.getTabAt(1)?.select()

                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "⚠️ 请输入有效的热量数值", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "⚠️ 请填写食物名称、分量和热量", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddMealDialog() {
        val mealTypes = arrayOf("早餐", "午餐", "晚餐", "加餐")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("选择餐次")
            .setItems(mealTypes) { _, which ->
                showMealInputDialog(mealTypes[which])
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showMealInputDialog(mealType: String) {
        // 使用自定义布局
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_meal_record, null)

        // 获取控件引用
        val btnBreakfast = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_breakfast)
        val btnLunch = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_lunch)
        val btnDinner = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_dinner)
        val btnSnack = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_snack)
        val etFoodName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_food_name)
        val etAmount = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_amount)
        val etCalories = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_calories)
        val etNotes = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_notes)

        // 餐次按钮列表
        val mealButtons = listOf(btnBreakfast, btnLunch, btnDinner, btnSnack)
        var selectedMealType = mealType

        // 设置默认选中的餐次
        val defaultButton = when(mealType) {
            "早餐" -> btnBreakfast
            "午餐" -> btnLunch
            "晚餐" -> btnDinner
            "加餐" -> btnSnack
            else -> btnSnack
        }
        selectMealButton(defaultButton, mealButtons)

        // 设置餐次按钮点击事件
        btnBreakfast.setOnClickListener {
            selectedMealType = "早餐"
            selectMealButton(btnBreakfast, mealButtons)
        }
        btnLunch.setOnClickListener {
            selectedMealType = "午餐"
            selectMealButton(btnLunch, mealButtons)
        }
        btnDinner.setOnClickListener {
            selectedMealType = "晚餐"
            selectMealButton(btnDinner, mealButtons)
        }
        btnSnack.setOnClickListener {
            selectedMealType = "加餐"
            selectMealButton(btnSnack, mealButtons)
        }

        // 创建美观的对话框
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("🍽️ 记录${selectedMealType}")
            .setView(dialogView)
            .setPositiveButton("💾 保存", null)
            .setNegativeButton("❌ 取消", null)
            .create()

        dialog.show()

        // 手动设置保存按钮点击事件
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val foodName = etFoodName.text.toString().trim()
            val amount = etAmount.text.toString().trim()
            val caloriesText = etCalories.text.toString().trim()
            val notes = etNotes.text.toString().trim()

            if (foodName.isNotEmpty() && amount.isNotEmpty() && caloriesText.isNotEmpty()) {
                try {
                    val calories = caloriesText.toInt()

                    // 创建饮食记录
                    val mealRecord = com.vere.fit8.data.model.MealRecord(
                        id = "record_${System.currentTimeMillis()}",
                        date = viewModel.currentDate.value,
                        mealType = when(selectedMealType) {
                            "早餐" -> "BREAKFAST"
                            "午餐" -> "LUNCH"
                            "晚餐" -> "DINNER"
                            "加餐" -> "SNACK"
                            else -> "SNACK"
                        },
                        foodName = foodName,
                        amount = amount,
                        calories = calories,
                        protein = 0f, // 默认营养值，用户可以后续编辑
                        carbs = 0f,
                        fat = 0f,
                        notes = notes,
                        recordType = "MANUAL",
                        createdAt = java.time.LocalDateTime.now()
                    )

                    // 保存到数据库
                    viewModel.addMealRecord(mealRecord)
                    Toast.makeText(requireContext(), "✅ ${selectedMealType}记录已保存", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()

                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "⚠️ 请输入有效的热量数值", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "⚠️ 请填写食物名称、分量和热量", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 辅助方法：选中餐次按钮
    private fun selectMealButton(selectedButton: com.google.android.material.button.MaterialButton, allButtons: List<com.google.android.material.button.MaterialButton>) {
        allButtons.forEach { button ->
            if (button == selectedButton) {
                button.setBackgroundColor(resources.getColor(R.color.primary, null))
                button.setTextColor(resources.getColor(R.color.on_primary, null))
            } else {
                button.setBackgroundColor(resources.getColor(R.color.surface, null))
                button.setTextColor(resources.getColor(R.color.on_surface, null))
            }
        }
    }
    
    private fun showEditMealDialog(record: com.vere.fit8.data.model.MealRecord) {
        // 使用自定义布局
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_meal_record, null)

        // 获取控件引用
        val spinnerMealType = dialogView.findViewById<android.widget.Spinner>(R.id.spinner_meal_type)
        val etFoodName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_food_name)
        val etAmount = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_amount)
        val etCalories = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_calories)
        val etProtein = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_protein)
        val etCarbs = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_carbs)
        val etFat = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_fat)
        val etNotes = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_notes)

        // 设置餐次选择器
        val mealTypes = arrayOf("早餐", "午餐", "晚餐", "加餐")
        val mealTypeAdapter = android.widget.ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mealTypes)
        mealTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMealType.adapter = mealTypeAdapter

        // 设置当前餐次
        val currentMealTypeIndex = when(record.mealType) {
            "BREAKFAST" -> 0
            "LUNCH" -> 1
            "DINNER" -> 2
            "SNACK" -> 3
            else -> 3
        }
        spinnerMealType.setSelection(currentMealTypeIndex)

        // 填充现有数据
        etFoodName.setText(record.foodName)
        etAmount.setText(record.amount)
        etCalories.setText(record.calories.toString())
        etProtein.setText(if (record.protein > 0) record.protein.toString() else "")
        etCarbs.setText(if (record.carbs > 0) record.carbs.toString() else "")
        etFat.setText(if (record.fat > 0) record.fat.toString() else "")
        etNotes.setText(record.notes)

        // 创建美观的对话框
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("✏️ 编辑饮食记录")
            .setView(dialogView)
            .setPositiveButton("💾 保存", null) // 设置为null，稍后手动设置点击事件
            .setNegativeButton("❌ 取消", null)
            .setNeutralButton("🗑️ 删除", null)
            .create()

        // 显示对话框
        dialog.show()

        // 手动设置按钮点击事件，防止对话框自动关闭
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val foodName = etFoodName.text.toString().trim()
            val amount = etAmount.text.toString().trim()
            val caloriesText = etCalories.text.toString().trim()
            val proteinText = etProtein.text.toString().trim()
            val carbsText = etCarbs.text.toString().trim()
            val fatText = etFat.text.toString().trim()
            val notes = etNotes.text.toString().trim()

            if (foodName.isNotEmpty() && amount.isNotEmpty() && caloriesText.isNotEmpty()) {
                try {
                    val calories = caloriesText.toInt()
                    val protein = if (proteinText.isNotEmpty()) proteinText.toFloat() else 0f
                    val carbs = if (carbsText.isNotEmpty()) carbsText.toFloat() else 0f
                    val fat = if (fatText.isNotEmpty()) fatText.toFloat() else 0f

                    val selectedMealType = when(spinnerMealType.selectedItemPosition) {
                        0 -> "BREAKFAST"
                        1 -> "LUNCH"
                        2 -> "DINNER"
                        3 -> "SNACK"
                        else -> "SNACK"
                    }

                    // 更新记录
                    val updatedRecord = record.copy(
                        mealType = selectedMealType,
                        foodName = foodName,
                        amount = amount,
                        calories = calories,
                        protein = protein,
                        carbs = carbs,
                        fat = fat,
                        notes = notes,
                        updatedAt = java.time.LocalDateTime.now()
                    )

                    // 保存到数据库
                    viewModel.updateMealRecord(updatedRecord)
                    Toast.makeText(requireContext(), "✅ 饮食记录已更新", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()

                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "⚠️ 请输入有效的数值", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "⚠️ 请填写食物名称、分量和热量", Toast.LENGTH_SHORT).show()
            }
        }

        // 删除按钮点击事件
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
            dialog.dismiss()
            showDeleteConfirmDialog(record)
        }
    }

    private fun showDeleteConfirmDialog(record: com.vere.fit8.data.model.MealRecord) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("删除记录")
            .setMessage("确定要删除这条饮食记录吗？\n\n${record.foodName} (${record.amount})")
            .setPositiveButton("删除") { _, _ ->
                viewModel.deleteMealRecord(record)
                Toast.makeText(requireContext(), "记录已删除", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showShoppingList() {
        val shoppingItems = listOf(
            "🥬 青菜 500g",
            "🍖 鸡胸肉 300g",
            "🍚 大米 1kg",
            "🥚 鸡蛋 12个",
            "🐟 三文鱼 200g",
            "🥛 牛奶 1L",
            "🍌 香蕉 6根",
            "🥕 胡萝卜 3根"
        )

        val message = buildString {
            append("📝 本周购物清单：\n\n")
            shoppingItems.forEach { item ->
                append("• $item\n")
            }
            append("\n💡 建议提前准备，确保营养均衡！")
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("购物清单")
            .setMessage(message)
            .setPositiveButton("知道了", null)
            .show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
