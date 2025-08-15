package com.vere.fit8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vere.fit8.data.model.DietPlan
import com.vere.fit8.data.model.MealRecord
import com.vere.fit8.data.model.NutritionSummary
import com.vere.fit8.data.repository.Fit8Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

/**
 * 饮食ViewModel
 * 管理饮食计划和记录数据
 */
@HiltViewModel
class DietViewModel @Inject constructor(
    private val repository: Fit8Repository
) : ViewModel() {
    
    private val _currentDate = MutableStateFlow(LocalDate.now())
    val currentDate: StateFlow<LocalDate> = _currentDate.asStateFlow()
    
    private val _todayDietPlans = MutableStateFlow<List<DietPlan>>(emptyList())
    val todayDietPlans: StateFlow<List<DietPlan>> = _todayDietPlans.asStateFlow()
    
    private val _todayMealRecords = MutableStateFlow<List<MealRecord>>(emptyList())
    val todayMealRecords: StateFlow<List<MealRecord>> = _todayMealRecords.asStateFlow()
    
    private val _nutritionSummary = MutableStateFlow<NutritionSummary?>(null)
    val nutritionSummary: StateFlow<NutritionSummary?> = _nutritionSummary.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun loadTodayDiet() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val date = _currentDate.value
                val currentWeek = getCurrentWeek()

                // 调试：检查数据库中是否有饮食计划数据
                val allPlans = repository.getDietPlans(currentWeek)
                android.util.Log.d("DietViewModel", "Total diet plans in week $currentWeek: ${allPlans.size}")

                // 加载饮食计划 - 根据当前是周几来获取对应的食谱
                val dayOfWeek = date.dayOfWeek.value // 1=周一, 7=周日
                val dietPlans = repository.getDietPlansByDay(currentWeek, dayOfWeek)
                android.util.Log.d("DietViewModel", "Diet plans for day $dayOfWeek: ${dietPlans.size}")

                // 如果没有数据，尝试强制初始化
                if (dietPlans.isEmpty()) {
                    android.util.Log.d("DietViewModel", "No diet plans found, trying to initialize...")
                    // 这里可以触发数据初始化
                    initializeDietPlansIfNeeded()
                    // 重新加载
                    val newDietPlans = repository.getDietPlansByDay(currentWeek, dayOfWeek)
                    _todayDietPlans.value = newDietPlans
                    android.util.Log.d("DietViewModel", "After initialization, diet plans: ${newDietPlans.size}")
                } else {
                    _todayDietPlans.value = dietPlans
                }

                // 加载饮食记录
                val mealRecords = repository.getMealRecords(date)
                _todayMealRecords.value = mealRecords

                // 计算营养汇总
                updateNutritionSummary(mealRecords)

            } catch (e: Exception) {
                e.printStackTrace()
                android.util.Log.e("DietViewModel", "Error loading diet data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun initializeDietPlansIfNeeded() {
        try {
            // 创建一些示例数据用于测试
            val samplePlans = listOf(
                DietPlan(
                    id = "sample_breakfast_1",
                    week = 1,
                    dayOfWeek = LocalDate.now().dayOfWeek.value,
                    mealType = "BREAKFAST",
                    foodName = "燕麦粥",
                    amount = "50g",
                    calories = 190,
                    protein = 6.5f,
                    carbs = 32.0f,
                    fat = 3.5f,
                    description = "营养丰富的早餐"
                ),
                DietPlan(
                    id = "sample_breakfast_2",
                    week = 1,
                    dayOfWeek = LocalDate.now().dayOfWeek.value,
                    mealType = "BREAKFAST",
                    foodName = "鸡蛋",
                    amount = "2个",
                    calories = 140,
                    protein = 12.0f,
                    carbs = 1.0f,
                    fat = 10.0f,
                    description = "优质蛋白质来源"
                ),
                DietPlan(
                    id = "sample_lunch_1",
                    week = 1,
                    dayOfWeek = LocalDate.now().dayOfWeek.value,
                    mealType = "LUNCH",
                    foodName = "鸡胸肉",
                    amount = "120g",
                    calories = 198,
                    protein = 37.2f,
                    carbs = 0.0f,
                    fat = 4.3f,
                    description = "高蛋白低脂肉类"
                ),
                DietPlan(
                    id = "sample_dinner_1",
                    week = 1,
                    dayOfWeek = LocalDate.now().dayOfWeek.value,
                    mealType = "DINNER",
                    foodName = "蔬菜沙拉",
                    amount = "200g",
                    calories = 50,
                    protein = 3.0f,
                    carbs = 8.0f,
                    fat = 1.0f,
                    description = "清爽的晚餐"
                )
            )

            repository.saveDietPlans(samplePlans)
            android.util.Log.d("DietViewModel", "Sample diet plans saved")
        } catch (e: Exception) {
            android.util.Log.e("DietViewModel", "Error initializing sample diet plans", e)
        }
    }
    
    fun previousDay() {
        _currentDate.value = _currentDate.value.minusDays(1)
        loadTodayDiet()
    }
    
    fun nextDay() {
        val tomorrow = LocalDate.now().plusDays(1)
        if (_currentDate.value.isBefore(tomorrow)) {
            _currentDate.value = _currentDate.value.plusDays(1)
            loadTodayDiet()
        }
    }
    
    fun addMealRecord(
        mealType: String,
        foodName: String,
        amount: String,
        calories: Int,
        protein: Float,
        carbs: Float,
        fat: Float,
        notes: String = "",
        photoPath: String = ""
    ) {
        viewModelScope.launch {
            val record = MealRecord(
                id = UUID.randomUUID().toString(),
                date = _currentDate.value,
                mealType = mealType,
                foodName = foodName,
                amount = amount,
                calories = calories,
                protein = protein,
                carbs = carbs,
                fat = fat,
                notes = notes,
                photoPath = photoPath
            )
            
            repository.saveMealRecord(record)
            loadTodayDiet() // 重新加载数据
        }
    }

    // 重载方法：直接接受MealRecord对象
    fun addMealRecord(record: MealRecord) {
        viewModelScope.launch {
            repository.saveMealRecord(record)
            loadTodayDiet() // 重新加载数据
        }
    }
    
    fun updateMealRecord(record: MealRecord) {
        viewModelScope.launch {
            repository.updateMealRecord(record)
            loadTodayDiet()
        }
    }
    
    fun deleteMealRecord(record: MealRecord) {
        viewModelScope.launch {
            repository.deleteMealRecord(record)
            loadTodayDiet()
        }
    }
    
    fun getCurrentWeek(): Int {
        // 计算当前是第几周
        // 这里简化处理，实际应该根据用户开始日期计算
        val startDate = LocalDate.of(2025, 8, 1) // 假设8月1日开始
        val currentDate = _currentDate.value
        val daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, currentDate)
        return ((daysBetween / 7) % 8 + 1).toInt().coerceIn(1, 8)
    }
    
    private fun updateNutritionSummary(mealRecords: List<MealRecord>) {
        val totalCalories = mealRecords.sumOf { it.calories }
        val totalProtein = mealRecords.sumOf { it.protein.toDouble() }.toFloat()
        val totalCarbs = mealRecords.sumOf { it.carbs.toDouble() }.toFloat()
        val totalFat = mealRecords.sumOf { it.fat.toDouble() }.toFloat()
        
        _nutritionSummary.value = NutritionSummary(
            totalCalories = totalCalories,
            totalProtein = totalProtein,
            totalCarbs = totalCarbs,
            totalFat = totalFat
        )
    }
    
    fun generateShoppingList(week: Int): List<ShoppingItem> {
        // 生成购物清单
        // 根据一周的饮食计划汇总食材
        return emptyList() // 简化实现
    }
}

/**
 * 购物清单项目
 */
data class ShoppingItem(
    val name: String,           // 食材名称
    val totalAmount: String,    // 总用量
    val unit: String,           // 单位
    val category: String,       // 分类（蛋白质/蔬菜/主食等）
    val isChecked: Boolean = false // 是否已购买
)
