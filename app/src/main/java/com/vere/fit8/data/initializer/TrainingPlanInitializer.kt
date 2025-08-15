package com.vere.fit8.data.initializer

import com.vere.fit8.data.model.ExerciseTemplate
import com.vere.fit8.data.model.WeeklyPlan
import com.vere.fit8.data.repository.Fit8Repository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 训练计划初始化器
 * 基于开发手册创建8周训练计划数据
 */
@Singleton
class TrainingPlanInitializer @Inject constructor(
    private val repository: Fit8Repository
) {
    
    suspend fun initializeTrainingPlans() {
        // 检查是否已经初始化过
        val existingPlans = repository.getWeeklyPlans(1)
        if (existingPlans.isNotEmpty()) {
            return // 已经初始化过，跳过
        }
        
        // 创建8周训练计划
        val allPlans = mutableListOf<WeeklyPlan>()
        
        // 为每周创建7天的训练计划
        for (week in 1..8) {
            allPlans.addAll(createWeeklyPlans(week))
        }
        
        // 保存到数据库
        repository.saveWeeklyPlans(allPlans)
    }
    
    private fun createWeeklyPlans(week: Int): List<WeeklyPlan> {
        return listOf(
            // 周一：上肢+核心 + 跳绳
            WeeklyPlan(
                id = "week${week}_day1",
                week = week,
                dayOfWeek = 1,
                trainingType = "上肢+核心",
                exercises = createUpperBodyExercises(),
                estimatedDurationMin = 35,
                estimatedCalories = 280,
                description = "上肢力量训练结合核心稳定性练习",
                tips = "保持动作标准，控制节奏，注意呼吸配合"
            ),
            
            // 周二：跑步/快走 + 核心
            WeeklyPlan(
                id = "week${week}_day2",
                week = week,
                dayOfWeek = 2,
                trainingType = "有氧运动",
                exercises = createCardioExercises(),
                estimatedDurationMin = 40,
                estimatedCalories = 320,
                description = "有氧运动提高心肺功能，燃烧脂肪",
                tips = "保持适中强度，心率控制在130-150bpm"
            ),
            
            // 周三：下肢 + 跳绳
            WeeklyPlan(
                id = "week${week}_day3",
                week = week,
                dayOfWeek = 3,
                trainingType = "下肢训练",
                exercises = createLowerBodyExercises(),
                estimatedDurationMin = 30,
                estimatedCalories = 250,
                description = "下肢力量训练，塑造腿部线条",
                tips = "深蹲时膝盖不要超过脚尖，保持背部挺直"
            ),
            
            // 周四：HIIT全身 + 拉伸
            WeeklyPlan(
                id = "week${week}_day4",
                week = week,
                dayOfWeek = 4,
                trainingType = "HIIT训练",
                exercises = createHIITExercises(),
                estimatedDurationMin = 25,
                estimatedCalories = 300,
                description = "高强度间歇训练，快速燃脂",
                tips = "高强度动作40秒，休息20秒，循环进行"
            ),
            
            // 周五：上肢+核心 + 跳绳
            WeeklyPlan(
                id = "week${week}_day5",
                week = week,
                dayOfWeek = 5,
                trainingType = "上肢+核心",
                exercises = createUpperBodyExercises(),
                estimatedDurationMin = 35,
                estimatedCalories = 280,
                description = "上肢力量训练结合核心稳定性练习",
                tips = "保持动作标准，控制节奏，注意呼吸配合"
            ),
            
            // 周六：跑步/快走 + 拉伸
            WeeklyPlan(
                id = "week${week}_day6",
                week = week,
                dayOfWeek = 6,
                trainingType = "有氧运动",
                exercises = createCardioExercises(),
                estimatedDurationMin = 45,
                estimatedCalories = 350,
                description = "长时间有氧运动，提高耐力",
                tips = "保持轻松节奏，可以边运动边聊天的强度"
            ),
            
            // 周日：休息 / 瑜伽
            WeeklyPlan(
                id = "week${week}_day7",
                week = week,
                dayOfWeek = 7,
                trainingType = "休息日",
                exercises = createRestDayExercises(),
                estimatedDurationMin = 20,
                estimatedCalories = 80,
                description = "主动恢复，轻度拉伸和放松",
                tips = "充分休息，为下周训练做准备"
            )
        )
    }
    
    private fun createUpperBodyExercises(): List<ExerciseTemplate> {
        return listOf(
            ExerciseTemplate(
                name = "俯卧撑（标准/跪姿可选）",
                nameEn = "Push-ups",
                sets = 3,
                reps = 12,
                restSec = 30,
                description = "胸肌、三头肌、前三角肌训练",
                tips = "保持身体一条直线，下降至胸部接近地面",
                difficulty = 2,
                targetMuscles = listOf("胸肌", "三头肌", "核心"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "宽距俯卧撑",
                nameEn = "Wide Push-ups",
                sets = 3,
                reps = 12,
                restSec = 30,
                description = "重点训练胸肌外侧",
                tips = "双手距离比肩宽1.5倍，重点感受胸肌发力",
                difficulty = 3,
                targetMuscles = listOf("胸肌外侧", "三头肌"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "窄距俯卧撑",
                nameEn = "Diamond Push-ups",
                sets = 3,
                reps = 10,
                restSec = 30,
                description = "重点训练三头肌",
                tips = "双手形成钻石形状，重点感受三头肌发力",
                difficulty = 4,
                targetMuscles = listOf("三头肌", "胸肌内侧"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "平板支撑",
                nameEn = "Plank",
                sets = 3,
                durationSec = 40,
                restSec = 30,
                description = "核心稳定性训练",
                tips = "保持身体一条直线，收紧腹部，正常呼吸",
                difficulty = 2,
                targetMuscles = listOf("核心", "肩部"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "仰卧交替卷腹（20次/边）",
                nameEn = "Bicycle Crunch",
                sets = 3,
                reps = 20,
                restSec = 30,
                description = "腹直肌和腹斜肌训练",
                tips = "左肘碰右膝，右肘碰左膝，控制节奏",
                difficulty = 2,
                targetMuscles = listOf("腹直肌", "腹斜肌"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "俄罗斯转体（20次/边）",
                nameEn = "Russian Twist",
                sets = 3,
                reps = 20,
                restSec = 30,
                description = "腹斜肌和核心旋转力量训练",
                tips = "坐姿，双脚离地，左右转动躯干",
                difficulty = 3,
                targetMuscles = listOf("腹斜肌", "核心"),
                equipment = "无器械"
            )
        )
    }
    
    private fun createLowerBodyExercises(): List<ExerciseTemplate> {
        return listOf(
            ExerciseTemplate(
                name = "深蹲",
                nameEn = "Squat",
                sets = 3,
                reps = 15,
                restSec = 30,
                description = "下肢综合力量训练",
                tips = "膝盖不超过脚尖，臀部向后坐，保持背部挺直",
                difficulty = 2,
                targetMuscles = listOf("股四头肌", "臀大肌", "股二头肌"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "跳跃深蹲",
                nameEn = "Jump Squat",
                sets = 3,
                reps = 12,
                restSec = 30,
                description = "爆发力和下肢力量训练",
                tips = "深蹲后用力跳起，落地时缓冲",
                difficulty = 3,
                targetMuscles = listOf("股四头肌", "臀大肌", "小腿"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "弓步蹲（12个/腿）",
                nameEn = "Lunge",
                sets = 3,
                reps = 12,
                restSec = 30,
                description = "单腿力量和平衡训练",
                tips = "前腿大腿与地面平行，后腿膝盖接近地面",
                difficulty = 2,
                targetMuscles = listOf("股四头肌", "臀大肌", "股二头肌"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "提踵",
                nameEn = "Calf Raise",
                sets = 3,
                reps = 20,
                restSec = 30,
                description = "小腿肌肉训练",
                tips = "脚尖着地，用力提起脚跟，感受小腿收缩",
                difficulty = 1,
                targetMuscles = listOf("小腿三头肌"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "仰卧抬腿",
                nameEn = "Leg Raise",
                sets = 3,
                reps = 15,
                restSec = 30,
                description = "下腹部和髋屈肌训练",
                tips = "仰卧，双腿伸直抬起至90度，缓慢放下",
                difficulty = 2,
                targetMuscles = listOf("下腹部", "髋屈肌"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "臀桥",
                nameEn = "Glute Bridge",
                sets = 3,
                reps = 15,
                restSec = 30,
                description = "臀大肌和后链训练",
                tips = "仰卧，双脚着地，用力抬起臀部",
                difficulty = 1,
                targetMuscles = listOf("臀大肌", "股二头肌"),
                equipment = "无器械"
            )
        )
    }
    
    private fun createHIITExercises(): List<ExerciseTemplate> {
        return listOf(
            ExerciseTemplate(
                name = "开合跳",
                nameEn = "Jumping Jack",
                sets = 4,
                durationSec = 40,
                restSec = 20,
                description = "全身有氧运动",
                tips = "跳跃时双臂上举，双腿分开，节奏要快",
                difficulty = 2,
                targetMuscles = listOf("全身", "心肺"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "跳跃深蹲",
                nameEn = "Jump Squat",
                sets = 4,
                durationSec = 40,
                restSec = 20,
                description = "下肢爆发力训练",
                tips = "深蹲后用力跳起，连续进行",
                difficulty = 3,
                targetMuscles = listOf("股四头肌", "臀大肌"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "波比跳",
                nameEn = "Burpee",
                sets = 4,
                durationSec = 40,
                restSec = 20,
                description = "全身综合训练",
                tips = "俯卧撑-跳跃-深蹲跳，连贯动作",
                difficulty = 5,
                targetMuscles = listOf("全身"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "登山跑",
                nameEn = "Mountain Climber",
                sets = 4,
                durationSec = 40,
                restSec = 20,
                description = "核心和心肺训练",
                tips = "平板支撑姿势，双腿交替快速蹬踏",
                difficulty = 3,
                targetMuscles = listOf("核心", "心肺"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "俯卧撑",
                nameEn = "Push-up",
                sets = 4,
                durationSec = 40,
                restSec = 20,
                description = "上肢力量训练",
                tips = "快速连续进行，保持动作标准",
                difficulty = 3,
                targetMuscles = listOf("胸肌", "三头肌"),
                equipment = "无器械"
            )
        )
    }
    
    private fun createCardioExercises(): List<ExerciseTemplate> {
        return listOf(
            ExerciseTemplate(
                name = "原地高抬腿",
                nameEn = "High Knees",
                sets = 10,
                durationSec = 40,
                restSec = 60,
                description = "有氧运动，提高心率",
                tips = "膝盖尽量抬高，保持快速节奏",
                difficulty = 2,
                targetMuscles = listOf("心肺", "腿部"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "原地跑步",
                nameEn = "Running in Place",
                sets = 1,
                durationSec = 1200, // 20分钟
                restSec = 0,
                description = "持续有氧运动",
                tips = "保持轻松节奏，可以间歇休息",
                difficulty = 2,
                targetMuscles = listOf("心肺", "全身"),
                equipment = "无器械"
            )
        )
    }
    
    private fun createRestDayExercises(): List<ExerciseTemplate> {
        return listOf(
            ExerciseTemplate(
                name = "全身拉伸",
                nameEn = "Full Body Stretch",
                sets = 1,
                durationSec = 600, // 10分钟
                restSec = 0,
                description = "放松肌肉，促进恢复",
                tips = "缓慢拉伸，保持深呼吸",
                difficulty = 1,
                targetMuscles = listOf("全身"),
                equipment = "无器械"
            ),
            ExerciseTemplate(
                name = "冥想放松",
                nameEn = "Meditation",
                sets = 1,
                durationSec = 600, // 10分钟
                restSec = 0,
                description = "心理放松，缓解压力",
                tips = "找个安静的地方，专注呼吸",
                difficulty = 1,
                targetMuscles = listOf("心理"),
                equipment = "无器械"
            )
        )
    }
}
