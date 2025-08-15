package com.vere.fit8.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vere.fit8.R
import com.vere.fit8.databinding.FragmentProfileBinding
import com.vere.fit8.ui.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 个人中心Fragment
 * 显示用户信息、设置和成就
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ProfileViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
        observeViewModel()
        loadUserData()
    }
    
    private fun setupUI() {
        // 设置点击事件
        binding.cardUserInfo.setOnClickListener {
            startActivity(Intent(requireContext(), com.vere.fit8.ui.activity.UserProfileActivity::class.java))
        }

        binding.cardCoupons.setOnClickListener {
            showCouponsDialog()
        }

        binding.cardVip.setOnClickListener {
            showVipDialog()
        }

        binding.cardAddPhoto.setOnClickListener {
            startActivity(Intent(requireContext(), com.vere.fit8.ui.activity.ProgressPhotoActivity::class.java))
        }

        binding.layoutAccount.setOnClickListener {
            showAccountDialog()
        }

        binding.layoutSystemSettings.setOnClickListener {
            startActivity(Intent(requireContext(), com.vere.fit8.ui.activity.SettingsActivity::class.java))
        }

        binding.layoutContact.setOnClickListener {
            showContactDialog()
        }
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userStats.collect { stats ->
                updateUserStats(stats)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.achievements.collect { achievements ->
                updateAchievements(achievements)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.appSettings.collect { settings ->
                updateUserInfo(settings)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.couponCount.collect { count ->
                binding.tvCouponCount.text = count.toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.photoCount.collect { count ->
                binding.tvPhotoCount.text = count.toString()
            }
        }
    }
    
    private fun loadUserData() {
        viewModel.loadUserData()
    }
    
    private fun updateUserStats(stats: com.vere.fit8.data.model.UserStats?) {
        stats?.let {
            binding.tvTotalDays.text = "${it.totalDays}"
            binding.tvTotalMinutes.text = "${it.totalMinutes}分钟"
            binding.tvBadges.text = "${it.badges}"
        }
    }
    
    private fun updateAchievements(achievements: List<com.vere.fit8.data.model.Achievement>) {
        // 成就相关的UI更新可以在这里实现
        // 目前新设计中没有专门的成就显示区域
    }

    private fun updateUserInfo(settings: com.vere.fit8.data.model.AppSettings?) {
        settings?.let {
            // 更新用户名
            binding.tvUserName.text = it.userName

            // 更新用户信息
            val userInfo = buildString {
                append("身高: ${it.userHeight.toInt()}cm")
                append(" | ")
                append("年龄: ${it.userAge}岁")
                append(" | ")
                append("目标: ${it.userGoal}")
            }
            binding.tvUserInfo.text = userInfo

            // 更新头像
            loadUserAvatar(it.userAvatar)
        }
    }

    private fun loadUserAvatar(avatarPath: String?) {
        if (!avatarPath.isNullOrEmpty()) {
            Glide.with(this)
                .load(avatarPath)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(binding.ivUserAvatar)
        } else {
            // 使用默认头像
            binding.ivUserAvatar.setImageResource(R.drawable.ic_profile)
        }
    }

    // 用户信息现在直接跳转到编辑页面

    private fun showCouponsDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("我的优惠券")
            .setMessage("🎫 当前拥有 0 张优惠券\n\n暂无可用优惠券，完成训练任务可获得优惠券奖励！")
            .setPositiveButton("去训练", null)
            .setNegativeButton("关闭", null)
            .show()
    }

    private fun showVipDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("燃力VIP")
            .setMessage("🔥 开通VIP享受以下特权：\n\n• 专属训练计划\n• 营养师指导\n• 无广告体验\n• 数据云同步\n• 优先客服支持")
            .setPositiveButton("立即订阅") { _, _ ->
                Toast.makeText(requireContext(), "VIP订阅功能开发中", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showOrdersDialog(type: String) {
        val title = when(type) {
            "pending" -> "待支付订单"
            "paid" -> "已支付订单"
            "completed" -> "已完成订单"
            "cancelled" -> "已取消订单"
            else -> "我的订单"
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage("暂无${title.replace("订单", "")}订单")
            .setPositiveButton("确定", null)
            .show()
    }

    // 进步照片现在直接跳转到专门页面

    private fun showBusinessDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("商务合作")
            .setMessage("🤝 欢迎与燃力8周合作\n\n如果您有商务合作意向，请联系我们的商务团队。")
            .setPositiveButton("联系商务") { _, _ ->
                Toast.makeText(requireContext(), "商务合作功能开发中", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showMerchantDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("入驻商训")
            .setMessage("🏪 成为燃力8周合作商家\n\n为用户提供专业的健身服务和产品。")
            .setPositiveButton("了解详情") { _, _ ->
                Toast.makeText(requireContext(), "商家入驻功能开发中", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showAccountDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("账号管理")
            .setMessage("⚙️ 管理您的账号设置\n\n• 修改密码\n• 绑定手机号\n• 账号安全设置\n• 注销账号")
            .setPositiveButton("进入设置") { _, _ ->
                Toast.makeText(requireContext(), "账号管理功能开发中", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showContactDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("联系我们")
            .setMessage("📞 客服热线：400-888-8888\n📧 邮箱：support@fit8.com\n🕐 服务时间：9:00-18:00\n\n我们随时为您提供帮助！")
            .setPositiveButton("拨打电话") { _, _ ->
                Toast.makeText(requireContext(), "拨打电话功能开发中", Toast.LENGTH_SHORT).show()
            }
            .setNeutralButton("发送邮件") { _, _ ->
                Toast.makeText(requireContext(), "发送邮件功能开发中", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("关闭", null)
            .show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
