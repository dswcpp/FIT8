package com.vere.fit8.data.provider

import com.vere.fit8.data.model.ExerciseDetail
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 有氧动作详情数据
 * 包含：原地高抬腿、原地跑步等
 */
@Singleton
class CardioExerciseDetails @Inject constructor() {
    
    fun getExercises(): List<ExerciseDetail> {
        return listOf(
            // 1. 原地高抬腿
            ExerciseDetail(
                id = "high_knees",
                name = "原地高抬腿",
                category = "有氧",
                difficulty = "初级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("股四头肌", "髂腰肌"),
                secondaryMuscles = listOf("小腿肌", "核心肌群"),
                description = "原地高抬腿是一个简单有效的有氧运动，能够快速提高心率，锻炼下肢力量和协调性。",
                benefits = listOf(
                    "提高心率",
                    "锻炼下肢力量",
                    "改善协调性",
                    "燃烧卡路里"
                ),
                instructions = listOf(
                    "原地站立，保持挺胸收腹",
                    "交替抬高膝盖至腰部高度",
                    "双臂自然摆动配合腿部动作",
                    "保持快速连续的节奏",
                    "脚掌轻盈着地"
                ),
                commonMistakes = listOf(
                    "抬腿高度不够",
                    "身体前倾",
                    "手臂摆动不协调",
                    "落地声音过大"
                ),
                tips = listOf(
                    "膝盖抬至腰部高度",
                    "保持身体直立",
                    "手臂自然摆动",
                    "保持轻盈的步伐"
                ),
                variations = listOf(
                    "慢速高抬腿",
                    "标准高抬腿",
                    "快速高抬腿"
                ),
                safetyNotes = listOf(
                    "穿合适的运动鞋",
                    "避免在硬地面上进行",
                    "循序渐进增加强度"
                ),
                breathingPattern = "自然呼吸，不要憋气",
                anatomyAnalysis = "主要激活髂腰肌和股四头肌，同时锻炼核心稳定性和下肢协调性。",
                biomechanics = "涉及髋关节屈曲和膝关节抬升，提高下肢的灵活性和爆发力。",
                progressions = listOf(
                    "增加速度",
                    "增加持续时间",
                    "增加抬腿高度"
                ),
                regressions = listOf(
                    "慢速抬腿",
                    "降低抬腿高度"
                ),
                caloriesBurnedPerMinute = 10,
                recommendedSets = "3组",
                recommendedReps = "30秒",
                restTime = "30秒"
            ),
            
            // 2. 原地跑步
            ExerciseDetail(
                id = "running_in_place",
                name = "原地跑步",
                category = "有氧",
                difficulty = "初级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("心肺系统"),
                secondaryMuscles = listOf("小腿肌", "股四头肌"),
                description = "原地跑步是最基础的有氧运动，适合所有健身水平的人群，是很好的热身和有氧训练动作。",
                benefits = listOf(
                    "改善心肺功能",
                    "简单易学",
                    "随时随地可做",
                    "适合热身"
                ),
                instructions = listOf(
                    "原地站立，保持自然姿势",
                    "开始轻松的原地跑步动作",
                    "双臂自然摆动",
                    "保持均匀的呼吸",
                    "逐渐提高速度"
                ),
                commonMistakes = listOf(
                    "步幅过大",
                    "身体过度前倾",
                    "呼吸不规律",
                    "速度过快"
                ),
                tips = listOf(
                    "保持轻松的步伐",
                    "自然摆臂",
                    "均匀呼吸",
                    "循序渐进"
                ),
                variations = listOf(
                    "慢速原地跑",
                    "中速原地跑",
                    "快速原地跑"
                ),
                safetyNotes = listOf(
                    "穿合适的运动鞋",
                    "保持良好姿势",
                    "避免过度疲劳"
                ),
                breathingPattern = "深呼吸，保持规律",
                anatomyAnalysis = "主要锻炼心肺系统，同时激活下肢多个肌群进行协调运动。",
                biomechanics = "模拟跑步动作，提高心肺功能和下肢协调性。",
                progressions = listOf(
                    "增加速度",
                    "增加持续时间",
                    "添加变化动作"
                ),
                regressions = listOf(
                    "原地踏步",
                    "慢走"
                ),
                caloriesBurnedPerMinute = 8,
                recommendedSets = "1组",
                recommendedReps = "5分钟",
                restTime = "1分钟"
            )
        )
    }
}
