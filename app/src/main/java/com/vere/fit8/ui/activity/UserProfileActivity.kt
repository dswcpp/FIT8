package com.vere.fit8.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vere.fit8.R
import com.vere.fit8.databinding.ActivityUserProfileBinding
import com.vere.fit8.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 用户信息编辑页面
 */
@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private val viewModel: SettingsViewModel by viewModels()

    private var currentAvatarUri: Uri? = null

    // 相机权限请求
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "需要相机权限才能拍照", Toast.LENGTH_SHORT).show()
        }
    }

    // 存储权限请求
    private val storagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openGallery()
        } else {
            Toast.makeText(this, "需要存储权限才能选择照片", Toast.LENGTH_SHORT).show()
        }
    }

    // 拍照结果
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentAvatarUri != null) {
            updateAvatar(currentAvatarUri!!)
        }
    }

    // 选择照片结果
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { updateAvatar(it) }
    }
    
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, UserProfileActivity::class.java)
            context.startActivity(intent)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupUI()
        observeViewModel()
        loadUserProfile()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "个人信息"
        }
        
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun setupUI() {
        // 头像更换
        binding.layoutAvatar.setOnClickListener {
            showAvatarDialog()
        }

        // 姓名编辑
        binding.layoutName.setOnClickListener {
            showNameEditDialog()
        }
        
        // 身高编辑
        binding.layoutHeight.setOnClickListener {
            showHeightEditDialog()
        }
        
        // 性别选择
        binding.layoutGender.setOnClickListener {
            showGenderDialog()
        }
        
        // 年龄编辑
        binding.layoutAge.setOnClickListener {
            showAgeEditDialog()
        }
        
        // 目标选择
        binding.layoutGoal.setOnClickListener {
            showGoalDialog()
        }
        
        // 保存按钮
        binding.btnSave.setOnClickListener {
            saveUserProfile()
        }
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.settings.collect { settings ->
                updateUI(settings)
            }
        }
        
        lifecycleScope.launch {
            viewModel.message.collect { message ->
                message?.let {
                    Toast.makeText(this@UserProfileActivity, it, Toast.LENGTH_SHORT).show()
                    viewModel.clearMessage()
                }
            }
        }
    }
    
    private fun loadUserProfile() {
        viewModel.loadSettings()
    }
    
    private fun updateUI(settings: com.vere.fit8.data.model.AppSettings) {
        binding.tvName.text = settings.userName
        binding.tvHeight.text = "${settings.userHeight.toInt()}cm"
        binding.tvGender.text = if (settings.userGender == "male") "男" else "女"
        binding.tvAge.text = "${settings.userAge}岁"
        binding.tvGoal.text = settings.userGoal

        // 加载头像
        loadAvatar(settings.userAvatar)
    }

    private fun loadAvatar(avatarPath: String?) {
        if (!avatarPath.isNullOrEmpty()) {
            Glide.with(this)
                .load(avatarPath)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(binding.ivAvatar)
        } else {
            // 使用默认头像
            binding.ivAvatar.setImageResource(R.drawable.ic_profile)
        }
    }
    
    private fun showNameEditDialog() {
        val editText = android.widget.EditText(this)
        editText.setText(binding.tvName.text)
        editText.hint = "请输入姓名"
        
        MaterialAlertDialogBuilder(this)
            .setTitle("编辑姓名")
            .setView(editText)
            .setPositiveButton("确定") { _, _ ->
                val name = editText.text.toString().trim()
                if (name.isNotEmpty()) {
                    viewModel.updateUserName(name)
                } else {
                    Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun showHeightEditDialog() {
        val editText = android.widget.EditText(this)
        editText.setText(binding.tvHeight.text.toString().replace("cm", ""))
        editText.hint = "请输入身高(cm)"
        editText.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        
        MaterialAlertDialogBuilder(this)
            .setTitle("编辑身高")
            .setView(editText)
            .setPositiveButton("确定") { _, _ ->
                val heightStr = editText.text.toString().trim()
                try {
                    val height = heightStr.toFloat()
                    if (height in 100f..250f) {
                        viewModel.updateUserHeight(height)
                    } else {
                        Toast.makeText(this, "请输入合理的身高值(100-250cm)", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun showGenderDialog() {
        val genders = arrayOf("男", "女")
        val currentGender = if (binding.tvGender.text == "男") 0 else 1
        
        MaterialAlertDialogBuilder(this)
            .setTitle("选择性别")
            .setSingleChoiceItems(genders, currentGender) { dialog, which ->
                val gender = if (which == 0) "male" else "female"
                viewModel.updateUserGender(gender)
                dialog.dismiss()
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun showAgeEditDialog() {
        val editText = android.widget.EditText(this)
        editText.setText(binding.tvAge.text.toString().replace("岁", ""))
        editText.hint = "请输入年龄"
        editText.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        
        MaterialAlertDialogBuilder(this)
            .setTitle("编辑年龄")
            .setView(editText)
            .setPositiveButton("确定") { _, _ ->
                val ageStr = editText.text.toString().trim()
                try {
                    val age = ageStr.toInt()
                    if (age in 10..100) {
                        viewModel.updateUserAge(age)
                    } else {
                        Toast.makeText(this, "请输入合理的年龄(10-100岁)", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun showGoalDialog() {
        val goals = arrayOf("减脂塑形", "增肌", "保持健康", "提高体能")
        val currentGoal = goals.indexOf(binding.tvGoal.text.toString())
        
        MaterialAlertDialogBuilder(this)
            .setTitle("选择健身目标")
            .setSingleChoiceItems(goals, currentGoal) { dialog, which ->
                viewModel.updateUserGoal(goals[which])
                dialog.dismiss()
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun saveUserProfile() {
        Toast.makeText(this, "个人信息已保存", Toast.LENGTH_SHORT).show()
        finish()
    }

    // 头像相关方法
    private fun showAvatarDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("更换头像")
            .setItems(arrayOf("拍照", "从相册选择")) { _, which ->
                when (which) {
                    0 -> checkCameraPermissionAndTakePhoto()
                    1 -> checkStoragePermissionAndSelectPhoto()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun checkCameraPermissionAndTakePhoto() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun checkStoragePermissionAndSelectPhoto() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            else -> {
                storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openCamera() {
        val photoFile = createImageFile()
        currentAvatarUri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            photoFile
        )
        takePictureLauncher.launch(currentAvatarUri)
    }

    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "AVATAR_${timeStamp}_"
        val storageDir = getExternalFilesDir("avatars")
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private fun updateAvatar(uri: Uri) {
        // 更新UI显示
        Glide.with(this)
            .load(uri)
            .transform(CircleCrop())
            .into(binding.ivAvatar)

        // 保存头像路径到数据库
        viewModel.updateUserAvatar(uri.toString())

        Toast.makeText(this, "头像已更新", Toast.LENGTH_SHORT).show()
    }
}
