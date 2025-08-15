package com.vere.fit8.ui.activity

import android.Manifest
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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vere.fit8.R
import com.vere.fit8.data.model.ProgressPhoto
import com.vere.fit8.databinding.ActivityProgressPhotoBinding
import com.vere.fit8.ui.adapter.ProgressPhotoAdapter
import com.vere.fit8.ui.viewmodel.ProgressPhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 进步照片管理页面
 * 遵循扁平化设计风格，极简布局，大量留白
 */
@AndroidEntryPoint
class ProgressPhotoActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityProgressPhotoBinding
    private val viewModel: ProgressPhotoViewModel by viewModels()
    private lateinit var photoAdapter: ProgressPhotoAdapter
    
    private var currentPhotoUri: Uri? = null
    
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
        if (success && currentPhotoUri != null) {
            viewModel.addPhoto(currentPhotoUri!!)
        }
    }
    
    // 选择照片结果
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.addPhoto(it) }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        setupRecyclerView()
        observeViewModel()
        loadPhotos()
    }
    
    private fun setupUI() {
        // 设置Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // 拍照按钮 - 扁平化设计
        binding.btnTakePhoto.setOnClickListener {
            checkCameraPermissionAndTakePhoto()
        }
        
        // 选择照片按钮 - 扁平化设计
        binding.btnSelectPhoto.setOnClickListener {
            checkStoragePermissionAndSelectPhoto()
        }
    }
    
    private fun setupRecyclerView() {
        photoAdapter = ProgressPhotoAdapter { photo ->
            // 点击照片显示编辑对话框
            showEditPhotoDialog(photo)
        }
        
        binding.recyclerViewPhotos.apply {
            layoutManager = GridLayoutManager(this@ProgressPhotoActivity, 2)
            adapter = photoAdapter
            // 移除默认动画，保持极简
            itemAnimator = null
        }
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.photos.collect { photos ->
                photoAdapter.submitList(photos)
                updateEmptyState(photos.isEmpty())
            }
        }
        
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                // 简单的加载状态，无复杂动画
                binding.progressBar.visibility = if (isLoading) 
                    android.view.View.VISIBLE else android.view.View.GONE
            }
        }
    }
    
    private fun loadPhotos() {
        viewModel.loadPhotos()
    }
    
    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.layoutEmpty.visibility = android.view.View.VISIBLE
            binding.recyclerViewPhotos.visibility = android.view.View.GONE
        } else {
            binding.layoutEmpty.visibility = android.view.View.GONE
            binding.recyclerViewPhotos.visibility = android.view.View.VISIBLE
        }
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
        currentPhotoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            photoFile
        )
        takePictureLauncher.launch(currentPhotoUri)
    }
    
    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }
    
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "PROGRESS_${timeStamp}_"
        val storageDir = getExternalFilesDir("progress_photos")
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private fun showEditPhotoDialog(photo: ProgressPhoto) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_photo, null)

        val etWeight = dialogView.findViewById<android.widget.EditText>(R.id.et_weight)
        val etBodyFat = dialogView.findViewById<android.widget.EditText>(R.id.et_body_fat)
        val etNotes = dialogView.findViewById<android.widget.EditText>(R.id.et_notes)

        // 填充当前数据
        photo.weight?.let { etWeight.setText(it.toString()) }
        photo.bodyFat?.let { etBodyFat.setText(it.toString()) }
        etNotes.setText(photo.notes)

        MaterialAlertDialogBuilder(this)
            .setTitle("编辑照片信息")
            .setView(dialogView)
            .setPositiveButton("保存") { _, _ ->
                val weight = etWeight.text.toString().toFloatOrNull()
                val bodyFat = etBodyFat.text.toString().toFloatOrNull()
                val notes = etNotes.text.toString()

                viewModel.updatePhotoInfo(photo.id, weight, bodyFat, notes)
            }
            .setNegativeButton("取消", null)
            .setNeutralButton("删除") { _, _ ->
                MaterialAlertDialogBuilder(this)
                    .setTitle("确认删除")
                    .setMessage("确定要删除这张照片吗？")
                    .setPositiveButton("删除") { _, _ ->
                        viewModel.deletePhoto(photo.id)
                    }
                    .setNegativeButton("取消", null)
                    .show()
            }
            .show()
    }
}
