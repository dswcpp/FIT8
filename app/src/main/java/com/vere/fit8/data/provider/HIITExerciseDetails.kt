package com.vere.fit8.data.provider

import com.vere.fit8.data.model.ExerciseDetail
import javax.inject.Inject
import javax.inject.Singleton

/**
 * HIIT动作详情数据
 * 包含：开合跳、波比跳、登山跑等
 */
@Singleton
class HIITExerciseDetails @Inject constructor() {
    
    fun getExercises(): List<ExerciseDetail> {
        return listOf(
            // 1. 开合跳
            ExerciseDetail(
                id = "jumping_jack",
                name = "开合跳",
                category = "有氧",
                difficulty = "初级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("全身", "心肺"),
                secondaryMuscles = listOf("小腿肌", "肩部"),
                description = "开合跳是一个经典的全身有氧运动，能够快速提高心率，是HIIT训练的常用动作。",
                benefits = listOf(
                    "快速提高心率",
                    "全身协调训练",
                    "燃烧卡路里",
                    "改善心肺功能"
                ),
                instructions = listOf(
                    "双脚并拢站立，双臂自然下垂",
                    "跳跃时双腿分开，同时双臂向上举过头顶",
                    "再次跳跃回到起始位置",
                    "保持快速连续的节奏",
                    "注意落地缓冲"
                ),
                commonMistakes = listOf(
                    "动作幅度不够",
                    "节奏过慢",
                    "落地声音过大",
                    "手臂摆动不协调"
                ),
                tips = listOf(
                    "跳跃时双臂上举，双腿分开",
                    "节奏要快",
                    "保持轻盈的落地",
                    "呼吸要规律"
                ),
                variations = listOf(
                    "半程开合跳",
                    "标准开合跳",
                    "高强度开合跳"
                ),
                safetyNotes = listOf(
                    "避免在硬地面上进行",
                    "穿合适的运动鞋",
                    "如有关节问题，谨慎进行"
                ),
                breathingPattern = "自然呼吸，不要憋气",
                anatomyAnalysis = "开合跳是一个全身性的有氧运动，主要提高心肺功能，同时激活多个肌群进行协调运动。",
                biomechanics = "涉及多个关节的协调运动，提高神经肌肉协调性和反应速度。",
                progressions = listOf(
                    "增加速度",
                    "增加持续时间",
                    "添加变式动作"
                ),
                regressions = listOf(
                    "踏步开合",
                    "半程开合跳"
                ),
                caloriesBurnedPerMinute = 12,
                recommendedSets = "4组",
                recommendedReps = "40秒",
                restTime = "20秒"
            ),
            
            // 2. 波比跳
            ExerciseDetail(
                id = "burpee",
                name = "波比跳",
                category = "全身",
                difficulty = "高级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("全身"),
                secondaryMuscles = listOf("心肺系统"),
                description = "波比跳是最具挑战性的全身训练动作之一，结合了俯卧撑、深蹲跳和跳跃，是HIIT训练的王牌动作。",
                benefits = listOf(
                    "全身综合训练",
                    "极高的卡路里燃烧",
                    "提高爆发力",
                    "增强心肺功能"
                ),
                instructions = listOf(
                    "站立姿势开始",
                    "下蹲，双手撑地",
                    "双脚向后跳成俯卧撑姿势",
                    "完成一个俯卧撑",
                    "双脚跳回蹲姿，然后向上跳跃"
                ),
                commonMistakes = listOf(
                    "动作不连贯",
                    "俯卧撑姿势不正确",
                    "跳跃高度不够",
                    "节奏过快失去控制"
                ),
                tips = listOf(
                    "俯卧撑-跳跃-深蹲跳，连贯动作",
                    "保持动作质量",
                    "控制呼吸节奏",
                    "量力而行"
                ),
                variations = listOf(
                    "半波比（无俯卧撑）",
                    "标准波比跳",
                    "波比跳+转体"
                ),
                safetyNotes = listOf(
                    "动作难度极高，量力而行",
                    "确保充分热身",
                    "如有心脏问题，请咨询医生"
                ),
                breathingPattern = "每个动作阶段调整呼吸",
                anatomyAnalysis = "波比跳激活全身几乎所有主要肌群，是最全面的功能性训练动作之一。",
                biomechanics = "结合了多个运动模式：蹲、推、跳，提高全身协调性和爆发力。",
                progressions = listOf(
                    "增加速度",
                    "增加重复次数",
                    "添加负重",
                    "尝试高级变式"
                ),
                regressions = listOf(
                    "踏步波比",
                    "半波比",
                    "分解动作练习"
                ),
                caloriesBurnedPerMinute = 15,
                recommendedSets = "4组",
                recommendedReps = "40秒",
                restTime = "20秒"
            ),

            // 3. 登山跑
            ExerciseDetail(
                id = "mountain_climber",
                name = "登山跑",
                category = "全身",
                difficulty = "中级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("核心肌群"),
                secondaryMuscles = listOf("肩部", "腿部", "心肺系统"),
                description = "登山跑是一个动态的全身训练动作，结合了核心稳定性和心肺训练，是HIIT训练的经典动作。",
                benefits = listOf(
                    "核心和心肺训练",
                    "全身协调性",
                    "高效燃脂",
                    "提高敏捷性"
                ),
                instructions = listOf(
                    "从平板支撑姿势开始",
                    "保持手臂和核心稳定",
                    "交替将膝盖快速向胸部拉近",
                    "就像在原地爬山一样",
                    "保持快速连续的动作"
                ),
                commonMistakes = listOf(
                    "臀部过高",
                    "手臂不稳定",
                    "动作幅度不够",
                    "节奏不规律"
                ),
                tips = listOf(
                    "平板支撑姿势，双腿交替快速蹬踏",
                    "保持核心稳定",
                    "膝盖尽量靠近胸部",
                    "保持快速节奏"
                ),
                variations = listOf(
                    "慢速登山跑",
                    "标准登山跑",
                    "交叉登山跑"
                ),
                safetyNotes = listOf(
                    "保持手腕稳定",
                    "避免臀部过高",
                    "如有手腕不适，停止训练"
                ),
                breathingPattern = "快速规律呼吸",
                anatomyAnalysis = "主要激活核心肌群维持稳定，同时腿部肌群进行快速交替运动，提高心肺功能。",
                biomechanics = "结合了等长收缩（上肢稳定）和动态收缩（下肢运动），提高全身协调性。",
                progressions = listOf(
                    "增加速度",
                    "增加持续时间",
                    "添加变式动作"
                ),
                regressions = listOf(
                    "慢速登山跑",
                    "膝盖着地登山跑"
                ),
                caloriesBurnedPerMinute = 13,
                recommendedSets = "4组",
                recommendedReps = "40秒",
                restTime = "20秒"
            )
        )
    }
}
