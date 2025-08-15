package com.vere.fit8.ui.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vere.fit8.databinding.ActivityWeightInputBinding
import com.vere.fit8.ui.viewmodel.WeightViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class WeightInputActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityWeightInputBinding
    private val viewModel: WeightViewModel by viewModels()
    private var selectedDate = LocalDate.now()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeightInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        observeViewModel()
        loadCurrentWeight()
    }
    
    private fun setupUI() {
        // 设置工具栏
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // 设置日期选择
        updateDateDisplay()
        binding.layoutDate.setOnClickListener {
            showDatePicker()
        }
        
        // 设置保存按钮
        binding.btnSave.setOnClickListener {
            saveWeight()
        }
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.currentWeight.collect { weight ->
                binding.tvCurrentWeight.text = if (weight > 0) {
                    String.format("%.1f", weight)
                } else {
                    "--"
                }
            }
        }

        lifecycleScope.launch {
            viewModel.currentBodyFat.collect { bodyFat ->
                binding.tvCurrentBodyFat.text = if (bodyFat > 0) {
                    String.format("%.1f", bodyFat)
                } else {
                    "--"
                }
            }
        }

        lifecycleScope.launch {
            viewModel.saveResult.collect { success ->
                if (success) {
                    Toast.makeText(this@WeightInputActivity, "身体数据保存成功", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@WeightInputActivity, "保存失败，请重试", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun loadCurrentWeight() {
        viewModel.loadCurrentData()
    }
    
    private fun updateDateDisplay() {
        val formatter = DateTimeFormatter.ofPattern("MM月dd日 EEEE", Locale.CHINESE)
        val dateText = if (selectedDate == LocalDate.now()) {
            "今天 (${selectedDate.format(formatter)})"
        } else {
            selectedDate.format(formatter)
        }
        binding.tvSelectedDate.text = dateText
    }
    
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.set(selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth)
        
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                updateDateDisplay()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            // 限制日期范围
            datePicker.maxDate = System.currentTimeMillis()
            show()
        }
    }
    
    private fun saveWeight() {
        val weightText = binding.etWeight.text.toString().trim()
        val bodyFatText = binding.etBodyFat.text.toString().trim()

        // 验证体重
        if (weightText.isEmpty()) {
            binding.tilWeight.error = "请输入体重"
            return
        }

        val weight = weightText.toDoubleOrNull()
        if (weight == null || weight <= 0 || weight > 500) {
            binding.tilWeight.error = "请输入有效的体重值 (1-500斤)"
            return
        }
        binding.tilWeight.error = null

        // 验证体脂率（可选）
        var bodyFat: Double? = null
        if (bodyFatText.isNotEmpty()) {
            bodyFat = bodyFatText.toDoubleOrNull()
            if (bodyFat == null || bodyFat < 3 || bodyFat > 50) {
                binding.tilBodyFat.error = "请输入有效的体脂率 (3-50%)"
                return
            }
        }
        binding.tilBodyFat.error = null

        val notes = binding.etNotes.text.toString().trim()
        viewModel.saveBodyData(weight, bodyFat, selectedDate, notes)
    }
}
