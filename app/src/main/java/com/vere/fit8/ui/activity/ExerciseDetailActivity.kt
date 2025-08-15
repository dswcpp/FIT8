package com.vere.fit8.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vere.fit8.data.model.ExerciseDetail
import com.vere.fit8.databinding.ActivityExerciseDetailBinding
import com.vere.fit8.ui.viewmodel.ExerciseDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 单个动作详情页面
 */
@AndroidEntryPoint
class ExerciseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseDetailBinding
    private val viewModel: ExerciseDetailViewModel by viewModels()
    
    private var exerciseId: String? = null
    private var exerciseData: ExerciseDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentData()
        setupToolbar()
        observeViewModel()
        loadExerciseDetail()
    }

    private fun getIntentData() {
        exerciseId = intent.getStringExtra("exercise_id")
        exerciseData = intent.getSerializableExtra("exercise_data") as? ExerciseDetail
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

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.selectedExercise.collect { exercise ->
                exercise?.let { updateUI(it) }
            }
        }
    }

    private fun loadExerciseDetail() {
        when {
            exerciseData != null -> {
                updateUI(exerciseData!!)
            }
            exerciseId != null -> {
                viewModel.getExerciseById(exerciseId!!)
            }
            else -> {
                finish() // 没有有效数据，关闭页面
            }
        }
    }

    private fun updateUI(exercise: ExerciseDetail) {
        binding.apply {
            // 设置标题
            toolbar.title = exercise.name

            // 基本信息
            tvExerciseName.text = exercise.name
            tvCategoryTag.text = exercise.category
            tvDifficultyTag.text = exercise.difficulty
            tvDescription.text = exercise.description

            // 快速信息
            tvCalories.text = exercise.caloriesBurnedPerMinute.toString()
            tvSets.text = exercise.recommendedSets.ifEmpty { "3-4" }
            tvReps.text = exercise.recommendedReps.ifEmpty { "8-12" }
            tvRestTime.text = exercise.restTime.ifEmpty { "60s" }

            // TODO: 设置TabLayout和ViewPager内容
            setupTabContent(exercise)
        }
    }
    
    private fun setupTabContent(exercise: ExerciseDetail) {
        // 这里可以设置TabLayout和ViewPager的内容
        // 包括：动作步骤、常见错误、专业建议、动作变式等
        // 暂时简化实现
    }

    companion object {
        const val EXTRA_EXERCISE_ID = "exercise_id"
        const val EXTRA_EXERCISE_DATA = "exercise_data"
    }
}
