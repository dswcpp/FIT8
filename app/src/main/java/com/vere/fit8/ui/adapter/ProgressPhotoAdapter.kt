package com.vere.fit8.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.vere.fit8.R
import com.vere.fit8.data.model.ProgressPhoto
import com.vere.fit8.databinding.ItemProgressPhotoBinding
import java.time.format.DateTimeFormatter

/**
 * 进步照片适配器
 * 遵循扁平化设计，极简风格
 */
class ProgressPhotoAdapter(
    private val onPhotoClick: (ProgressPhoto) -> Unit
) : ListAdapter<ProgressPhoto, ProgressPhotoAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProgressPhotoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemProgressPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: ProgressPhoto) {
            binding.apply {
                // 加载照片 - 使用圆角但不过度，符合扁平化设计
                Glide.with(itemView.context)
                    .load(Uri.parse(photo.filePath))
                    .placeholder(R.drawable.ic_photo_placeholder)
                    .error(R.drawable.ic_photo_error)
                    .transform(RoundedCorners(8)) // 微妙的圆角
                    .into(imagePhoto)

                // 显示拍摄日期 - 简洁的日期格式
                val dateFormatter = DateTimeFormatter.ofPattern("MM/dd")
                textDate.text = photo.takenDate.format(dateFormatter)

                // 显示体重信息（如果有）
                if (photo.weight != null) {
                    textWeight.text = "${photo.weight}斤"
                    textWeight.visibility = android.view.View.VISIBLE
                } else {
                    textWeight.visibility = android.view.View.GONE
                }

                // 显示体脂率信息（如果有）
                if (photo.bodyFat != null) {
                    textBodyFat.text = "${photo.bodyFat}%"
                    textBodyFat.visibility = android.view.View.VISIBLE
                } else {
                    textBodyFat.visibility = android.view.View.GONE
                }

                // 显示备注（如果有）
                if (photo.notes.isNotEmpty()) {
                    textNotes.text = photo.notes
                    textNotes.visibility = android.view.View.VISIBLE
                } else {
                    textNotes.visibility = android.view.View.GONE
                }

                // 点击事件
                root.setOnClickListener {
                    onPhotoClick(photo)
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ProgressPhoto>() {
        override fun areItemsTheSame(oldItem: ProgressPhoto, newItem: ProgressPhoto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProgressPhoto, newItem: ProgressPhoto): Boolean {
            return oldItem == newItem
        }
    }
}
