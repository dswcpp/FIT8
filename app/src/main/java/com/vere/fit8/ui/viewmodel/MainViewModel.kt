package com.vere.fit8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vere.fit8.data.initializer.AchievementInitializer
import com.vere.fit8.data.initializer.DietPlanInitializer
import com.vere.fit8.data.initializer.TrainingPlanInitializer
import com.vere.fit8.data.repository.Fit8Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 主Activity的ViewModel
 * 管理全局状态和数据
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Fit8Repository,
    private val dietPlanInitializer: DietPlanInitializer,
    private val trainingPlanInitializer: TrainingPlanInitializer,
    private val achievementInitializer: AchievementInitializer
) : ViewModel() {
    
    init {
        initializeApp()
    }
    
    private fun initializeApp() {
        viewModelScope.launch {
            // 初始化应用数据
            // 检查是否是首次启动
            // 初始化8周训练计划数据
            initializeTrainingPlans()
            initializeDietPlans()
            initializeAchievements()
        }
    }
    
    private suspend fun initializeTrainingPlans() {
        // 初始化8周训练计划
        trainingPlanInitializer.initializeTrainingPlans()
    }
    
    private suspend fun initializeDietPlans() {
        // 初始化饮食计划
        dietPlanInitializer.initializeDietPlans()
    }
    
    private suspend fun initializeAchievements() {
        // 初始化成就系统
        achievementInitializer.initializeAchievements()
    }
}
