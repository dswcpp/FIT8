package com.vere.fit8.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vere.fit8.data.model.AppSettings
import com.vere.fit8.data.repository.Fit8Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 设置页面ViewModel
 * 管理应用设置和用户偏好
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val repository: Fit8Repository
) : AndroidViewModel(application) {
    
    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()
    
    fun loadSettings() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                val settings = repository.getAppSettings() ?: AppSettings()
                _settings.value = settings
                
                // 如果是首次使用，保存默认设置
                if (repository.getAppSettings() == null) {
                    repository.saveAppSettings(settings)
                }
                
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "加载设置失败"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // 通知设置
    fun updateTrainingReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateTrainingReminderEnabled(enabled)
                _settings.value = _settings.value.copy(trainingReminderEnabled = enabled)
                _message.value = if (enabled) "训练提醒已开启" else "训练提醒已关闭"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "设置失败"
            }
        }
    }
    
    fun updateTrainingReminderTime(time: String) {
        viewModelScope.launch {
            try {
                repository.updateTrainingReminderTime(time)
                _settings.value = _settings.value.copy(trainingReminderTime = time)
                _message.value = "训练提醒时间已设置为 $time"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "设置失败"
            }
        }
    }
    
    fun updateWaterReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateWaterReminderEnabled(enabled)
                _settings.value = _settings.value.copy(waterReminderEnabled = enabled)
                _message.value = if (enabled) "饮水提醒已开启" else "饮水提醒已关闭"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "设置失败"
            }
        }
    }
    
    fun updateSleepReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateSleepReminderEnabled(enabled)
                _settings.value = _settings.value.copy(sleepReminderEnabled = enabled)
                _message.value = if (enabled) "睡眠提醒已开启" else "睡眠提醒已关闭"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "设置失败"
            }
        }
    }
    
    fun updateSleepReminderTime(time: String) {
        viewModelScope.launch {
            try {
                repository.updateSleepReminderTime(time)
                _settings.value = _settings.value.copy(sleepReminderTime = time)
                _message.value = "睡眠提醒时间已设置为 $time"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "设置失败"
            }
        }
    }
    
    // 应用设置
    fun updateDarkModeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateDarkModeEnabled(enabled)
                _settings.value = _settings.value.copy(darkModeEnabled = enabled)
                _message.value = if (enabled) "深色模式已开启" else "深色模式已关闭"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "设置失败"
            }
        }
    }
    
    fun updateLanguage(language: String) {
        viewModelScope.launch {
            try {
                repository.updateLanguage(language)
                _settings.value = _settings.value.copy(language = language)
                val languageName = if (language == "zh") "中文" else "English"
                _message.value = "语言已切换为 $languageName"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "设置失败"
            }
        }
    }
    
    // 数据管理
    fun syncData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                // 模拟数据同步
                kotlinx.coroutines.delay(2000)
                _message.value = "数据同步成功"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "数据同步失败"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun exportTrainingData() {
        viewModelScope.launch {
            try {
                // 实现训练数据导出
                _message.value = "训练数据导出功能开发中"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "导出失败"
            }
        }
    }
    
    fun exportDietData() {
        viewModelScope.launch {
            try {
                // 实现饮食数据导出
                _message.value = "饮食数据导出功能开发中"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "导出失败"
            }
        }
    }
    
    fun exportBodyData() {
        viewModelScope.launch {
            try {
                // 实现身体数据导出
                _message.value = "身体数据导出功能开发中"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "导出失败"
            }
        }
    }
    
    fun exportAllData() {
        viewModelScope.launch {
            try {
                // 实现全部数据导出
                _message.value = "全部数据导出功能开发中"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "导出失败"
            }
        }
    }
    
    fun resetAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                // 实现数据重置
                repository.resetAllUserData()
                _message.value = "所有数据已重置"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "重置失败"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearCache() {
        viewModelScope.launch {
            try {
                // 实现缓存清理
                val context = getApplication<Application>()
                context.cacheDir.deleteRecursively()
                _message.value = "缓存已清除"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "清除失败"
            }
        }
    }
    
    // 用户信息更新
    fun updateUserName(name: String) {
        viewModelScope.launch {
            try {
                repository.updateUserName(name)
                _settings.value = _settings.value.copy(userName = name)
                _message.value = "姓名已更新"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "更新失败"
            }
        }
    }

    fun updateUserHeight(height: Float) {
        viewModelScope.launch {
            try {
                repository.updateUserHeight(height)
                _settings.value = _settings.value.copy(userHeight = height)
                _message.value = "身高已更新"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "更新失败"
            }
        }
    }

    fun updateUserGender(gender: String) {
        viewModelScope.launch {
            try {
                repository.updateUserGender(gender)
                _settings.value = _settings.value.copy(userGender = gender)
                _message.value = "性别已更新"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "更新失败"
            }
        }
    }

    fun updateUserAge(age: Int) {
        viewModelScope.launch {
            try {
                repository.updateUserAge(age)
                _settings.value = _settings.value.copy(userAge = age)
                _message.value = "年龄已更新"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "更新失败"
            }
        }
    }

    fun updateUserGoal(goal: String) {
        viewModelScope.launch {
            try {
                repository.updateUserGoal(goal)
                _settings.value = _settings.value.copy(userGoal = goal)
                _message.value = "健身目标已更新"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "更新失败"
            }
        }
    }

    fun updateUserAvatar(avatarPath: String) {
        viewModelScope.launch {
            try {
                repository.updateUserAvatar(avatarPath)
                _settings.value = _settings.value.copy(userAvatar = avatarPath)
                _message.value = "头像已更新"
            } catch (e: Exception) {
                e.printStackTrace()
                _message.value = "头像更新失败"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
