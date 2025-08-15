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
            tvAverageWeight.text = if (summary.averageWeight > 0) "${summary.averageWeight}斤" else "暂无数据"
            tvAverageBodyFat.text = if (summary.averageBodyFat > 0) "${summary.averageBodyFat}%" else "暂无数据"
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
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
