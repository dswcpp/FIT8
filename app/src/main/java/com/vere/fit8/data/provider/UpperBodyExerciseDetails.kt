package com.vere.fit8.data.provider

import com.vere.fit8.data.model.ExerciseDetail
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 上肢动作详情数据
 * 包含：俯卧撑、宽距俯卧撑、窄距俯卧撑、平板支撑等
 */
@Singleton
class UpperBodyExerciseDetails @Inject constructor() {
    
    fun getExercises(): List<ExerciseDetail> {
        return listOf(
            // 1. 俯卧撑（标准/跪姿可选）
            ExerciseDetail(
                id = "push_up_standard",
                name = "俯卧撑（标准/跪姿可选）",
                category = "胸部",
                difficulty = "初级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("胸大肌", "三角肌前束"),
                secondaryMuscles = listOf("肱三头肌", "前锯肌", "核心肌群"),
                description = "俯卧撑是一个经典的复合动作，主要锻炼胸大肌、三角肌前束和肱三头肌。这是一个非常实用的徒手训练动作，可以随时随地进行。",
                benefits = listOf(
                    "增强胸部肌肉力量",
                    "改善上肢推力",
                    "提高核心稳定性",
                    "增强肩关节稳定性"
                ),
                instructions = listOf(
                    "俯卧在地面上，双手撑地，手掌位于肩膀正下方",
                    "双脚并拢，脚尖着地，身体保持一条直线",
                    "收紧核心，缓慢下降身体直到胸部接近地面",
                    "用力推起身体回到起始位置",
                    "重复动作，保持控制和稳定"
                ),
                commonMistakes = listOf(
                    "臀部过高或过低，身体不成直线",
                    "手臂张开过宽，增加肩关节压力",
                    "下降幅度不够，影响训练效果",
                    "动作过快，缺乏控制"
                ),
                tips = listOf(
                    "保持身体从头到脚成一条直线",
                    "控制动作节奏，下降2秒，推起1秒",
                    "呼气时推起，吸气时下降",
                    "如果太难，可以膝盖着地进行"
                ),
                variations = listOf(
                    "膝盖俯卧撑（初学者）",
                    "标准俯卧撑",
                    "宽距俯卧撑",
                    "窄距俯卧撑"
                ),
                safetyNotes = listOf(
                    "如有肩部或手腕疼痛，请停止训练",
                    "确保充分热身",
                    "循序渐进，不要急于求成"
                ),
                breathingPattern = "下降时吸气，推起时呼气",
                anatomyAnalysis = "俯卧撑主要激活胸大肌的胸肋部分，同时三角肌前束和肱三头肌作为协同肌群参与发力。核心肌群在整个动作过程中保持身体稳定。",
                biomechanics = "这是一个闭链运动，手部固定，身体移动。主要涉及肩关节水平内收和肘关节伸展。正确的身体排列对于力量传递和安全性至关重要。",
                progressions = listOf(
                    "增加重复次数",
                    "尝试更难的变式",
                    "增加训练频率",
                    "添加负重"
                ),
                regressions = listOf(
                    "墙壁俯卧撑",
                    "斜坡俯卧撑",
                    "膝盖俯卧撑"
                ),
                caloriesBurnedPerMinute = 8,
                recommendedSets = "3组",
                recommendedReps = "12次",
                restTime = "30秒"
            ),
            
            // 2. 宽距俯卧撑
            ExerciseDetail(
                id = "wide_push_up",
                name = "宽距俯卧撑",
                category = "胸部",
                difficulty = "中级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("胸大肌外侧"),
                secondaryMuscles = listOf("三角肌前束", "肱三头肌"),
                description = "宽距俯卧撑是标准俯卧撑的变式，通过增加手臂间距来重点训练胸肌外侧，增加胸部宽度。",
                benefits = listOf(
                    "重点锻炼胸肌外侧",
                    "增加胸部宽度",
                    "改善胸肌形状",
                    "增强肩关节稳定性"
                ),
                instructions = listOf(
                    "俯卧在地面上，双手撑地，手距比肩宽1.5倍",
                    "双脚并拢，脚尖着地，身体保持一条直线",
                    "收紧核心，缓慢下降身体直到胸部接近地面",
                    "重点感受胸肌外侧发力，推起身体",
                    "重复动作，保持控制"
                ),
                commonMistakes = listOf(
                    "手臂张开过宽，增加肩关节压力",
                    "身体不成直线",
                    "下降幅度不够",
                    "忽略胸肌发力感受"
                ),
                tips = listOf(
                    "双手距离比肩宽1.5倍",
                    "重点感受胸肌发力",
                    "保持身体稳定",
                    "控制动作节奏"
                ),
                variations = listOf(
                    "膝盖宽距俯卧撑",
                    "标准宽距俯卧撑",
                    "单臂宽距俯卧撑"
                ),
                safetyNotes = listOf(
                    "不要手臂张开过宽",
                    "如有肩部不适，立即停止",
                    "确保充分热身"
                ),
                breathingPattern = "下降时吸气，推起时呼气",
                anatomyAnalysis = "宽距俯卧撑主要激活胸大肌的外侧纤维，通过增加手臂间距改变肌肉激活模式，更多地刺激胸肌外侧。",
                biomechanics = "较宽的手臂位置增加了肩关节的外展角度，使胸大肌在更大的拉伸位置下工作，增强对胸肌外侧的刺激。",
                progressions = listOf(
                    "增加重复次数",
                    "增加下降深度",
                    "尝试单臂变式"
                ),
                regressions = listOf(
                    "膝盖宽距俯卧撑",
                    "斜坡宽距俯卧撑"
                ),
                caloriesBurnedPerMinute = 9,
                recommendedSets = "3组",
                recommendedReps = "12次",
                restTime = "30秒"
            ),

            // 3. 窄距俯卧撑（钻石俯卧撑）
            ExerciseDetail(
                id = "diamond_push_up",
                name = "窄距俯卧撑",
                category = "手臂",
                difficulty = "高级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("肱三头肌"),
                secondaryMuscles = listOf("胸大肌内侧", "三角肌前束"),
                description = "窄距俯卧撑，也称钻石俯卧撑，是俯卧撑的高难度变式，主要锻炼肱三头肌，对上肢力量要求较高。",
                benefits = listOf(
                    "重点锻炼肱三头肌",
                    "增强手臂力量",
                    "改善手臂线条",
                    "提高上肢稳定性"
                ),
                instructions = listOf(
                    "俯卧在地面上，双手形成钻石形状",
                    "拇指和食指相触，形成三角形",
                    "身体保持一条直线，收紧核心",
                    "缓慢下降身体，重点感受三头肌发力",
                    "用力推起身体回到起始位置"
                ),
                commonMistakes = listOf(
                    "手臂位置不正确",
                    "身体不稳定",
                    "下降幅度不够",
                    "动作过快失去控制"
                ),
                tips = listOf(
                    "双手形成钻石形状",
                    "重点感受三头肌发力",
                    "保持身体稳定",
                    "如果太难，可以膝盖着地"
                ),
                variations = listOf(
                    "膝盖钻石俯卧撑",
                    "标准钻石俯卧撑",
                    "单臂钻石俯卧撑"
                ),
                safetyNotes = listOf(
                    "动作难度较高，量力而行",
                    "确保手腕无不适",
                    "循序渐进练习"
                ),
                breathingPattern = "下降时吸气，推起时呼气",
                anatomyAnalysis = "窄距俯卧撑主要激活肱三头肌的三个头，特别是长头和内侧头。较窄的手臂位置增加了对三头肌的刺激。",
                biomechanics = "窄距手臂位置增加了肘关节的屈曲角度，使肱三头肌在更大的拉伸范围内工作，提高训练效果。",
                progressions = listOf(
                    "增加重复次数",
                    "增加下降深度",
                    "尝试单臂变式",
                    "添加负重"
                ),
                regressions = listOf(
                    "膝盖钻石俯卧撑",
                    "斜坡钻石俯卧撑",
                    "标准俯卧撑"
                ),
                caloriesBurnedPerMinute = 10,
                recommendedSets = "3组",
                recommendedReps = "10次",
                restTime = "30秒"
            )
        )
    }
}
