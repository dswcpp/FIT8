package com.vere.fit8.data.initializer

import com.vere.fit8.data.model.DietPlan
import com.vere.fit8.data.repository.Fit8Repository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 饮食计划初始化器
 * 基于8周减脂塑形食谱创建预设数据
 */
@Singleton
class DietPlanInitializer @Inject constructor(
    private val repository: Fit8Repository
) {
    
    suspend fun initializeDietPlans() {
        // 检查是否已经初始化过
        val existingPlans = repository.getDietPlans(1)
        if (existingPlans.isNotEmpty()) {
            return // 已经初始化过，跳过
        }
        
        // 创建8周循环食谱数据
        val allDietPlans = mutableListOf<DietPlan>()
        
        // 周一食谱
        allDietPlans.addAll(createMondayMeals())
        
        // 周二食谱
        allDietPlans.addAll(createTuesdayMeals())
        
        // 周三食谱
        allDietPlans.addAll(createWednesdayMeals())
        
        // 周四食谱
        allDietPlans.addAll(createThursdayMeals())
        
        // 周五食谱
        allDietPlans.addAll(createFridayMeals())
        
        // 周六食谱
        allDietPlans.addAll(createSaturdayMeals())
        
        // 周日食谱
        allDietPlans.addAll(createSundayMeals())
        
        // 保存到数据库
        repository.saveDietPlans(allDietPlans)
    }
    
    private fun createMondayMeals(): List<DietPlan> {
        return listOf(
            // 早餐
            DietPlan(
                id = "week1_mon_breakfast_1",
                week = 1,
                mealType = "BREAKFAST",
                foodName = "水煮蛋",
                amount = "2个",
                calories = 140,
                protein = 12.0f,
                carbs = 1.0f,
                fat = 10.0f,
                description = "建议水煮5-7分钟，保持蛋黄半熟状态"
            ),
            DietPlan(
                id = "week1_mon_breakfast_2",
                week = 1,
                mealType = "BREAKFAST",
                foodName = "燕麦粥",
                amount = "50g",
                calories = 190,
                protein = 6.5f,
                carbs = 32.0f,
                fat = 3.5f,
                description = "可加少量蜂蜜调味"
            ),
            DietPlan(
                id = "week1_mon_breakfast_3",
                week = 1,
                mealType = "BREAKFAST",
                foodName = "蓝莓",
                amount = "50g",
                calories = 28,
                protein = 0.4f,
                carbs = 7.0f,
                fat = 0.2f,
                description = "富含抗氧化物质"
            ),
            
            // 加餐
            DietPlan(
                id = "week1_mon_snack_1",
                week = 1,
                mealType = "SNACK",
                foodName = "无糖酸奶",
                amount = "150g",
                calories = 90,
                protein = 9.0f,
                carbs = 6.0f,
                fat = 3.0f,
                description = "选择希腊酸奶更佳"
            ),
            DietPlan(
                id = "week1_mon_snack_2",
                week = 1,
                mealType = "SNACK",
                foodName = "杏仁",
                amount = "10g",
                calories = 58,
                protein = 2.1f,
                carbs = 2.2f,
                fat = 5.0f,
                description = "富含健康脂肪和维生素E"
            ),
            
            // 午餐
            DietPlan(
                id = "week1_mon_lunch_1",
                week = 1,
                mealType = "LUNCH",
                foodName = "糙米饭",
                amount = "80g",
                calories = 280,
                protein = 6.0f,
                carbs = 58.0f,
                fat = 2.0f,
                description = "低GI碳水化合物"
            ),
            DietPlan(
                id = "week1_mon_lunch_2",
                week = 1,
                mealType = "LUNCH",
                foodName = "鸡胸肉",
                amount = "120g",
                calories = 198,
                protein = 37.2f,
                carbs = 0.0f,
                fat = 4.3f,
                description = "去皮烹饪，可用香料调味"
            ),
            DietPlan(
                id = "week1_mon_lunch_3",
                week = 1,
                mealType = "LUNCH",
                foodName = "西兰花",
                amount = "150g",
                calories = 51,
                protein = 4.3f,
                carbs = 10.0f,
                fat = 0.6f,
                description = "蒸煮或水焯，保持脆嫩"
            ),
            DietPlan(
                id = "week1_mon_lunch_4",
                week = 1,
                mealType = "LUNCH",
                foodName = "胡萝卜",
                amount = "50g",
                calories = 20,
                protein = 0.5f,
                carbs = 4.6f,
                fat = 0.1f,
                description = "富含β-胡萝卜素"
            ),
            
            // 晚餐
            DietPlan(
                id = "week1_mon_dinner_1",
                week = 1,
                mealType = "DINNER",
                foodName = "鸡胸肉",
                amount = "100g",
                calories = 165,
                protein = 31.0f,
                carbs = 0.0f,
                fat = 3.6f,
                description = "用于制作生菜鸡胸肉卷"
            ),
            DietPlan(
                id = "week1_mon_dinner_2",
                week = 1,
                mealType = "DINNER",
                foodName = "生菜",
                amount = "80g",
                calories = 11,
                protein = 1.0f,
                carbs = 2.2f,
                fat = 0.2f,
                description = "选择新鲜脆嫩的生菜叶"
            ),
            DietPlan(
                id = "week1_mon_dinner_3",
                week = 1,
                mealType = "DINNER",
                foodName = "番茄",
                amount = "50g",
                calories = 9,
                protein = 0.4f,
                carbs = 1.9f,
                fat = 0.1f,
                description = "富含番茄红素"
            ),
            DietPlan(
                id = "week1_mon_dinner_4",
                week = 1,
                mealType = "DINNER",
                foodName = "全麦卷饼",
                amount = "30g",
                calories = 75,
                protein = 2.7f,
                carbs = 14.0f,
                fat = 1.2f,
                description = "选择无添加糖的全麦卷饼"
            )
        )
    }
    
    private fun createTuesdayMeals(): List<DietPlan> {
        return listOf(
            // 早餐
            DietPlan(
                id = "week1_tue_breakfast_1",
                week = 1,
                mealType = "BREAKFAST",
                foodName = "全麦吐司",
                amount = "2片",
                calories = 160,
                protein = 6.0f,
                carbs = 28.0f,
                fat = 2.5f,
                description = "选择100%全麦面包"
            ),
            DietPlan(
                id = "week1_tue_breakfast_2",
                week = 1,
                mealType = "BREAKFAST",
                foodName = "煎蛋",
                amount = "1个",
                calories = 90,
                protein = 6.0f,
                carbs = 0.5f,
                fat = 7.0f,
                description = "用少量橄榄油煎制"
            ),
            DietPlan(
                id = "week1_tue_breakfast_3",
                week = 1,
                mealType = "BREAKFAST",
                foodName = "牛油果",
                amount = "40g",
                calories = 64,
                protein = 0.8f,
                carbs = 3.4f,
                fat = 5.9f,
                description = "富含单不饱和脂肪酸"
            ),
            
            // 加餐
            DietPlan(
                id = "week1_tue_snack_1",
                week = 1,
                mealType = "SNACK",
                foodName = "香蕉",
                amount = "1根",
                calories = 89,
                protein = 1.1f,
                carbs = 23.0f,
                fat = 0.3f,
                description = "运动前后的理想食物"
            ),
            DietPlan(
                id = "week1_tue_snack_2",
                week = 1,
                mealType = "SNACK",
                foodName = "核桃",
                amount = "5g",
                calories = 33,
                protein = 0.8f,
                carbs = 0.7f,
                fat = 3.3f,
                description = "富含Omega-3脂肪酸"
            )
            
            // 可以继续添加午餐和晚餐...
        )
    }
    
    // 其他天的食谱创建方法...
    private fun createWednesdayMeals(): List<DietPlan> = emptyList()
    private fun createThursdayMeals(): List<DietPlan> = emptyList()
    private fun createFridayMeals(): List<DietPlan> = emptyList()
    private fun createSaturdayMeals(): List<DietPlan> = emptyList()
    private fun createSundayMeals(): List<DietPlan> = emptyList()
}
