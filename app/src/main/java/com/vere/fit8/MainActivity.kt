package com.vere.fit8

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.vere.fit8.data.repository.Fit8Repository
import com.vere.fit8.databinding.ActivityMainBinding
import com.vere.fit8.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 燃力8周 - 主Activity
 * 管理底部导航和Fragment容器
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var repository: Fit8Repository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 设置状态栏样式
        setupStatusBar()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置系统栏内边距
        setupSystemBars()

        setupNavigation()
        observeViewModel()
        initializeData()

        // 延迟处理Intent，确保UI完全初始化
        binding.root.post {
            handleIntent()
        }
    }
    
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        // 强制设置图标颜色
        val unselectedColor = androidx.core.content.ContextCompat.getColor(this, R.color.nav_unselected)
        val selectedColor = androidx.core.content.ContextCompat.getColor(this, R.color.primary)

        val states = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf()
        )
        val colors = intArrayOf(selectedColor, unselectedColor)
        val colorStateList = android.content.res.ColorStateList(states, colors)

        binding.bottomNavigation.itemIconTintList = colorStateList

        // 隐藏文字标签但保持图标可见
        hideBottomNavigationLabels()

        android.util.Log.d("MainActivity", "BottomNav setup completed")
    }
    
    private fun observeViewModel() {
        // 观察ViewModel数据变化
        // 可以在这里处理全局状态变化
    }

    /**
     * 切换到指定的标签页
     * @param tabIndex 标签页索引 (0=首页, 1=训练, 2=饮食, 3=数据, 4=个人)
     */
    fun switchToTab(tabIndex: Int) {
        when (tabIndex) {
            0 -> binding.bottomNavigation.selectedItemId = R.id.nav_home
            1 -> binding.bottomNavigation.selectedItemId = R.id.nav_training
            2 -> binding.bottomNavigation.selectedItemId = R.id.nav_diet
            3 -> binding.bottomNavigation.selectedItemId = R.id.nav_data
            4 -> binding.bottomNavigation.selectedItemId = R.id.nav_profile
        }
    }

    private fun setupStatusBar() {
        // 设置状态栏透明
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        // 设置状态栏文字为深色（适用于浅色背景）
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun hideBottomNavigationLabels() {
        // 通过反射或直接设置文字颜色为透明来隐藏标签
        try {
            val bottomNavigationMenuView = binding.bottomNavigation.getChildAt(0) as? com.google.android.material.bottomnavigation.BottomNavigationMenuView
            bottomNavigationMenuView?.let { menuView ->
                for (i in 0 until menuView.childCount) {
                    val item = menuView.getChildAt(i) as? com.google.android.material.bottomnavigation.BottomNavigationItemView
                    item?.let { itemView ->
                        // 隐藏文字标签
                        try {
                            val smallLabelId = resources.getIdentifier("smallLabel", "id", "com.google.android.material")
                            val largeLabelId = resources.getIdentifier("largeLabel", "id", "com.google.android.material")
                            val smallLabel = itemView.findViewById<android.widget.TextView>(smallLabelId)
                            val largeLabel = itemView.findViewById<android.widget.TextView>(largeLabelId)
                            smallLabel?.visibility = android.view.View.GONE
                            largeLabel?.visibility = android.view.View.GONE
                        } catch (e: Exception) {
                            android.util.Log.e("MainActivity", "Error finding label views: ${e.message}")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "Error hiding labels: ${e.message}")
        }
    }

    private fun setupSystemBars() {
        // 设置系统栏内边距
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // 为Fragment容器设置顶部内边距，避免与状态栏重叠
            binding.navHostFragment.setPadding(0, systemBars.top, 0, 0)

            // 为底部导航设置底部内边距，让它无缝衔接到底部
            // 只添加系统导航栏的高度作为内边距
            binding.bottomNavigation.setPadding(
                0,
                resources.getDimensionPixelSize(R.dimen.spacing_sm),
                0,
                systemBars.bottom
            )

            insets
        }
    }

    private fun initializeData() {
        lifecycleScope.launch {
            try {
                repository.initializeDefaultData()
            } catch (e: Exception) {
                e.printStackTrace()
                // 如果初始化失败，可以在这里处理错误
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent()
    }

    private fun handleIntent() {
        try {
            // 处理从其他Activity传递的参数
            val switchToTab = intent.getIntExtra("switch_to_tab", -1)
            if (switchToTab != -1) {
                switchToTab(switchToTab)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // 如果处理Intent失败，不影响应用正常运行
        }
    }
}
