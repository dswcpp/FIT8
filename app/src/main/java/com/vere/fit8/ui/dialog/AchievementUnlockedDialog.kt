package com.vere.fit8.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vere.fit8.R
import com.vere.fit8.data.model.Achievement
import com.vere.fit8.databinding.DialogAchievementUnlockedBinding

/**
 * 成就解锁对话框
 * 显示新解锁的成就信息
 */
class AchievementUnlockedDialog : DialogFragment() {
    
    private var _binding: DialogAchievementUnlockedBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var achievement: Achievement
    
    companion object {
        private const val ARG_ACHIEVEMENT = "achievement"
        
        fun newInstance(achievement: Achievement): AchievementUnlockedDialog {
            val dialog = AchievementUnlockedDialog()
            val args = Bundle().apply {
                putSerializable(ARG_ACHIEVEMENT, achievement)
            }
            dialog.arguments = args
            return dialog
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        achievement = arguments?.getSerializable(ARG_ACHIEVEMENT) as Achievement
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAchievementUnlockedBinding.inflate(layoutInflater)
        
        setupUI()
        
        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .setCancelable(true)
            .create()
    }
    
    private fun setupUI() {
        binding.apply {
            // 设置成就信息
            tvAchievementTitle.text = achievement.title
            tvAchievementDescription.text = achievement.description
            tvAchievementPoints.text = "+${achievement.points}分"
            
            // 设置成就图标
            val iconRes = getAchievementIcon(achievement.iconRes)
            ivAchievementIcon.setImageResource(iconRes)
            
            // 设置按钮点击事件
            btnClose.setOnClickListener {
                dismiss()
            }
            
            btnShare.setOnClickListener {
                shareAchievement()
            }
        }
    }
    
    private fun getAchievementIcon(iconName: String): Int {
        return when (iconName) {
            "ic_first_checkin" -> R.drawable.ic_achievement
            "ic_checkin_streak" -> R.drawable.ic_achievement
            "ic_week_master" -> R.drawable.ic_achievement
            "ic_month_champion" -> R.drawable.ic_achievement
            "ic_first_workout" -> R.drawable.ic_training
            "ic_training_master" -> R.drawable.ic_training
            "ic_training_expert" -> R.drawable.ic_training
            "ic_week_complete" -> R.drawable.ic_achievement
            "ic_half_hero" -> R.drawable.ic_achievement
            "ic_champion" -> R.drawable.ic_achievement
            "ic_first_record" -> R.drawable.ic_data
            "ic_weight_loss" -> R.drawable.ic_achievement
            "ic_weight_master" -> R.drawable.ic_achievement
            "ic_body_fat_killer" -> R.drawable.ic_achievement
            "ic_diet_recorder" -> R.drawable.ic_diet
            "ic_diet_manager" -> R.drawable.ic_diet
            "ic_early_bird" -> R.drawable.ic_achievement
            "ic_night_owl" -> R.drawable.ic_achievement
            "ic_perfectionist" -> R.drawable.ic_achievement
            "ic_calorie_killer" -> R.drawable.ic_achievement
            else -> R.drawable.ic_achievement
        }
    }
    
    private fun shareAchievement() {
        // 实现分享功能
        val shareText = "我在燃力8周APP中解锁了新成就：${achievement.title}！${achievement.description}"
        
        val shareIntent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
        }
        
        startActivity(android.content.Intent.createChooser(shareIntent, "分享成就"))
        dismiss()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
