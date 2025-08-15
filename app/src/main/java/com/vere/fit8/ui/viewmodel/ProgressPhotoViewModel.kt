package com.vere.fit8.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vere.fit8.data.model.ProgressPhoto
import com.vere.fit8.data.repository.Fit8Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 进步照片ViewModel
 * 管理照片数据和状态
 */
@HiltViewModel
class ProgressPhotoViewModel @Inject constructor(
    private val repository: Fit8Repository
) : ViewModel() {
    
    private val _photos = MutableStateFlow<List<ProgressPhoto>>(emptyList())
    val photos: StateFlow<List<ProgressPhoto>> = _photos.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _selectedPhoto = MutableStateFlow<ProgressPhoto?>(null)
    val selectedPhoto: StateFlow<ProgressPhoto?> = _selectedPhoto.asStateFlow()
    
    fun loadPhotos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val photos = repository.getAllProgressPhotos()
                _photos.value = photos
            } catch (e: Exception) {
                // 处理错误
                _photos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun addPhoto(uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 创建新的进步照片记录
                val newPhoto = ProgressPhoto(
                    id = System.currentTimeMillis(),
                    filePath = uri.toString(),
                    takenDate = LocalDateTime.now(),
                    weight = null, // 可以从最近的体重记录获取
                    bodyFat = null, // 可以从最近的体脂记录获取
                    notes = ""
                )
                
                // 保存到数据库
                repository.insertProgressPhoto(newPhoto)

                // 重新加载数据
                loadPhotos()
                
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deletePhoto(photoId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteProgressPhoto(photoId)
                loadPhotos() // 重新加载数据
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updatePhotoInfo(photoId: Long, weight: Float?, bodyFat: Float?, notes: String) {
        viewModelScope.launch {
            try {
                weight?.let { repository.updateProgressPhotoWeight(photoId, it) }
                bodyFat?.let { repository.updateProgressPhotoBodyFat(photoId, it) }
                repository.updateProgressPhotoNotes(photoId, notes)
                loadPhotos() // 重新加载数据
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun selectPhoto(photo: ProgressPhoto) {
        _selectedPhoto.value = photo
    }

    fun clearSelection() {
        _selectedPhoto.value = null
    }
    
    fun deletePhoto(photo: ProgressPhoto) {
        viewModelScope.launch {
            try {
                // 从数据库删除
                // repository.deleteProgressPhoto(photo.id)
                
                // 更新UI列表
                val currentPhotos = _photos.value.toMutableList()
                currentPhotos.remove(photo)
                _photos.value = currentPhotos
                
            } catch (e: Exception) {
                // 处理错误
            }
        }
    }
    
    fun viewPhotoDetail(photo: ProgressPhoto) {
        _selectedPhoto.value = photo
    }
    
    fun updatePhotoNotes(photo: ProgressPhoto, notes: String) {
        viewModelScope.launch {
            try {
                val updatedPhoto = photo.copy(notes = notes)
                
                // 更新数据库
                // repository.updateProgressPhoto(updatedPhoto)
                
                // 更新UI列表
                val currentPhotos = _photos.value.toMutableList()
                val index = currentPhotos.indexOfFirst { it.id == photo.id }
                if (index >= 0) {
                    currentPhotos[index] = updatedPhoto
                    _photos.value = currentPhotos
                }
                
            } catch (e: Exception) {
                // 处理错误
            }
        }
    }
    
    fun getPhotosByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<ProgressPhoto> {
        return _photos.value.filter { photo ->
            photo.takenDate.isAfter(startDate) && photo.takenDate.isBefore(endDate)
        }
    }
    
    fun getLatestPhoto(): ProgressPhoto? {
        return _photos.value.maxByOrNull { it.takenDate }
    }
    
    fun getPhotoCount(): Int {
        return _photos.value.size
    }
}
