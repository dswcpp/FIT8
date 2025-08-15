package com.vere.fit8.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vere.fit8.databinding.ActivityWaterInputBinding
import com.vere.fit8.ui.adapter.WaterRecordAdapter
import com.vere.fit8.ui.viewmodel.WaterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WaterInputActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityWaterInputBinding
    private val viewModel: WaterViewModel by viewModels()
    private lateinit var waterRecordAdapter: WaterRecordAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaterInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        setupRecyclerView()
        observeViewModel()
        loadTodayData()
    }
    
    private fun setupUI() {
        // 设置工具栏
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // 快速添加按钮
        binding.btnAdd250.setOnClickListener { addWater(250) }
        binding.btnAdd500.setOnClickListener { addWater(500) }
        binding.btnAdd1000.setOnClickListener { addWater(1000) }
        
        // 自定义添加按钮
        binding.btnAddCustom.setOnClickListener {
            addCustomWater()
        }
    }
    
    private fun setupRecyclerView() {
        waterRecordAdapter = WaterRecordAdapter { record ->
            viewModel.deleteWaterRecord(record.id)
        }
        
        binding.rvWaterRecords.apply {
            layoutManager = LinearLayoutManager(this@WaterInputActivity)
            adapter = waterRecordAdapter
        }
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.todayWaterAmount.collect { amount ->
                binding.tvCurrentWater.text = amount.toString()
                binding.progressWater.progress = amount
                
                // 更新进度条颜色
                val progress = (amount.toFloat() / 2000f * 100).toInt()
                if (progress >= 100) {
                    Toast.makeText(this@WaterInputActivity, "🎉 恭喜！今日饮水目标已达成", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        lifecycleScope.launch {
            viewModel.todayRecords.collect { records ->
                waterRecordAdapter.submitList(records)
            }
        }
        
        lifecycleScope.launch {
            viewModel.addResult.collect { success ->
                if (success) {
                    binding.etWater.text?.clear()
                    Toast.makeText(this@WaterInputActivity, "饮水记录添加成功", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@WaterInputActivity, "添加失败，请重试", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun loadTodayData() {
        viewModel.loadTodayData()
    }
    
    private fun addWater(amount: Int) {
        viewModel.addWaterRecord(amount)
    }
    
    private fun addCustomWater() {
        val amountText = binding.etWater.text.toString().trim()
        if (amountText.isEmpty()) {
            binding.tilWater.error = "请输入饮水量"
            return
        }
        
        val amount = amountText.toIntOrNull()
        if (amount == null || amount <= 0 || amount > 5000) {
            binding.tilWater.error = "请输入有效的饮水量 (1-5000ml)"
            return
        }
        
        binding.tilWater.error = null
        viewModel.addWaterRecord(amount)
    }
}
