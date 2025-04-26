package com.fadlurahmanfdev.example.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanfdev.example.MainViewModel
import com.fadlurahmanfdev.example.R
import com.fadlurahmanfdev.example.domain.ExampleMediaUseCaseImpl
import com.fadlurahmanfdev.example.presentation.adapter.GridMediaAdapter
import com.fadlurahmanfdev.media_grab.MediaGrab
import com.fadlurahmanfdev.media_grab.data.model.MediaGrabItemModel

class ListImageActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var adapter: GridMediaAdapter
    private var images = arrayListOf<MediaGrabItemModel>()

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val albumId = intent.getLongExtra("ALBUM_ID", -1)

        recyclerView = findViewById<RecyclerView>(R.id.rv)

        viewModel = MainViewModel(
            exampleMediaUseCase = ExampleMediaUseCaseImpl(
                mediaRepository = MediaGrab()
            )
        )

        adapter = GridMediaAdapter()
        adapter.setImages(images)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 4)

        val fetchedImages =
            viewModel.getPhotos(this, albumId = if (albumId == -1L) null else albumId)
        images.clear()
        images.addAll(fetchedImages)
        adapter.setImages(fetchedImages)
    }
}