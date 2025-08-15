package com.vere.fit8.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.vere.fit8.R
import com.vere.fit8.databinding.FragmentDataBinding
import com.vere.fit8.ui.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

/**
 * 数据统计Fragment
 * 显示体重、体脂趋势图和训练统计
 */
@AndroidEntryPoint
class DataFragment : Fragment() {
    
    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: DataViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
        setupCharts()
        observeViewModel()
        loadData()
    }
    
    private fun setupUI() {
        // 设置时间范围选择
        binding.chipWeek.setOnClickListener {
            viewModel.setTimeRange(DataViewModel.TimeRange.WEEK)
        }
        
        binding.chipMonth.setOnClickListener {
            viewModel.setTimeRange(DataViewModel.TimeRange.MONTH)
        }
        
        binding.chip3Months.setOnClickListener {
            viewModel.setTimeRange(DataViewModel.TimeRange.THREE_MONTHS)
        }
        
        binding.chipAll.setOnClickListener {
            viewModel.setTimeRange(DataViewModel.TimeRange.ALL)
        }
        
        // 设置数据类型切换
        binding.tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showWeightChart()
                    1 -> showBodyFatChart()
                    2 -> showTrainingStats()
                }
            }
            
            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })
        
        // 导出数据按钮
        binding.btnExportData.setOnClickListener {
            viewModel.exportData()
        }

        // 导出图片按钮
        binding.btnExportImage.setOnClickListener {
            viewModel.exportDataAsImage()
        }
    }
    
    private fun setupCharts() {
        // 设置体重趋势图
        setupLineChart(binding.chartWeight, "体重 (斤)")
        
        // 设置体脂趋势图
        setupLineChart(binding.chartBodyFat, "体脂率 (%)")
        
        // 设置训练统计图
        setupBarChart()
    }
    
    private fun setupLineChart(chart: com.github.mikephil.charting.charts.LineChart, label: String) {
        chart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            setDrawGridBackground(false)

            // X轴设置
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textColor = Color.GRAY
                labelRotationAngle = -45f // 旋转标签以避免重叠
                setLabelCount(6, false) // 限制标签数量
            }

            // Y轴设置
            axisLeft.apply {
                setDrawGridLines(true)
                textColor = Color.GRAY
                gridColor = Color.LTGRAY
            }

            axisRight.isEnabled = false

            // 图例设置
            legend.apply {
                isEnabled = true
                textColor = Color.GRAY
            }

            // 设置视口
            setVisibleXRangeMaximum(30f) // 最多显示30天
        }
    }
    
    private fun setupBarChart() {
        binding.chartTraining.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(false)
            setDrawGridBackground(false)

            // X轴设置
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textColor = Color.GRAY
                labelRotationAngle = -45f // 旋转标签以避免重叠
                setLabelCount(6, false) // 限制标签数量
            }

            // Y轴设置
            axisLeft.apply {
                setDrawGridLines(true)
                textColor = Color.GRAY
                gridColor = Color.LTGRAY
                axisMinimum = 0f
                axisMaximum = 120f // 训练时间最大120分钟
            }

            axisRight.isEnabled = false

            // 图例设置
            legend.apply {
                isEnabled = true
                textColor = Color.GRAY
            }

            // 设置视口
            setVisibleXRangeMaximum(30f) // 最多显示30天
        }
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.weightData.collect { data ->
                updateWeightChart(data)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bodyFatData.collect { data ->
                updateBodyFatChart(data)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.trainingStats.collect { stats ->
                updateTrainingStats(stats)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.summaryStats.collect { summary ->
                updateSummaryStats(summary)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exportEvent.collect { event ->
                event?.let { handleExportEvent(it) }
            }
        }
    }
    
    private fun loadData() {
        viewModel.loadData()
    }
    
    private fun updateWeightChart(data: List<com.vere.fit8.ui.viewmodel.ChartDataPoint>) {
        if (data.isEmpty()) {
            binding.chartWeight.clear()
            return
        }

        // 只显示有数据的点，但保持X轴索引连续
        val entries = data.mapIndexedNotNull { index, point ->
            if (point.hasData && point.value > 0) {
                Entry(index.toFloat(), point.value)
            } else null
        }

        if (entries.isEmpty()) {
            binding.chartWeight.clear()
            return
        }

        val dataSet = LineDataSet(entries, "体重 (斤)").apply {
            color = requireContext().getColor(R.color.primary)
            setCircleColor(requireContext().getColor(R.color.primary))
            lineWidth = 3f
            circleRadius = 5f
            setDrawCircleHole(false)
            valueTextSize = 9f
            setDrawFilled(true)
            fillColor = requireContext().getColor(R.color.primary_alpha_20)
            mode = LineDataSet.Mode.CUBIC_BEZIER // 平滑曲线
        }

        val lineData = LineData(dataSet)
        binding.chartWeight.data = lineData

        // 设置X轴标签，只显示非空标签
        val labels = data.map { if (it.label.isNotEmpty()) it.label else " " }
        binding.chartWeight.xAxis.valueFormatter = IndexAxisValueFormatter(labels)

        binding.chartWeight.invalidate()
    }
    
    private fun updateBodyFatChart(data: List<com.vere.fit8.ui.viewmodel.ChartDataPoint>) {
        if (data.isEmpty()) {
            binding.chartBodyFat.clear()
            return
        }

        // 只显示有数据的点，但保持X轴索引连续
        val entries = data.mapIndexedNotNull { index, point ->
            if (point.hasData && point.value > 0) {
                Entry(index.toFloat(), point.value)
            } else null
        }

        if (entries.isEmpty()) {
            binding.chartBodyFat.clear()
            return
        }

        val dataSet = LineDataSet(entries, "体脂率 (%)").apply {
            color = requireContext().getColor(R.color.secondary)
            setCircleColor(requireContext().getColor(R.color.secondary))
            lineWidth = 3f
            circleRadius = 5f
            setDrawCircleHole(false)
            valueTextSize = 9f
            setDrawFilled(true)
            fillColor = requireContext().getColor(R.color.secondary_alpha_20)
            mode = LineDataSet.Mode.CUBIC_BEZIER // 平滑曲线
        }

        val lineData = LineData(dataSet)
        binding.chartBodyFat.data = lineData

        // 设置X轴标签，只显示非空标签
        val labels = data.map { if (it.label.isNotEmpty()) it.label else " " }
        binding.chartBodyFat.xAxis.valueFormatter = IndexAxisValueFormatter(labels)

        binding.chartBodyFat.invalidate()
    }
    
    private fun updateTrainingStats(stats: List<com.vere.fit8.ui.viewmodel.TrainingStatItem>) {
        if (stats.isEmpty()) {
            binding.chartTraining.clear()
            return
        }

        // 显示训练时长而不是完成率
        val entries = stats.mapIndexed { index, stat ->
            BarEntry(index.toFloat(), stat.trainingMinutes.toFloat())
        }

        val dataSet = BarDataSet(entries, "训练时长 (分钟)").apply {
            color = requireContext().getColor(R.color.warning)
            valueTextSize = 8f
            setDrawValues(false) // 不显示数值，避免拥挤
        }

        val barData = BarData(dataSet)
        barData.barWidth = 0.8f // 设置柱状图宽度
        binding.chartTraining.data = barData

        // 设置X轴标签，只显示非空标签
        val labels = stats.map { if (it.label.isNotEmpty()) it.label else " " }
        binding.chartTraining.xAxis.valueFormatter = IndexAxisValueFormatter(labels)

        binding.chartTraining.invalidate()
    }
    
    private fun updateSummaryStats(summary: com.vere.fit8.ui.viewmodel.SummaryStats) {
        binding.apply {
            tvTotalWorkouts.text = "${summary.totalWorkouts}次"
            tvTotalDays.text = "${summary.totalDays}天"
            tvConsecutiveDays.text = "${summary.consecutiveDays}天"
            tvTotalCalories.text = "${summary.totalCalories}kcal"
            tvCurrentWeight.text = if (summary.currentWeight > 0) "${summary.currentWeight}斤" else "暂无数据"
            tvCurrentBodyFat.text = if (summary.currentBodyFat > 0) "${summary.currentBodyFat}%" else "暂无数据"
            tvTodayWaterIntake.text = "${summary.todayWaterIntake}ml"
        }
    }
    
    private fun showWeightChart() {
        binding.chartWeight.visibility = View.VISIBLE
        binding.chartBodyFat.visibility = View.GONE
        binding.chartTraining.visibility = View.GONE
    }
    
    private fun showBodyFatChart() {
        binding.chartWeight.visibility = View.GONE
        binding.chartBodyFat.visibility = View.VISIBLE
        binding.chartTraining.visibility = View.GONE
    }
    
    private fun showTrainingStats() {
        binding.chartWeight.visibility = View.GONE
        binding.chartBodyFat.visibility = View.GONE
        binding.chartTraining.visibility = View.VISIBLE
    }

    private fun handleExportEvent(event: com.vere.fit8.ui.viewmodel.ExportEvent) {
        when (event) {
            is com.vere.fit8.ui.viewmodel.ExportEvent.Success -> {
                shareCSVData(event.csvContent)
            }
            is com.vere.fit8.ui.viewmodel.ExportEvent.ImageSuccess -> {
                generateAndShareImage(event.summaryStats, event.recentRecords)
            }
            is com.vere.fit8.ui.viewmodel.ExportEvent.Error -> {
                android.widget.Toast.makeText(requireContext(), event.message, android.widget.Toast.LENGTH_LONG).show()
            }
        }
        viewModel.clearExportEvent()
    }

    private fun shareCSVData(csvContent: String) {
        try {
            // 创建临时文件
            val fileName = "Fit8_数据导出_${java.time.LocalDate.now()}.csv"
            val file = java.io.File(requireContext().cacheDir, fileName)
            file.writeText(csvContent, Charsets.UTF_8)

            // 创建分享Intent
            val uri = androidx.core.content.FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                file
            )

            val shareIntent = android.content.Intent().apply {
                action = android.content.Intent.ACTION_SEND
                type = "text/csv"
                putExtra(android.content.Intent.EXTRA_STREAM, uri)
                putExtra(android.content.Intent.EXTRA_SUBJECT, "Fit8 数据导出")
                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(android.content.Intent.createChooser(shareIntent, "导出数据"))

        } catch (e: Exception) {
            e.printStackTrace()
            android.widget.Toast.makeText(requireContext(), "导出失败：${e.message}", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    private fun generateAndShareImage(
        summaryStats: com.vere.fit8.ui.viewmodel.SummaryStats,
        recentRecords: List<com.vere.fit8.data.model.DailyRecord>
    ) {
        try {
            // 生成图片
            val imageGenerator = com.vere.fit8.utils.DataImageGenerator(requireContext())
            val bitmap = imageGenerator.generateDataSummaryImage(summaryStats, recentRecords)

            // 保存图片到临时文件
            val fileName = "Fit8_数据报告_${java.time.LocalDate.now()}.jpg"
            val file = java.io.File(requireContext().cacheDir, fileName)

            val outputStream = java.io.FileOutputStream(file)
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()

            // 创建分享Intent
            val uri = androidx.core.content.FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                file
            )

            val shareIntent = android.content.Intent().apply {
                action = android.content.Intent.ACTION_SEND
                type = "image/jpeg"
                putExtra(android.content.Intent.EXTRA_STREAM, uri)
                putExtra(android.content.Intent.EXTRA_SUBJECT, "Fit8 健身数据报告")
                putExtra(android.content.Intent.EXTRA_TEXT, "我的健身数据报告，由Fit8生成")
                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(android.content.Intent.createChooser(shareIntent, "分享数据报告"))

            android.widget.Toast.makeText(requireContext(), "数据报告图片已生成", android.widget.Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            android.widget.Toast.makeText(requireContext(), "图片生成失败：${e.message}", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
