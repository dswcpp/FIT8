package com.vere.fit8.data.provider

import com.vere.fit8.data.model.ExerciseDetail
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 动作详情数据提供者
 * 整合所有动作详情数据
 */
@Singleton
class ExerciseDetailProvider @Inject constructor(
    private val upperBodyExercises: UpperBodyExerciseDetails,
    private val lowerBodyExercises: LowerBodyExerciseDetails,
    private val hiitExercises: HIITExerciseDetails,
    private val cardioExercises: CardioExerciseDetails,
    private val coreExercises: CoreExerciseDetails,
    private val stretchingExercises: StretchingExerciseDetails
) {
    
    /**
     * 获取所有动作详情
     */
    fun getAllExerciseDetails(): List<ExerciseDetail> {
        return buildList {
            addAll(upperBodyExercises.getExercises())
            addAll(lowerBodyExercises.getExercises())
            addAll(hiitExercises.getExercises())
            addAll(cardioExercises.getExercises())
            addAll(coreExercises.getExercises())
            addAll(stretchingExercises.getExercises())
        }
    }
    
    /**
     * 根据分类获取动作详情
     */
    fun getExercisesByCategory(category: String): List<ExerciseDetail> {
        return when (category) {
            "胸部", "手臂", "肩部" -> upperBodyExercises.getExercises()
            "腿部" -> lowerBodyExercises.getExercises()
            "核心" -> coreExercises.getExercises()
            "有氧" -> cardioExercises.getExercises()
            "HIIT", "全身" -> hiitExercises.getExercises()
            "拉伸" -> stretchingExercises.getExercises()
            else -> getAllExerciseDetails()
        }
    }
    
    /**
     * 根据ID获取动作详情
     */
    fun getExerciseById(id: String): ExerciseDetail? {
        return getAllExerciseDetails().find { it.id == id }
    }
    
    /**
     * 搜索动作
     */
    fun searchExercises(query: String): List<ExerciseDetail> {
        val lowerQuery = query.lowercase()
        return getAllExerciseDetails().filter { exercise ->
            exercise.name.lowercase().contains(lowerQuery) ||
            exercise.primaryMuscles.any { it.lowercase().contains(lowerQuery) } ||
            exercise.equipment.any { it.lowercase().contains(lowerQuery) } ||
            exercise.description.lowercase().contains(lowerQuery)
        }
    }
}
