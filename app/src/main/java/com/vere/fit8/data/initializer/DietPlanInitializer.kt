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
                dayOfWeek = 1, // 周一
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
                dayOfWeek = 1, // 周一
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
                dayOfWeek = 1, // 周一
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
                dayOfWeek = 1, // 周一
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
                dayOfWeek = 1, // 周一
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
                dayOfWeek = 1, // 周一
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
                dayOfWeek = 1, // 周一
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
                dayOfWeek = 1, // 周一
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
                dayOfWeek = 1, // 周一
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
                dayOfWeek = 1, // 周一
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
                dayOfWeek = 1, // 周一
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
                dayOfWeek = 1, // 周一
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
                dayOfWeek = 1, // 周一
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
                dayOfWeek = 2, // 周二
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
                dayOfWeek = 2, // 周二
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
                dayOfWeek = 2, // 周二
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
                dayOfWeek = 2, // 周二
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
                dayOfWeek = 2, // 周二
                mealType = "SNACK",
                foodName = "核桃",
                amount = "5g",
                calories = 33,
                protein = 0.8f,
                carbs = 0.7f,
                fat = 3.3f,
                description = "富含Omega-3脂肪酸"
            ),

            // 午餐
            DietPlan(
                id = "week1_tue_lunch_1",
                week = 1,
                dayOfWeek = 2, // 周二
                mealType = "LUNCH",
                foodName = "藜麦饭",
                amount = "70g",
                calories = 120,
                protein = 4.4f,
                carbs = 22.0f,
                fat = 1.9f,
                description = "超级食物，富含完全蛋白质"
            ),
            DietPlan(
                id = "week1_tue_lunch_2",
                week = 1,
                dayOfWeek = 2, // 周二
                mealType = "LUNCH",
                foodName = "三文鱼",
                amount = "100g",
                calories = 208,
                protein = 25.4f,
                carbs = 0.0f,
                fat = 12.4f,
                description = "富含Omega-3脂肪酸"
            ),
            DietPlan(
                id = "week1_tue_lunch_3",
                week = 1,
                dayOfWeek = 2, // 周二
                mealType = "LUNCH",
                foodName = "芦笋",
                amount = "100g",
                calories = 20,
                protein = 2.2f,
                carbs = 3.9f,
                fat = 0.1f,
                description = "富含叶酸和维生素K"
            ),
            DietPlan(
                id = "week1_tue_lunch_4",
                week = 1,
                dayOfWeek = 2, // 周二
                mealType = "LUNCH",
                foodName = "菠菜",
                amount = "80g",
                calories = 18,
                protein = 2.3f,
                carbs = 2.9f,
                fat = 0.3f,
                description = "富含铁质和叶绿素"
            ),

            // 晚餐
            DietPlan(
                id = "week1_tue_dinner_1",
                week = 1,
                dayOfWeek = 2, // 周二
                mealType = "DINNER",
                foodName = "虾仁",
                amount = "100g",
                calories = 106,
                protein = 20.1f,
                carbs = 1.0f,
                fat = 1.7f,
                description = "高蛋白低脂海鲜"
            ),
            DietPlan(
                id = "week1_tue_dinner_2",
                week = 1,
                dayOfWeek = 2, // 周二
                mealType = "DINNER",
                foodName = "西葫芦",
                amount = "150g",
                calories = 25,
                protein = 1.8f,
                carbs = 4.9f,
                fat = 0.5f,
                description = "低热量高纤维蔬菜"
            ),
            DietPlan(
                id = "week1_tue_dinner_3",
                week = 1,
                dayOfWeek = 2, // 周二
                mealType = "DINNER",
                foodName = "紫薯",
                amount = "50g",
                calories = 57,
                protein = 1.1f,
                carbs = 13.1f,
                fat = 0.1f,
                description = "富含花青素的优质碳水"
            )
        )
    }
    
    private fun createWednesdayMeals(): List<DietPlan> {
        return listOf(
            // 早餐：牛奶燕麦 50g + 奇亚籽 10g + 草莓 50g
            DietPlan(
                id = "week1_wed_breakfast_1",
                week = 1,
                dayOfWeek = 3, // 周三
                mealType = "BREAKFAST",
                foodName = "牛奶燕麦",
                amount = "50g",
                calories = 190,
                protein = 6.5f,
                carbs = 32.0f,
                fat = 3.5f,
                description = "燕麦用牛奶煮制，营养丰富"
            ),
            DietPlan(
                id = "week1_wed_breakfast_2",
                week = 1,
                dayOfWeek = 3, // 周三
                mealType = "BREAKFAST",
                foodName = "奇亚籽",
                amount = "10g",
                calories = 49,
                protein = 1.7f,
                carbs = 4.2f,
                fat = 3.1f,
                description = "超级食物，富含Omega-3"
            ),
            DietPlan(
                id = "week1_wed_breakfast_3",
                week = 1,
                dayOfWeek = 3, // 周三
                mealType = "BREAKFAST",
                foodName = "草莓",
                amount = "50g",
                calories = 16,
                protein = 0.3f,
                carbs = 3.9f,
                fat = 0.2f,
                description = "富含维生素C和抗氧化物"
            ),

            // 加餐：鸡蛋白 3 个 + 黄瓜条 100g
            DietPlan(
                id = "week1_wed_snack_1",
                week = 1,
                dayOfWeek = 3, // 周三
                mealType = "SNACK",
                foodName = "鸡蛋白",
                amount = "3个",
                calories = 51,
                protein = 10.8f,
                carbs = 0.7f,
                fat = 0.2f,
                description = "纯蛋白质，零脂肪"
            ),
            DietPlan(
                id = "week1_wed_snack_2",
                week = 1,
                dayOfWeek = 3, // 周三
                mealType = "SNACK",
                foodName = "黄瓜条",
                amount = "100g",
                calories = 16,
                protein = 0.7f,
                carbs = 3.6f,
                fat = 0.1f,
                description = "清脆爽口，补充水分"
            ),

            // 午餐：鸡胸肉 120g + 南瓜 80g + 四季豆 150g
            DietPlan(
                id = "week1_wed_lunch_1",
                week = 1,
                dayOfWeek = 3, // 周三
                mealType = "LUNCH",
                foodName = "鸡胸肉",
                amount = "120g",
                calories = 198,
                protein = 37.2f,
                carbs = 0.0f,
                fat = 4.3f,
                description = "去皮烹饪，优质蛋白质"
            ),
            DietPlan(
                id = "week1_wed_lunch_2",
                week = 1,
                dayOfWeek = 3, // 周三
                mealType = "LUNCH",
                foodName = "南瓜",
                amount = "80g",
                calories = 22,
                protein = 0.7f,
                carbs = 5.5f,
                fat = 0.1f,
                description = "富含β-胡萝卜素"
            ),
            DietPlan(
                id = "week1_wed_lunch_3",
                week = 1,
                dayOfWeek = 3, // 周三
                mealType = "LUNCH",
                foodName = "四季豆",
                amount = "150g",
                calories = 42,
                protein = 3.0f,
                carbs = 9.0f,
                fat = 0.3f,
                description = "富含膳食纤维和维生素"
            ),

            // 晚餐：金枪鱼沙拉（罐头金枪鱼 80g + 生菜 100g + 番茄 50g + 橄榄油 5g）
            DietPlan(
                id = "week1_wed_dinner_1",
                week = 1,
                dayOfWeek = 3, // 周三
                mealType = "DINNER",
                foodName = "罐头金枪鱼",
                amount = "80g",
                calories = 116,
                protein = 25.4f,
                carbs = 0.0f,
                fat = 0.8f,
                description = "水浸金枪鱼，高蛋白低脂"
            ),
            DietPlan(
                id = "week1_wed_dinner_2",
                week = 1,
                dayOfWeek = 3, // 周三
                mealType = "DINNER",
                foodName = "生菜",
                amount = "100g",
                calories = 14,
                protein = 1.2f,
                carbs = 2.8f,
                fat = 0.2f,
                description = "新鲜脆嫩，制作沙拉"
            ),
            DietPlan(
                id = "week1_wed_dinner_3",
                week = 1,
                dayOfWeek = 3, // 周三
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
                id = "week1_wed_dinner_4",
                week = 1,
                dayOfWeek = 3, // 周三
                mealType = "DINNER",
                foodName = "橄榄油",
                amount = "5g",
                calories = 45,
                protein = 0.0f,
                carbs = 0.0f,
                fat = 5.0f,
                description = "特级初榨橄榄油，沙拉调味"
            )
        )
    }

    private fun createThursdayMeals(): List<DietPlan> {
        return listOf(
            // 早餐：玉米 100g + 鸡蛋 2 个 + 无糖豆浆 200ml
            DietPlan(
                id = "week1_thu_breakfast_1",
                week = 1,
                dayOfWeek = 4, // 周四
                mealType = "BREAKFAST",
                foodName = "玉米",
                amount = "100g",
                calories = 106,
                protein = 4.0f,
                carbs = 22.8f,
                fat = 1.2f,
                description = "新鲜玉米粒，富含叶黄素"
            ),
            DietPlan(
                id = "week1_thu_breakfast_2",
                week = 1,
                dayOfWeek = 4, // 周四
                mealType = "BREAKFAST",
                foodName = "鸡蛋",
                amount = "2个",
                calories = 140,
                protein = 12.0f,
                carbs = 1.0f,
                fat = 10.0f,
                description = "完全蛋白质来源"
            ),
            DietPlan(
                id = "week1_thu_breakfast_3",
                week = 1,
                dayOfWeek = 4, // 周四
                mealType = "BREAKFAST",
                foodName = "无糖豆浆",
                amount = "200ml",
                calories = 54,
                protein = 4.5f,
                carbs = 3.3f,
                fat = 2.7f,
                description = "植物蛋白，无添加糖"
            ),

            // 加餐：低脂酸奶 150g + 蓝莓 30g
            DietPlan(
                id = "week1_thu_snack_1",
                week = 1,
                dayOfWeek = 4, // 周四
                mealType = "SNACK",
                foodName = "低脂酸奶",
                amount = "150g",
                calories = 90,
                protein = 9.0f,
                carbs = 6.0f,
                fat = 3.0f,
                description = "益生菌丰富，助消化"
            ),
            DietPlan(
                id = "week1_thu_snack_2",
                week = 1,
                dayOfWeek = 4, // 周四
                mealType = "SNACK",
                foodName = "蓝莓",
                amount = "30g",
                calories = 17,
                protein = 0.2f,
                carbs = 4.2f,
                fat = 0.1f,
                description = "富含花青素和抗氧化物"
            ),

            // 午餐：牛肉 100g + 糙米饭 70g + 西兰花 100g + 蘑菇 50g
            DietPlan(
                id = "week1_thu_lunch_1",
                week = 1,
                dayOfWeek = 4, // 周四
                mealType = "LUNCH",
                foodName = "牛肉",
                amount = "100g",
                calories = 250,
                protein = 26.0f,
                carbs = 0.0f,
                fat = 15.0f,
                description = "瘦牛肉，富含铁质"
            ),
            DietPlan(
                id = "week1_thu_lunch_2",
                week = 1,
                dayOfWeek = 4, // 周四
                mealType = "LUNCH",
                foodName = "糙米饭",
                amount = "70g",
                calories = 245,
                protein = 5.3f,
                carbs = 50.8f,
                fat = 1.8f,
                description = "低GI碳水化合物"
            ),
            DietPlan(
                id = "week1_thu_lunch_3",
                week = 1,
                dayOfWeek = 4, // 周四
                mealType = "LUNCH",
                foodName = "西兰花",
                amount = "100g",
                calories = 34,
                protein = 2.9f,
                carbs = 6.6f,
                fat = 0.4f,
                description = "富含维生素C和膳食纤维"
            ),
            DietPlan(
                id = "week1_thu_lunch_4",
                week = 1,
                dayOfWeek = 4, // 周四
                mealType = "LUNCH",
                foodName = "蘑菇",
                amount = "50g",
                calories = 13,
                protein = 1.4f,
                carbs = 2.9f,
                fat = 0.1f,
                description = "富含多种维生素和矿物质"
            ),

            // 晚餐：烤鸡腿肉（去皮）120g + 生菜沙拉
            DietPlan(
                id = "week1_thu_dinner_1",
                week = 1,
                dayOfWeek = 4, // 周四
                mealType = "DINNER",
                foodName = "鸡腿肉",
                amount = "120g",
                calories = 217,
                protein = 24.0f,
                carbs = 0.0f,
                fat = 13.1f,
                description = "去皮烤制，口感嫩滑"
            ),
            DietPlan(
                id = "week1_thu_dinner_2",
                week = 1,
                dayOfWeek = 4, // 周四
                mealType = "DINNER",
                foodName = "生菜沙拉",
                amount = "100g",
                calories = 14,
                protein = 1.2f,
                carbs = 2.8f,
                fat = 0.2f,
                description = "新鲜生菜，清爽低热量"
            )
        )
    }

    private fun createFridayMeals(): List<DietPlan> {
        return listOf(
            // 早餐：全麦吐司 2 片 + 花生酱 10g + 水煮蛋 1 个
            DietPlan(
                id = "week1_fri_breakfast_1",
                week = 1,
                dayOfWeek = 5, // 周五
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
                id = "week1_fri_breakfast_2",
                week = 1,
                dayOfWeek = 5, // 周五
                mealType = "BREAKFAST",
                foodName = "花生酱",
                amount = "10g",
                calories = 59,
                protein = 2.5f,
                carbs = 2.2f,
                fat = 5.0f,
                description = "天然花生酱，无添加糖"
            ),
            DietPlan(
                id = "week1_fri_breakfast_3",
                week = 1,
                dayOfWeek = 5, // 周五
                mealType = "BREAKFAST",
                foodName = "水煮蛋",
                amount = "1个",
                calories = 70,
                protein = 6.0f,
                carbs = 0.5f,
                fat = 5.0f,
                description = "完全蛋白质来源"
            ),

            // 加餐：苹果 1 个 + 杏仁 10g
            DietPlan(
                id = "week1_fri_snack_1",
                week = 1,
                dayOfWeek = 5, // 周五
                mealType = "SNACK",
                foodName = "苹果",
                amount = "1个",
                calories = 52,
                protein = 0.3f,
                carbs = 14.0f,
                fat = 0.2f,
                description = "中等大小，富含果胶"
            ),
            DietPlan(
                id = "week1_fri_snack_2",
                week = 1,
                dayOfWeek = 5, // 周五
                mealType = "SNACK",
                foodName = "杏仁",
                amount = "10g",
                calories = 58,
                protein = 2.1f,
                carbs = 2.2f,
                fat = 5.0f,
                description = "富含健康脂肪和维生素E"
            ),

            // 午餐：鸡胸肉 100g + 荞麦饭 80g + 西葫芦 100g + 胡萝卜 50g
            DietPlan(
                id = "week1_fri_lunch_1",
                week = 1,
                dayOfWeek = 5, // 周五
                mealType = "LUNCH",
                foodName = "鸡胸肉",
                amount = "100g",
                calories = 165,
                protein = 31.0f,
                carbs = 0.0f,
                fat = 3.6f,
                description = "去皮烹饪，优质蛋白质"
            ),
            DietPlan(
                id = "week1_fri_lunch_2",
                week = 1,
                dayOfWeek = 5, // 周五
                mealType = "LUNCH",
                foodName = "荞麦饭",
                amount = "80g",
                calories = 304,
                protein = 11.7f,
                carbs = 61.5f,
                fat = 2.4f,
                description = "富含芦丁，降血脂"
            ),
            DietPlan(
                id = "week1_fri_lunch_3",
                week = 1,
                dayOfWeek = 5, // 周五
                mealType = "LUNCH",
                foodName = "西葫芦",
                amount = "100g",
                calories = 17,
                protein = 1.2f,
                carbs = 3.3f,
                fat = 0.3f,
                description = "低热量高纤维蔬菜"
            ),
            DietPlan(
                id = "week1_fri_lunch_4",
                week = 1,
                dayOfWeek = 5, // 周五
                mealType = "LUNCH",
                foodName = "胡萝卜",
                amount = "50g",
                calories = 20,
                protein = 0.5f,
                carbs = 4.6f,
                fat = 0.1f,
                description = "富含β-胡萝卜素"
            ),

            // 晚餐：蛋白蔬菜汤（鸡蛋白 3 个 + 番茄 50g + 菠菜 80g）
            DietPlan(
                id = "week1_fri_dinner_1",
                week = 1,
                dayOfWeek = 5, // 周五
                mealType = "DINNER",
                foodName = "鸡蛋白",
                amount = "3个",
                calories = 51,
                protein = 10.8f,
                carbs = 0.7f,
                fat = 0.2f,
                description = "纯蛋白质，制作蛋白汤"
            ),
            DietPlan(
                id = "week1_fri_dinner_2",
                week = 1,
                dayOfWeek = 5, // 周五
                mealType = "DINNER",
                foodName = "番茄",
                amount = "50g",
                calories = 9,
                protein = 0.4f,
                carbs = 1.9f,
                fat = 0.1f,
                description = "富含番茄红素，汤底调味"
            ),
            DietPlan(
                id = "week1_fri_dinner_3",
                week = 1,
                dayOfWeek = 5, // 周五
                mealType = "DINNER",
                foodName = "菠菜",
                amount = "80g",
                calories = 18,
                protein = 2.3f,
                carbs = 2.9f,
                fat = 0.3f,
                description = "富含铁质和叶绿素"
            )
        )
    }

    private fun createSaturdayMeals(): List<DietPlan> {
        return listOf(
            // 早餐：燕麦粥 50g + 鸡蛋 1 个 + 牛奶 200ml
            DietPlan(
                id = "week1_sat_breakfast_1",
                week = 1,
                dayOfWeek = 6, // 周六
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
                id = "week1_sat_breakfast_2",
                week = 1,
                dayOfWeek = 6, // 周六
                mealType = "BREAKFAST",
                foodName = "鸡蛋",
                amount = "1个",
                calories = 70,
                protein = 6.0f,
                carbs = 0.5f,
                fat = 5.0f,
                description = "完全蛋白质来源"
            ),
            DietPlan(
                id = "week1_sat_breakfast_3",
                week = 1,
                dayOfWeek = 6, // 周六
                mealType = "BREAKFAST",
                foodName = "牛奶",
                amount = "200ml",
                calories = 108,
                protein = 6.6f,
                carbs = 9.6f,
                fat = 6.0f,
                description = "优质蛋白质和钙质来源"
            ),

            // 加餐：坚果混合 10g + 黄瓜条 100g
            DietPlan(
                id = "week1_sat_snack_1",
                week = 1,
                dayOfWeek = 6, // 周六
                mealType = "SNACK",
                foodName = "坚果混合",
                amount = "10g",
                calories = 58,
                protein = 2.0f,
                carbs = 2.0f,
                fat = 5.0f,
                description = "杏仁、核桃、腰果混合"
            ),
            DietPlan(
                id = "week1_sat_snack_2",
                week = 1,
                dayOfWeek = 6, // 周六
                mealType = "SNACK",
                foodName = "黄瓜条",
                amount = "100g",
                calories = 16,
                protein = 0.7f,
                carbs = 3.6f,
                fat = 0.1f,
                description = "清脆爽口，补充水分"
            ),

            // 午餐：鳕鱼 100g + 藜麦 70g + 芦笋 100g + 西红柿 50g
            DietPlan(
                id = "week1_sat_lunch_1",
                week = 1,
                dayOfWeek = 6, // 周六
                mealType = "LUNCH",
                foodName = "鳕鱼",
                amount = "100g",
                calories = 88,
                protein = 19.2f,
                carbs = 0.0f,
                fat = 0.7f,
                description = "高蛋白低脂白肉鱼"
            ),
            DietPlan(
                id = "week1_sat_lunch_2",
                week = 1,
                dayOfWeek = 6, // 周六
                mealType = "LUNCH",
                foodName = "藜麦",
                amount = "70g",
                calories = 120,
                protein = 4.4f,
                carbs = 22.0f,
                fat = 1.9f,
                description = "超级食物，富含完全蛋白质"
            ),
            DietPlan(
                id = "week1_sat_lunch_3",
                week = 1,
                dayOfWeek = 6, // 周六
                mealType = "LUNCH",
                foodName = "芦笋",
                amount = "100g",
                calories = 20,
                protein = 2.2f,
                carbs = 3.9f,
                fat = 0.1f,
                description = "富含叶酸和维生素K"
            ),
            DietPlan(
                id = "week1_sat_lunch_4",
                week = 1,
                dayOfWeek = 6, // 周六
                mealType = "LUNCH",
                foodName = "西红柿",
                amount = "50g",
                calories = 9,
                protein = 0.4f,
                carbs = 1.9f,
                fat = 0.1f,
                description = "富含番茄红素"
            ),

            // 晚餐：烤鸡胸肉 120g + 生菜 100g
            DietPlan(
                id = "week1_sat_dinner_1",
                week = 1,
                dayOfWeek = 6, // 周六
                mealType = "DINNER",
                foodName = "烤鸡胸肉",
                amount = "120g",
                calories = 198,
                protein = 37.2f,
                carbs = 0.0f,
                fat = 4.3f,
                description = "烤制，香料调味"
            ),
            DietPlan(
                id = "week1_sat_dinner_2",
                week = 1,
                dayOfWeek = 6, // 周六
                mealType = "DINNER",
                foodName = "生菜",
                amount = "100g",
                calories = 14,
                protein = 1.2f,
                carbs = 2.8f,
                fat = 0.2f,
                description = "新鲜脆嫩，制作沙拉"
            )
        )
    }

    private fun createSundayMeals(): List<DietPlan> {
        return listOf(
            // 早餐：全麦面包 2 片 + 鸡蛋 1 个 + 番茄 50g
            DietPlan(
                id = "week1_sun_breakfast_1",
                week = 1,
                dayOfWeek = 7, // 周日
                mealType = "BREAKFAST",
                foodName = "全麦面包",
                amount = "2片",
                calories = 160,
                protein = 6.0f,
                carbs = 28.0f,
                fat = 2.5f,
                description = "选择100%全麦面包"
            ),
            DietPlan(
                id = "week1_sun_breakfast_2",
                week = 1,
                dayOfWeek = 7, // 周日
                mealType = "BREAKFAST",
                foodName = "鸡蛋",
                amount = "1个",
                calories = 70,
                protein = 6.0f,
                carbs = 0.5f,
                fat = 5.0f,
                description = "完全蛋白质来源"
            ),
            DietPlan(
                id = "week1_sun_breakfast_3",
                week = 1,
                dayOfWeek = 7, // 周日
                mealType = "BREAKFAST",
                foodName = "番茄",
                amount = "50g",
                calories = 9,
                protein = 0.4f,
                carbs = 1.9f,
                fat = 0.1f,
                description = "富含番茄红素"
            ),

            // 加餐：低脂酸奶 150g + 核桃 5g
            DietPlan(
                id = "week1_sun_snack_1",
                week = 1,
                dayOfWeek = 7, // 周日
                mealType = "SNACK",
                foodName = "低脂酸奶",
                amount = "150g",
                calories = 90,
                protein = 9.0f,
                carbs = 6.0f,
                fat = 3.0f,
                description = "益生菌丰富，助消化"
            ),
            DietPlan(
                id = "week1_sun_snack_2",
                week = 1,
                dayOfWeek = 7, // 周日
                mealType = "SNACK",
                foodName = "核桃",
                amount = "5g",
                calories = 33,
                protein = 0.8f,
                carbs = 0.7f,
                fat = 3.3f,
                description = "富含Omega-3脂肪酸"
            ),

            // 午餐：鸡胸肉 120g + 红薯 80g + 西兰花 100g + 黄瓜 50g
            DietPlan(
                id = "week1_sun_lunch_1",
                week = 1,
                dayOfWeek = 7, // 周日
                mealType = "LUNCH",
                foodName = "鸡胸肉",
                amount = "120g",
                calories = 198,
                protein = 37.2f,
                carbs = 0.0f,
                fat = 4.3f,
                description = "去皮烹饪，优质蛋白质"
            ),
            DietPlan(
                id = "week1_sun_lunch_2",
                week = 1,
                dayOfWeek = 7, // 周日
                mealType = "LUNCH",
                foodName = "红薯",
                amount = "80g",
                calories = 69,
                protein = 1.3f,
                carbs = 16.0f,
                fat = 0.1f,
                description = "蒸制，富含β-胡萝卜素"
            ),
            DietPlan(
                id = "week1_sun_lunch_3",
                week = 1,
                dayOfWeek = 7, // 周日
                mealType = "LUNCH",
                foodName = "西兰花",
                amount = "100g",
                calories = 34,
                protein = 2.9f,
                carbs = 6.6f,
                fat = 0.4f,
                description = "富含维生素C和膳食纤维"
            ),
            DietPlan(
                id = "week1_sun_lunch_4",
                week = 1,
                dayOfWeek = 7, // 周日
                mealType = "LUNCH",
                foodName = "黄瓜",
                amount = "50g",
                calories = 8,
                protein = 0.4f,
                carbs = 1.8f,
                fat = 0.1f,
                description = "清脆爽口，补充水分"
            ),

            // 晚餐：蔬菜蛋饼（鸡蛋 2 个 + 菠菜 80g + 洋葱 30g）
            DietPlan(
                id = "week1_sun_dinner_1",
                week = 1,
                dayOfWeek = 7, // 周日
                mealType = "DINNER",
                foodName = "鸡蛋",
                amount = "2个",
                calories = 140,
                protein = 12.0f,
                carbs = 1.0f,
                fat = 10.0f,
                description = "制作蔬菜蛋饼的主料"
            ),
            DietPlan(
                id = "week1_sun_dinner_2",
                week = 1,
                dayOfWeek = 7, // 周日
                mealType = "DINNER",
                foodName = "菠菜",
                amount = "80g",
                calories = 18,
                protein = 2.3f,
                carbs = 2.9f,
                fat = 0.3f,
                description = "富含铁质和叶绿素"
            ),
            DietPlan(
                id = "week1_sun_dinner_3",
                week = 1,
                dayOfWeek = 7, // 周日
                mealType = "DINNER",
                foodName = "洋葱",
                amount = "30g",
                calories = 12,
                protein = 0.4f,
                carbs = 2.8f,
                fat = 0.1f,
                description = "增强免疫力，调味佳品"
            )
        )
    }
}
