package co.id.fadlurahmanf.kotlin_feature_media.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.id.fadlurahmanf.kotlin_feature_media.R
import com.bumptech.glide.Glide
import com.github.fadlurahmanfdev.kotlin_feature_media.data.enum.MediaItemType
import com.github.fadlurahmanfdev.kotlin_feature_media.data.model.MediaAlbumModel
import java.io.File

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    val albums: ArrayList<MediaAlbumModel> = arrayListOf()
    private lateinit var callback: Callback

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun setAlbums(list: List<MediaAlbumModel>) {
        if (albums.isNotEmpty()) {
            val oldSize = albums.size
            albums.clear()
            notifyItemRangeRemoved(0, oldSize)
        }
        albums.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val main: LinearLayout = view.findViewById(R.id.llMain)
        val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        val albumName: TextView = view.findViewById(R.id.albumName)
        val videoIcon: ImageView = view.findViewById(R.id.videoIcon)
        val albumItemCount: TextView = view.findViewById(R.id.itemCount)

        init {
            main.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = albums[position]
                    callback.onClicked(clickedItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = albums.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albums[position]

        Glide.with(holder.thumbnail).load(Uri.fromFile(File(album.thumbnailPath))).into(holder.thumbnail)
        holder.albumName.text = album.name
        holder.albumItemCount.text = "${album.itemCount}"

        if (album.thumbnailPathType == MediaItemType.VIDEO) {
            holder.videoIcon.visibility = View.VISIBLE
        }else{
            holder.videoIcon.visibility = View.GONE
        }


    }

    interface Callback {
        fun onClicked(album: MediaAlbumModel)
    }
}