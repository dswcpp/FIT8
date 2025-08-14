package com.vere.fit8.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vere.fit8.data.model.ExerciseTemplate
import com.vere.fit8.data.model.TrainingExercise
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Room数据库类型转换器
 * 用于复杂数据类型的序列化和反序列化
 */
class Converters {
    
    private val gson = Gson()
    
    // LocalDate转换
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
    
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) }
    }
    
    // LocalDateTime转换
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
    
    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    }
    
    // TrainingExercise列表转换
    @TypeConverter
    fun fromTrainingExerciseList(exercises: List<TrainingExercise>?): String {
        return gson.toJson(exercises ?: emptyList<TrainingExercise>())
    }
    
    @TypeConverter
    fun toTrainingExerciseList(exercisesString: String): List<TrainingExercise> {
        val listType = object : TypeToken<List<TrainingExercise>>() {}.type
        return gson.fromJson(exercisesString, listType) ?: emptyList()
    }
    
    // ExerciseTemplate列表转换
    @TypeConverter
    fun fromExerciseTemplateList(exercises: List<ExerciseTemplate>?): String {
        return gson.toJson(exercises ?: emptyList<ExerciseTemplate>())
    }
    
    @TypeConverter
    fun toExerciseTemplateList(exercisesString: String): List<ExerciseTemplate> {
        val listType = object : TypeToken<List<ExerciseTemplate>>() {}.type
        return gson.fromJson(exercisesString, listType) ?: emptyList()
    }
    
    // String列表转换
    @TypeConverter
    fun fromStringList(strings: List<String>?): String {
        return gson.toJson(strings ?: emptyList<String>())
    }
    
    @TypeConverter
    fun toStringList(stringsString: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(stringsString, listType) ?: emptyList()
    }
}
