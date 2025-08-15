package com.vere.fit8.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.vere.fit8.R
import com.vere.fit8.databinding.ActivitySettingsBinding
import com.vere.fit8.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 设置页面Activity
 * 包含应用的各种设置选项
 */
@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()
    
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupUI()
        observeViewModel()
        loadSettings()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "设置"
        }
        
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun setupUI() {
        // 通知设置
        binding.switchTrainingReminder.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateTrainingReminderEnabled(isChecked)
        }
        
        binding.switchWaterReminder.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateWaterReminderEnabled(isChecked)
        }
        
        binding.switchSleepReminder.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateSleepReminderEnabled(isChecked)
        }
        
        // 时间设置
        binding.layoutTrainingTime.setOnClickListener {
            showTrainingTimePicker()
        }
        
        binding.layoutSleepTime.setOnClickListener {
            showSleepTimePicker()
        }
        
        // 数据设置
        binding.layoutDataSync.setOnClickListener {
            showDataSyncDialog()
        }
        
        binding.layoutDataExport.setOnClickListener {
            showDataExportDialog()
        }
        
        binding.layoutDataReset.setOnClickListener {
            showDataResetDialog()
        }
        
        // 应用设置
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateDarkModeEnabled(isChecked)
        }
        
        binding.layoutLanguage.setOnClickListener {
            showLanguageDialog()
        }
        
        binding.layoutClearCache.setOnClickListener {
            showClearCacheDialog()
        }
        
        // 关于
        binding.layoutAbout.setOnClickListener {
            showAboutDialog()
        }
        
        binding.layoutPrivacy.setOnClickListener {
            showPrivacyDialog()
        }
        
        binding.layoutFeedback.setOnClickListener {
            showFeedbackDialog()
        }
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.settings.collect { settings ->
                updateUI(settings)
            }
        }
    }
    
    private fun loadSettings() {
        viewModel.loadSettings()
    }
    
    private fun updateUI(settings: com.vere.fit8.data.model.AppSettings) {
        // 更新通知开关
        binding.switchTrainingReminder.isChecked = settings.trainingReminderEnabled
        binding.switchWaterReminder.isChecked = settings.waterReminderEnabled
        binding.switchSleepReminder.isChecked = settings.sleepReminderEnabled
        
        // 更新时间显示
        binding.tvTrainingTime.text = settings.trainingReminderTime
        binding.tvSleepTime.text = settings.sleepReminderTime
        
        // 更新应用设置
        binding.switchDarkMode.isChecked = settings.darkModeEnabled
        binding.tvLanguage.text = if (settings.language == "zh") "中文" else "English"
    }
    
    private fun showTrainingTimePicker() {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(18)
            .setMinute(0)
            .setTitleText("选择训练提醒时间")
            .build()
        
        picker.addOnPositiveButtonClickListener {
            val time = String.format("%02d:%02d", picker.hour, picker.minute)
            viewModel.updateTrainingReminderTime(time)
        }
        
        picker.show(supportFragmentManager, "training_time_picker")
    }
    
    private fun showSleepTimePicker() {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(22)
            .setMinute(0)
            .setTitleText("选择睡眠提醒时间")
            .build()
        
        picker.addOnPositiveButtonClickListener {
            val time = String.format("%02d:%02d", picker.hour, picker.minute)
            viewModel.updateSleepReminderTime(time)
        }
        
        picker.show(supportFragmentManager, "sleep_time_picker")
    }
    
    private fun showDataSyncDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("数据同步")
            .setMessage("是否立即同步数据到云端？")
            .setPositiveButton("同步") { _, _ ->
                viewModel.syncData()
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun showDataExportDialog() {
        val options = arrayOf("导出训练记录", "导出饮食记录", "导出身体数据", "导出全部数据")
        
        MaterialAlertDialogBuilder(this)
            .setTitle("数据导出")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> viewModel.exportTrainingData()
                    1 -> viewModel.exportDietData()
                    2 -> viewModel.exportBodyData()
                    3 -> viewModel.exportAllData()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun showDataResetDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("重置数据")
            .setMessage("⚠️ 警告：此操作将删除所有训练和饮食记录，且无法恢复！\n\n确定要继续吗？")
            .setPositiveButton("确定重置") { _, _ ->
                showFinalConfirmDialog()
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun showFinalConfirmDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("最终确认")
            .setMessage("请再次确认：您真的要删除所有数据吗？")
            .setPositiveButton("确定删除") { _, _ ->
                viewModel.resetAllData()
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun showLanguageDialog() {
        val languages = arrayOf("中文", "English")
        val currentLanguage = if (binding.tvLanguage.text == "中文") 0 else 1
        
        MaterialAlertDialogBuilder(this)
            .setTitle("选择语言")
            .setSingleChoiceItems(languages, currentLanguage) { dialog, which ->
                val language = if (which == 0) "zh" else "en"
                viewModel.updateLanguage(language)
                dialog.dismiss()
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun showClearCacheDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("清除缓存")
            .setMessage("确定要清除应用缓存吗？这不会删除您的训练数据。")
            .setPositiveButton("确定") { _, _ ->
                viewModel.clearCache()
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun showAboutDialog() {
        val message = buildString {
            append("🔥 燃力8周 v1.0.0\n\n")
            append("💪 专业的8周健身训练计划\n")
            append("📊 科学的数据记录与分析\n")
            append("🏆 丰富的成就激励系统\n\n")
            append("📧 联系我们：support@fit8.com\n")
            append("🌐 官网：www.fit8.com\n\n")
            append("© 2024 燃力8周团队")
        }
        
        MaterialAlertDialogBuilder(this)
            .setTitle("关于燃力8周")
            .setMessage(message)
            .setPositiveButton("好的", null)
            .show()
    }
    
    private fun showPrivacyDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("隐私政策")
            .setMessage("我们重视您的隐私，所有数据仅用于改善您的健身体验。详细隐私政策请访问官网查看。")
            .setPositiveButton("了解", null)
            .show()
    }
    
    private fun showFeedbackDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("意见反馈")
            .setMessage("感谢您使用燃力8周！如有任何建议或问题，请发送邮件至：feedback@fit8.com")
            .setPositiveButton("好的", null)
            .show()
    }
}
