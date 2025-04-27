package com.fadlurahmanfdev.example.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanfdev.example.MainViewModel
import com.fadlurahmanfdev.example.R
import com.fadlurahmanfdev.example.domain.ExampleMediaUseCaseImpl
import com.fadlurahmanfdev.example.presentation.adapter.AlbumAdapter
import com.fadlurahmanfdev.pixmed.PixMed
import com.fadlurahmanfdev.pixmed.data.model.PixMedBucket

class ListAlbumActivity : AppCompatActivity(), AlbumAdapter.Callback {
    lateinit var viewModel: MainViewModel

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: AlbumAdapter

    private val albums: ArrayList<PixMedBucket> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_album)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById<RecyclerView>(R.id.rv)

        viewModel = MainViewModel(
            exampleMediaUseCase = ExampleMediaUseCaseImpl(
                mediaRepository = PixMed()
            )
        )

        adapter = AlbumAdapter()
        adapter.setCallback(this)
        adapter.setAlbums(albums)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fetchedAlbums = viewModel.getAlbums(this)
        albums.clear()
        albums.addAll(fetchedAlbums)
        adapter.setAlbums(albums)
    }

    override fun onClicked(album: PixMedBucket) {
//        val intent = Intent(this, ListVideoActivity::class.java)
//        intent.apply {
//            putExtra("ALBUM_ID", album.id)
//        }
//        startActivity(intent)
    }
}