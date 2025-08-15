package com.vere.fit8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vere.fit8.data.model.ExerciseDetail
import com.vere.fit8.data.provider.ExerciseDetailProvider
import com.vere.fit8.data.repository.Fit8Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 动作详情ViewModel
 */
@HiltViewModel
class ExerciseDetailViewModel @Inject constructor(
    private val repository: Fit8Repository,
    private val exerciseDetailProvider: ExerciseDetailProvider
) : ViewModel() {

    private val _exercises = MutableStateFlow<List<ExerciseDetail>>(emptyList())
    val exercises: StateFlow<List<ExerciseDetail>> = _exercises.asStateFlow()

    private val _filteredExercises = MutableStateFlow<List<ExerciseDetail>>(emptyList())
    val filteredExercises: StateFlow<List<ExerciseDetail>> = _filteredExercises.asStateFlow()

    private val _selectedExercise = MutableStateFlow<ExerciseDetail?>(null)
    val selectedExercise: StateFlow<ExerciseDetail?> = _selectedExercise.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("全部")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    init {
        loadExercises()
    }

    fun loadExercises() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val exerciseList = repository.getAllExerciseDetails()
                if (exerciseList.isEmpty()) {
                    // 如果数据库为空，初始化默认数据
                    initializeDefaultExercises()
                } else {
                    _exercises.value = exerciseList
                    applyFilters()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // 如果发生异常，也尝试初始化默认数据
                initializeDefaultExercises()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchExercises(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun filterByCategory(category: String) {
        _selectedCategory.value = category
        applyFilters()
    }

    fun selectExercise(exercise: ExerciseDetail) {
        _selectedExercise.value = exercise
    }

    fun getExerciseById(id: String) {
        viewModelScope.launch {
            try {
                val exercise = repository.getExerciseDetailById(id)
                _selectedExercise.value = exercise
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun applyFilters() {
        val query = _searchQuery.value.lowercase()
        val category = _selectedCategory.value
        
        val filtered = _exercises.value.filter { exercise ->
            val matchesSearch = if (query.isBlank()) {
                true
            } else {
                exercise.name.lowercase().contains(query) ||
                exercise.primaryMuscles.any { it.lowercase().contains(query) } ||
                exercise.equipment.any { it.lowercase().contains(query) }
            }
            
            val matchesCategory = if (category == "全部") {
                true
            } else {
                exercise.category == category
            }
            
            matchesSearch && matchesCategory
        }
        
        _filteredExercises.value = filtered
    }

    private suspend fun initializeDefaultExercises() {
        val defaultExercises = exerciseDetailProvider.getAllExerciseDetails()
        repository.insertExerciseDetails(defaultExercises)
        _exercises.value = defaultExercises
        applyFilters()
    }

}
