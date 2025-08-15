package com.vere.fit8.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.vere.fit8.R
import com.vere.fit8.databinding.ActivityExerciseDetailListBinding
import com.vere.fit8.ui.adapter.ExerciseDetailListAdapter
import com.vere.fit8.ui.viewmodel.ExerciseDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 动作详情列表页面
 */
@AndroidEntryPoint
class ExerciseDetailListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseDetailListBinding
    private val viewModel: ExerciseDetailViewModel by viewModels()
    private lateinit var adapter: ExerciseDetailListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityExerciseDetailListBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupToolbar()
            setupRecyclerView()
            setupSearch()
            setupFilters()
            observeViewModel()
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = ExerciseDetailListAdapter { exercise ->
            // 跳转到动作详情页面
            val intent = Intent(this, ExerciseDetailActivity::class.java).apply {
                putExtra("exercise_id", exercise.id)
                putExtra("exercise_data", exercise)
            }
            startActivity(intent)
        }

        binding.recyclerViewExercises.apply {
            layoutManager = LinearLayoutManager(this@ExerciseDetailListActivity)
            adapter = this@ExerciseDetailListActivity.adapter
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString() ?: ""
                viewModel.searchExercises(query)
            }
        })
    }

    private fun setupFilters() {
        // 设置分类筛选点击事件
        binding.chipAll.setOnClickListener {
            selectFilter(binding.chipAll, "全部")
        }

        binding.chipChest.setOnClickListener {
            selectFilter(binding.chipChest, "胸部")
        }

        binding.chipLegs.setOnClickListener {
            selectFilter(binding.chipLegs, "腿部")
        }

        binding.chipCore.setOnClickListener {
            selectFilter(binding.chipCore, "核心")
        }

        // 默认选中"全部"
        selectFilter(binding.chipAll, "全部")
    }

    private fun selectFilter(selectedView: android.widget.TextView, category: String) {
        // 重置所有筛选按钮样式
        listOf(binding.chipAll, binding.chipChest, binding.chipLegs, binding.chipCore).forEach { chip ->
            chip.setBackgroundColor(resources.getColor(com.vere.fit8.R.color.surface_variant, null))
            chip.setTextColor(resources.getColor(com.vere.fit8.R.color.on_surface_variant, null))
        }

        // 设置选中样式
        selectedView.setBackgroundColor(resources.getColor(com.vere.fit8.R.color.primary, null))
        selectedView.setTextColor(resources.getColor(com.vere.fit8.R.color.white, null))

        // 应用筛选
        viewModel.filterByCategory(category)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.filteredExercises.collect { exercises ->
                adapter.submitList(exercises)
                updateEmptyState(exercises.isEmpty())
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                updateLoadingState(isLoading)
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.layoutEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerViewExercises.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun updateLoadingState(isLoading: Boolean) {
        binding.layoutLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.recyclerViewExercises.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.layoutEmptyState.visibility = View.GONE
    }

    companion object {
        const val EXTRA_CATEGORY = "extra_category"
        
        fun createIntent(context: android.content.Context, category: String? = null): Intent {
            return Intent(context, ExerciseDetailListActivity::class.java).apply {
                category?.let { putExtra(EXTRA_CATEGORY, it) }
            }
        }
    }
}
