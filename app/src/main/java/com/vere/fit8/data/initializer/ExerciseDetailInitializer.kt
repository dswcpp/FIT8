package com.vere.fit8.data.initializer

import com.vere.fit8.data.provider.ExerciseDetailProvider
import com.vere.fit8.data.repository.Fit8Repository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 动作详情初始化器
 * 负责在应用启动时初始化动作详情数据
 */
@Singleton
class ExerciseDetailInitializer @Inject constructor(
    private val repository: Fit8Repository,
    private val exerciseDetailProvider: ExerciseDetailProvider
) {
    
    suspend fun initializeExerciseDetails() {
        // 检查是否已经初始化过
        val existingExercises = repository.getAllExerciseDetails()
        if (existingExercises.isNotEmpty()) {
            return // 已经初始化过，跳过
        }
        
        // 获取所有动作详情数据
        val allExerciseDetails = exerciseDetailProvider.getAllExerciseDetails()
        
        // 保存到数据库
        repository.insertExerciseDetails(allExerciseDetails)
    }
}
