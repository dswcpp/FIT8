package com.vere.fit8.data.provider

import com.vere.fit8.data.model.ExerciseDetail
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 核心动作详情数据
 * 包含：平板支撑、仰卧交替卷腹、俄罗斯转体、仰卧抬腿、臀桥等
 */
@Singleton
class CoreExerciseDetails @Inject constructor() {
    
    fun getExercises(): List<ExerciseDetail> {
        return listOf(
            // 1. 平板支撑
            ExerciseDetail(
                id = "plank",
                name = "平板支撑",
                category = "核心",
                difficulty = "初级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("腹直肌", "腹横肌"),
                secondaryMuscles = listOf("竖脊肌", "肩部", "臀部"),
                description = "平板支撑是最经典的核心稳定性训练动作，能够全面锻炼核心肌群，提高身体稳定性。",
                benefits = listOf(
                    "增强核心稳定性",
                    "改善身体姿态",
                    "预防腰部疼痛",
                    "提高运动表现"
                ),
                instructions = listOf(
                    "俯卧在地面上，前臂撑地，肘部位于肩膀正下方",
                    "双脚脚尖着地，身体保持一条直线",
                    "收紧核心，臀部不要过高或过低",
                    "保持自然呼吸，维持姿势",
                    "坚持指定时间"
                ),
                commonMistakes = listOf(
                    "臀部过高，形成山峰状",
                    "臀部下沉，腰部过度弯曲",
                    "头部过度抬起或下垂",
                    "呼吸不规律或憋气"
                ),
                tips = listOf(
                    "身体从头到脚成一条直线",
                    "收紧核心和臀部",
                    "保持自然呼吸",
                    "从短时间开始，逐渐增加"
                ),
                variations = listOf(
                    "膝盖平板支撑",
                    "标准平板支撑",
                    "单臂平板支撑",
                    "侧平板支撑"
                ),
                safetyNotes = listOf(
                    "如有腰部疼痛，立即停止",
                    "不要憋气",
                    "循序渐进增加时间"
                ),
                breathingPattern = "保持自然深呼吸",
                anatomyAnalysis = "平板支撑主要激活深层核心肌群，包括腹横肌、多裂肌和盆底肌，提供脊柱稳定性。",
                biomechanics = "这是一个等长收缩练习，肌肉在不改变长度的情况下产生张力，提高核心稳定性。",
                progressions = listOf(
                    "增加保持时间",
                    "尝试单臂或单腿变式",
                    "添加动态元素",
                    "增加不稳定性"
                ),
                regressions = listOf(
                    "膝盖平板支撑",
                    "斜坡平板支撑",
                    "墙壁平板支撑"
                ),
                caloriesBurnedPerMinute = 5,
                recommendedSets = "3组",
                recommendedReps = "30秒",
                restTime = "30秒"
            ),
            
            // 2. 仰卧交替卷腹
            ExerciseDetail(
                id = "bicycle_crunch",
                name = "仰卧交替卷腹（20次/边）",
                category = "核心",
                difficulty = "中级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("腹直肌", "腹斜肌"),
                secondaryMuscles = listOf("髂腰肌"),
                description = "仰卧交替卷腹是一个动态的核心训练动作，能够同时锻炼腹直肌和腹斜肌，提高核心力量和协调性。",
                benefits = listOf(
                    "锻炼腹直肌和腹斜肌",
                    "改善核心协调性",
                    "增强腹部力量",
                    "塑造腹部线条"
                ),
                instructions = listOf(
                    "仰卧在地面上，双手轻放在头部两侧",
                    "抬起肩胛骨离地，双腿抬起呈90度",
                    "右肘向左膝靠近，同时右腿伸直",
                    "交替进行，左肘向右膝靠近",
                    "保持连续的交替动作"
                ),
                commonMistakes = listOf(
                    "用手拉头部",
                    "动作过快失去控制",
                    "腿部动作幅度不够",
                    "肩胛骨没有离地"
                ),
                tips = listOf(
                    "手轻放头部，不要用力拉",
                    "重点感受腹部发力",
                    "保持肩胛骨离地",
                    "控制动作节奏"
                ),
                variations = listOf(
                    "慢速交替卷腹",
                    "标准交替卷腹",
                    "快速交替卷腹"
                ),
                safetyNotes = listOf(
                    "不要用手拉头部",
                    "如有颈部不适，停止训练",
                    "保持下背部贴地"
                ),
                breathingPattern = "卷腹时呼气，还原时吸气",
                anatomyAnalysis = "主要激活腹直肌和腹内外斜肌，通过旋转动作增强核心的多方向稳定性。",
                biomechanics = "结合了脊柱屈曲和旋转，模拟日常生活中的复合运动模式。",
                progressions = listOf(
                    "增加重复次数",
                    "增加动作幅度",
                    "添加负重",
                    "增加速度"
                ),
                regressions = listOf(
                    "标准卷腹",
                    "死虫式",
                    "膝盖卷腹"
                ),
                caloriesBurnedPerMinute = 6,
                recommendedSets = "3组",
                recommendedReps = "20次/边",
                restTime = "30秒"
            ),
            
            // 3. 俄罗斯转体
            ExerciseDetail(
                id = "russian_twist",
                name = "俄罗斯转体（20次/边）",
                category = "核心",
                difficulty = "中级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("腹斜肌"),
                secondaryMuscles = listOf("腹直肌", "竖脊肌"),
                description = "俄罗斯转体是一个经典的核心旋转训练动作，主要锻炼腹斜肌，提高核心的旋转力量和稳定性。",
                benefits = listOf(
                    "重点锻炼腹斜肌",
                    "提高核心旋转力量",
                    "改善身体协调性",
                    "塑造腰部线条"
                ),
                instructions = listOf(
                    "坐在地面上，膝盖弯曲，脚跟着地",
                    "身体略向后倾，保持脊柱挺直",
                    "双手合十或握拳放在胸前",
                    "左右旋转躯干，手臂跟随身体转动",
                    "保持双脚稳定，重点感受腹斜肌发力"
                ),
                commonMistakes = listOf(
                    "身体过度后倾",
                    "只有手臂在动，躯干不转",
                    "脚部不稳定",
                    "动作幅度不够"
                ),
                tips = listOf(
                    "保持脊柱挺直",
                    "躯干要真正旋转",
                    "双脚保持稳定",
                    "重点感受腹斜肌发力"
                ),
                variations = listOf(
                    "脚着地俄罗斯转体",
                    "脚离地俄罗斯转体",
                    "负重俄罗斯转体"
                ),
                safetyNotes = listOf(
                    "保持脊柱中立位",
                    "不要过度后倾",
                    "如有腰部不适，停止训练"
                ),
                breathingPattern = "转体时呼气，回中间时吸气",
                anatomyAnalysis = "主要激活腹内外斜肌，通过旋转动作增强核心的抗旋转能力和旋转力量。",
                biomechanics = "涉及脊柱的旋转运动，提高核心在多个平面上的稳定性和力量。",
                progressions = listOf(
                    "脚离地进行",
                    "增加旋转幅度",
                    "添加负重",
                    "增加速度"
                ),
                regressions = listOf(
                    "脚着地进行",
                    "减小旋转幅度",
                    "靠墙支撑"
                ),
                caloriesBurnedPerMinute = 7,
                recommendedSets = "3组",
                recommendedReps = "20次/边",
                restTime = "30秒"
            ),

            // 4. 仰卧抬腿
            ExerciseDetail(
                id = "leg_raise",
                name = "仰卧抬腿",
                category = "核心",
                difficulty = "中级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("下腹部"),
                secondaryMuscles = listOf("髂腰肌", "股四头肌"),
                description = "仰卧抬腿是一个专门锻炼下腹部的动作，能够有效加强下腹肌力量，改善核心稳定性。",
                benefits = listOf(
                    "重点锻炼下腹部",
                    "增强核心力量",
                    "改善腹部线条",
                    "提高髋屈肌力量"
                ),
                instructions = listOf(
                    "仰卧在地面上，双手放在身体两侧",
                    "双腿伸直并拢，脚尖向上",
                    "收紧腹部，抬起双腿至90度",
                    "缓慢放下双腿，但不要触地",
                    "重复抬腿动作"
                ),
                commonMistakes = listOf(
                    "腿部弯曲过多",
                    "下背部离地",
                    "动作过快",
                    "腿部完全放下触地"
                ),
                tips = listOf(
                    "双腿伸直抬起至90度",
                    "缓慢放下",
                    "保持下背部贴地",
                    "重点感受下腹发力"
                ),
                variations = listOf(
                    "膝盖弯曲抬腿",
                    "直腿抬腿",
                    "剪刀腿"
                ),
                safetyNotes = listOf(
                    "保持下背部贴地",
                    "如有腰部不适，停止训练",
                    "动作要控制"
                ),
                breathingPattern = "抬腿时呼气，放下时吸气",
                anatomyAnalysis = "主要激活腹直肌下部和髂腰肌，通过髋关节屈曲来锻炼下腹部力量。",
                biomechanics = "涉及髋关节屈曲和腹部肌肉的向心收缩，重点训练下腹部力量。",
                progressions = listOf(
                    "增加重复次数",
                    "增加保持时间",
                    "添加负重",
                    "尝试单腿变式"
                ),
                regressions = listOf(
                    "膝盖弯曲抬腿",
                    "减小抬腿幅度"
                ),
                caloriesBurnedPerMinute = 6,
                recommendedSets = "3组",
                recommendedReps = "15次",
                restTime = "30秒"
            ),

            // 5. 臀桥
            ExerciseDetail(
                id = "glute_bridge",
                name = "臀桥",
                category = "核心",
                difficulty = "初级",
                equipment = listOf("徒手"),
                primaryMuscles = listOf("臀大肌"),
                secondaryMuscles = listOf("腘绳肌", "竖脊肌", "核心肌群"),
                description = "臀桥是一个经典的臀部和后链训练动作，能够有效激活臀大肌，改善臀部力量和形状。",
                benefits = listOf(
                    "激活臀大肌",
                    "改善臀部形状",
                    "加强后链肌群",
                    "改善骨盆稳定性"
                ),
                instructions = listOf(
                    "仰卧在地面上，膝盖弯曲，双脚着地",
                    "双臂放在身体两侧，掌心向下",
                    "收紧臀部，用力抬起臀部",
                    "形成从膝盖到肩膀的直线",
                    "在最高点停顿，然后缓慢放下"
                ),
                commonMistakes = listOf(
                    "用腰部发力而非臀部",
                    "抬起过高造成腰部过伸",
                    "没有充分激活臀部",
                    "动作过快"
                ),
                tips = listOf(
                    "用力抬起臀部",
                    "重点感受臀部发力",
                    "保持膝盖到肩膀一条直线",
                    "在最高点停顿"
                ),
                variations = listOf(
                    "双脚臀桥",
                    "单脚臀桥",
                    "臀桥保持"
                ),
                safetyNotes = listOf(
                    "不要过度抬高",
                    "重点用臀部发力",
                    "如有腰部不适，检查动作"
                ),
                breathingPattern = "抬起时呼气，放下时吸气",
                anatomyAnalysis = "主要激活臀大肌和腘绳肌，通过髋关节伸展来加强后链肌群。",
                biomechanics = "通过髋关节伸展动作来对抗重力，激活臀部和后链肌群，改善骨盆稳定性。",
                progressions = listOf(
                    "单脚臀桥",
                    "增加保持时间",
                    "添加负重",
                    "增加重复次数"
                ),
                regressions = listOf(
                    "减小抬起幅度",
                    "缩短保持时间"
                ),
                caloriesBurnedPerMinute = 5,
                recommendedSets = "3组",
                recommendedReps = "15次",
                restTime = "30秒"
            )
        )
    }
}
