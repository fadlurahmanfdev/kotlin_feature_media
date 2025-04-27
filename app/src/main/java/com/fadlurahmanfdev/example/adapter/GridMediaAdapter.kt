package com.fadlurahmanfdev.example.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanfdev.example.R
import com.bumptech.glide.Glide
import com.fadlurahmanfdev.pixmed.data.model.PixMedItem
import java.io.File

class GridMediaAdapter : RecyclerView.Adapter<GridMediaAdapter.ViewHolder>() {
    private val images = arrayListOf<PixMedItem>()

    fun setImages(list: List<PixMedItem>) {
        if (images.isNotEmpty()) {
            val oldSize = images.size
            images.clear()
            notifyItemRangeRemoved(0, oldSize)
        }
        images.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.image)
        val videoIcon = view.findViewById<ImageView>(R.id.videoIcon)

        fun setItemView(item: PixMedItem) {
            Glide.with(image).load(Uri.fromFile(File(item.path))).into(image)

//            if (item.type == MediaItemType.VIDEO) {
//                videoIcon.visibility = View.VISIBLE
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]

        holder.setItemView(image)
    }
}