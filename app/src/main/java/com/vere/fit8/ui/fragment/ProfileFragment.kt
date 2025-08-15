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
            showUserInfoDialog()
        }

        binding.cardCoupons.setOnClickListener {
            showCouponsDialog()
        }

        binding.cardVip.setOnClickListener {
            showVipDialog()
        }

        binding.cardAddPhoto.setOnClickListener {
            showAddPhotoDialog()
        }

        binding.layoutAccount.setOnClickListener {
            showAccountDialog()
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
    
    private fun showUserInfoDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("用户信息")
            .setMessage("👤 昵称：Vere 🏃 健身小白\n🆔 ID：0081107\n📅 等级：Lv.1\n📅 加入时间：2024年8月")
            .setPositiveButton("编辑") { _, _ ->
                // 跳转到用户信息编辑页面
                Toast.makeText(requireContext(), "编辑用户信息功能开发中", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("关闭", null)
            .show()
    }

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

    private fun showAddPhotoDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("添加进步照片")
            .setMessage("📸 记录您的健身变化\n\n拍摄前后对比照，见证自己的蜕变！")
            .setPositiveButton("拍照") { _, _ ->
                Toast.makeText(requireContext(), "拍照功能开发中", Toast.LENGTH_SHORT).show()
            }
            .setNeutralButton("从相册选择") { _, _ ->
                Toast.makeText(requireContext(), "相册选择功能开发中", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }

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
