package com.vere.fit8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vere.fit8.data.model.Achievement
import com.vere.fit8.data.model.AppSettings
import com.vere.fit8.data.model.UserStats
import com.vere.fit8.data.repository.Fit8Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 个人中心ViewModel
 * 管理用户信息和成就数据
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Fit8Repository
) : ViewModel() {
    
    private val _userStats = MutableStateFlow<UserStats?>(null)
    val userStats: StateFlow<UserStats?> = _userStats.asStateFlow()

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    private val _appSettings = MutableStateFlow<AppSettings?>(null)
    val appSettings: StateFlow<AppSettings?> = _appSettings.asStateFlow()

    private val _couponCount = MutableStateFlow(0)
    val couponCount: StateFlow<Int> = _couponCount.asStateFlow()

    private val _photoCount = MutableStateFlow(0)
    val photoCount: StateFlow<Int> = _photoCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun loadUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                // 加载用户统计数据
                val stats = repository.getUserStats()
                _userStats.value = stats

                // 加载用户设置数据（包含个人信息）
                val settings = repository.getAppSettings()
                _appSettings.value = settings

                // 加载成就数据
                val achievements = repository.getUnlockedAchievements() + repository.getLockedAchievements()
                _achievements.value = achievements

                // 加载优惠券数量（模拟数据，实际应该从数据库获取）
                _couponCount.value = 0 // 暂时设为0，后续可以添加优惠券功能

                // 加载进步照片数量（模拟数据，实际应该从数据库获取）
                _photoCount.value = 0 // 暂时设为0，后续可以从ProgressPhoto表获取
                
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun exportUserData() {
        viewModelScope.launch {
            // 实现用户数据导出功能
        }
    }
    
    fun resetUserData() {
        viewModelScope.launch {
            // 实现用户数据重置功能（需要确认对话框）
        }
    }
}
