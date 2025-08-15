package com.vere.fit8.data.provider

import com.vere.fit8.data.model.ExerciseDetail
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 下肢动作详情数据
 * 包含：深蹲、跳跃深蹲、弓步蹲、提踵、仰卧抬腿、臀桥
 */
@Singleton
class LowerBodyExerciseDetails @Inject constructor() {
    
    fun getExercises(): List<ExerciseDetail> {
        return listOf(
            // 1. 深蹲
            ExerciseDetail(
                id = "squat",
                name = "深蹲",
                category = "腿部",
                difficulty = "初级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("股四头肌", "臀大肌"),
                secondaryMuscles = listOf("腘绳肌", "小腿肌", "核心肌群"),
                description = "深蹲是下肢训练的王者动作，被称为'动作之王'。它能够全面锻炼下肢肌群，提高功能性力量。",
                benefits = listOf(
                    "增强下肢力量",
                    "改善臀部和大腿线条",
                    "提高日常活动能力",
                    "促进全身肌肉协调"
                ),
                instructions = listOf(
                    "双脚与肩同宽站立，脚尖略向外",
                    "挺胸收腹，双手可放在胸前或伸直向前",
                    "臀部向后坐，膝盖弯曲下蹲",
                    "下蹲至大腿与地面平行或更低",
                    "用力站起回到起始位置"
                ),
                commonMistakes = listOf(
                    "膝盖内扣，增加受伤风险",
                    "重心过于前倾，失去平衡",
                    "下蹲深度不够，效果打折",
                    "脚跟离地，影响稳定性"
                ),
                tips = listOf(
                    "保持膝盖与脚尖方向一致",
                    "重心保持在脚跟",
                    "下蹲时想象坐椅子",
                    "保持胸部挺起，避免弯腰"
                ),
                variations = listOf(
                    "相扑深蹲",
                    "单腿深蹲",
                    "跳跃深蹲",
                    "杯式深蹲"
                ),
                safetyNotes = listOf(
                    "如有膝关节问题，请咨询医生",
                    "确保充分热身",
                    "循序渐进增加难度"
                ),
                breathingPattern = "下蹲时吸气，站起时呼气",
                anatomyAnalysis = "深蹲主要激活股四头肌和臀大肌，同时腘绳肌、小腿肌和核心肌群协同发力。这是一个多关节复合动作。",
                biomechanics = "涉及髋关节和膝关节的同时屈曲和伸展。正确的动作模式对于膝关节健康和力量发展至关重要。",
                progressions = listOf(
                    "增加深蹲深度",
                    "添加负重",
                    "尝试单腿变式",
                    "增加爆发力训练"
                ),
                regressions = listOf(
                    "椅子深蹲",
                    "半程深蹲",
                    "扶墙深蹲"
                ),
                caloriesBurnedPerMinute = 10,
                recommendedSets = "3组",
                recommendedReps = "15次",
                restTime = "30秒"
            ),
            
            // 2. 跳跃深蹲
            ExerciseDetail(
                id = "jump_squat",
                name = "跳跃深蹲",
                category = "腿部",
                difficulty = "中级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("股四头肌", "臀大肌"),
                secondaryMuscles = listOf("小腿肌", "核心肌群"),
                description = "跳跃深蹲是深蹲的爆发力变式，结合了力量训练和有氧运动，能够提高下肢爆发力和心肺功能。",
                benefits = listOf(
                    "提高下肢爆发力",
                    "增强心肺功能",
                    "燃烧更多卡路里",
                    "改善运动表现"
                ),
                instructions = listOf(
                    "双脚与肩同宽站立，脚尖略向外",
                    "下蹲至大腿与地面平行",
                    "用力向上跳跃，双臂可向上摆动",
                    "落地时缓冲，立即进入下一次深蹲",
                    "保持连续的跳跃节奏"
                ),
                commonMistakes = listOf(
                    "落地时膝盖内扣",
                    "跳跃高度不够",
                    "落地缓冲不足",
                    "节奏过快失去控制"
                ),
                tips = listOf(
                    "深蹲后用力跳起",
                    "落地时缓冲",
                    "保持膝盖稳定",
                    "控制跳跃节奏"
                ),
                variations = listOf(
                    "半程跳跃深蹲",
                    "标准跳跃深蹲",
                    "单腿跳跃深蹲"
                ),
                safetyNotes = listOf(
                    "确保落地缓冲",
                    "避免在硬地面上进行",
                    "如有膝关节问题，谨慎进行"
                ),
                breathingPattern = "下蹲时吸气，跳跃时呼气",
                anatomyAnalysis = "跳跃深蹲激活快肌纤维，特别是股四头肌和臀大肌的爆发力纤维，同时提高神经肌肉协调性。",
                biomechanics = "结合了向心收缩（跳跃）和离心收缩（落地缓冲），提高肌肉的爆发力和反应能力。",
                progressions = listOf(
                    "增加跳跃高度",
                    "增加重复次数",
                    "添加负重",
                    "尝试连续跳跃"
                ),
                regressions = listOf(
                    "标准深蹲",
                    "半程跳跃",
                    "椅子深蹲"
                ),
                caloriesBurnedPerMinute = 12,
                recommendedSets = "3组",
                recommendedReps = "12次",
                restTime = "30秒"
            ),

            // 3. 弓步蹲
            ExerciseDetail(
                id = "lunge",
                name = "弓步蹲（12个/腿）",
                category = "腿部",
                difficulty = "中级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("股四头肌", "臀大肌"),
                secondaryMuscles = listOf("腘绳肌", "小腿肌", "核心肌群"),
                description = "弓步蹲是一个单腿力量训练动作，能够有效锻炼下肢力量，同时提高平衡能力和身体协调性。",
                benefits = listOf(
                    "单腿力量训练",
                    "改善平衡能力",
                    "纠正左右不平衡",
                    "提高功能性力量"
                ),
                instructions = listOf(
                    "站立，双脚与髋同宽",
                    "一脚向前迈出一大步",
                    "下蹲至前腿大腿与地面平行",
                    "后腿膝盖接近地面但不触地",
                    "用力推起回到起始位置"
                ),
                commonMistakes = listOf(
                    "步幅过小或过大",
                    "前膝超过脚尖",
                    "身体过度前倾",
                    "后腿承重过多"
                ),
                tips = listOf(
                    "前腿大腿与地面平行",
                    "后腿膝盖接近地面",
                    "保持躯干直立",
                    "重心在前腿"
                ),
                variations = listOf(
                    "静态弓步蹲",
                    "交替弓步蹲",
                    "后退弓步蹲",
                    "侧弓步蹲"
                ),
                safetyNotes = listOf(
                    "确保膝盖稳定",
                    "避免膝盖内扣",
                    "循序渐进增加难度"
                ),
                breathingPattern = "下蹲时吸气，起立时呼气",
                anatomyAnalysis = "弓步蹲主要激活前腿的股四头肌和臀大肌，同时锻炼后腿的髂腰肌和小腿肌。",
                biomechanics = "单腿支撑的动作模式提高了对平衡和稳定性的要求，更接近日常生活中的运动模式。",
                progressions = listOf(
                    "增加步幅",
                    "添加负重",
                    "尝试跳跃弓步",
                    "增加不稳定性"
                ),
                regressions = listOf(
                    "扶墙弓步蹲",
                    "减小步幅",
                    "半程弓步蹲"
                ),
                caloriesBurnedPerMinute = 11,
                recommendedSets = "3组",
                recommendedReps = "12次/腿",
                restTime = "30秒"
            ),

            // 4. 提踵
            ExerciseDetail(
                id = "calf_raise",
                name = "提踵",
                category = "腿部",
                difficulty = "初级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("小腿三头肌"),
                secondaryMuscles = listOf("足底肌群"),
                description = "提踵是专门锻炼小腿肌肉的动作，能够增强小腿力量，改善小腿线条，提高踝关节稳定性。",
                benefits = listOf(
                    "增强小腿力量",
                    "改善小腿线条",
                    "提高踝关节稳定性",
                    "预防小腿抽筋"
                ),
                instructions = listOf(
                    "双脚与肩同宽站立",
                    "脚尖着地，用力提起脚跟",
                    "感受小腿肌肉收缩",
                    "在最高点停顿1秒",
                    "缓慢放下脚跟"
                ),
                commonMistakes = listOf(
                    "动作幅度不够",
                    "速度过快",
                    "没有停顿",
                    "身体不稳定"
                ),
                tips = listOf(
                    "脚尖着地，用力提起脚跟",
                    "感受小腿收缩",
                    "在最高点停顿",
                    "控制下降速度"
                ),
                variations = listOf(
                    "双脚提踵",
                    "单脚提踵",
                    "台阶提踵"
                ),
                safetyNotes = listOf(
                    "保持身体平衡",
                    "避免过度用力",
                    "如有小腿疼痛，停止训练"
                ),
                breathingPattern = "提起时呼气，放下时吸气",
                anatomyAnalysis = "主要激活腓肠肌和比目鱼肌，这两块肌肉组成小腿三头肌，负责足底屈曲。",
                biomechanics = "通过踝关节的足底屈曲动作来锻炼小腿后群肌肉，提高推进力。",
                progressions = listOf(
                    "单脚提踵",
                    "台阶提踵",
                    "负重提踵",
                    "增加重复次数"
                ),
                regressions = listOf(
                    "扶墙提踵",
                    "减小幅度"
                ),
                caloriesBurnedPerMinute = 4,
                recommendedSets = "3组",
                recommendedReps = "20次",
                restTime = "30秒"
            )
        )
    }
}
