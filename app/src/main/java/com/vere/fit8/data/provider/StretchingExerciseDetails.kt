package com.vere.fit8.data.provider

import com.vere.fit8.data.model.ExerciseDetail
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 拉伸动作详情数据
 * 包含：全身拉伸、冥想放松等
 */
@Singleton
class StretchingExerciseDetails @Inject constructor() {
    
    fun getExercises(): List<ExerciseDetail> {
        return listOf(
            // 1. 全身拉伸
            ExerciseDetail(
                id = "full_body_stretch",
                name = "全身拉伸",
                category = "拉伸",
                difficulty = "初级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("全身肌群"),
                secondaryMuscles = listOf("关节", "筋膜"),
                description = "全身拉伸是一套综合性的拉伸动作，能够放松全身肌肉，改善柔韧性，促进恢复。",
                benefits = listOf(
                    "改善全身柔韧性",
                    "放松紧张肌肉",
                    "促进血液循环",
                    "减少肌肉酸痛",
                    "预防运动损伤"
                ),
                instructions = listOf(
                    "颈部拉伸：轻柔地左右转动头部",
                    "肩部拉伸：双臂交叉胸前，轻拉对侧肩膀",
                    "背部拉伸：双手向前伸展，拱起背部",
                    "腿部拉伸：坐姿前屈，拉伸腘绳肌",
                    "臀部拉伸：坐姿扭转，拉伸臀部肌肉"
                ),
                commonMistakes = listOf(
                    "拉伸过度用力",
                    "憋气进行拉伸",
                    "拉伸时间过短",
                    "忽略某些肌群"
                ),
                tips = listOf(
                    "拉伸要轻柔，不要用力过猛",
                    "保持深呼吸",
                    "每个动作保持15-30秒",
                    "感受肌肉的拉伸感"
                ),
                variations = listOf(
                    "静态拉伸",
                    "动态拉伸",
                    "PNF拉伸"
                ),
                safetyNotes = listOf(
                    "不要强迫拉伸",
                    "如有疼痛立即停止",
                    "拉伸前适当热身"
                ),
                breathingPattern = "深呼吸，保持放松",
                anatomyAnalysis = "全身拉伸涉及多个肌群和关节，通过拉长肌纤维来改善柔韧性和关节活动度。",
                biomechanics = "通过静态拉伸增加肌肉长度，改善关节活动范围，促进肌肉恢复。",
                progressions = listOf(
                    "增加拉伸时间",
                    "增加拉伸幅度",
                    "添加更多拉伸动作"
                ),
                regressions = listOf(
                    "减少拉伸幅度",
                    "缩短拉伸时间",
                    "选择性拉伸"
                ),
                caloriesBurnedPerMinute = 2,
                recommendedSets = "1组",
                recommendedReps = "10分钟",
                restTime = "无"
            ),
            
            // 2. 冥想放松
            ExerciseDetail(
                id = "meditation_relaxation",
                name = "冥想放松",
                category = "拉伸",
                difficulty = "初级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("精神放松"),
                secondaryMuscles = listOf("呼吸系统"),
                description = "冥想放松是一种身心放松的练习，通过专注呼吸和正念来缓解压力，促进身心恢复。",
                benefits = listOf(
                    "缓解压力和焦虑",
                    "改善睡眠质量",
                    "提高专注力",
                    "促进身心恢复",
                    "降低血压和心率"
                ),
                instructions = listOf(
                    "找一个安静舒适的地方坐下或躺下",
                    "闭上眼睛，放松全身肌肉",
                    "专注于自己的呼吸",
                    "深吸气，慢呼气，保持规律",
                    "让思绪自然流淌，不要强迫"
                ),
                commonMistakes = listOf(
                    "强迫清空思绪",
                    "姿势不舒适",
                    "环境过于嘈杂",
                    "期望立即见效"
                ),
                tips = listOf(
                    "选择安静的环境",
                    "保持舒适的姿势",
                    "专注呼吸，不强迫",
                    "从短时间开始练习"
                ),
                variations = listOf(
                    "呼吸冥想",
                    "身体扫描冥想",
                    "正念冥想",
                    "引导冥想"
                ),
                safetyNotes = listOf(
                    "选择安全舒适的环境",
                    "不要在驾驶时进行",
                    "如有心理问题，咨询专业人士"
                ),
                breathingPattern = "深呼吸，4秒吸气，4秒保持，4秒呼气",
                anatomyAnalysis = "冥想主要影响神经系统，激活副交感神经，促进身体进入放松状态。",
                biomechanics = "通过调节呼吸和放松肌肉来影响自主神经系统，促进恢复。",
                progressions = listOf(
                    "增加冥想时间",
                    "尝试不同冥想技巧",
                    "加入正念练习"
                ),
                regressions = listOf(
                    "缩短冥想时间",
                    "使用引导音频",
                    "简单的深呼吸练习"
                ),
                caloriesBurnedPerMinute = 1,
                recommendedSets = "1组",
                recommendedReps = "10分钟",
                restTime = "无"
            )
        )
    }
}
