package co.id.fadlurahmanf.kotlin_feature_media.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.id.fadlurahmanf.kotlin_feature_media.R
import co.id.fadlurahmanf.kotlin_feature_media.domain.ExampleMediaUseCaseImpl
import co.id.fadlurahmanf.kotlin_feature_media.presentation.adapter.AlbumAdapter
import com.github.fadlurahmanfdev.kotlin_feature_media.data.model.MediaAlbumModel
import com.github.fadlurahmanfdev.kotlin_feature_media.data.repositories.MediaRepositoryImpl

class ListAlbumActivity : AppCompatActivity(), AlbumAdapter.Callback {
    lateinit var viewModel: MainViewModel

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: AlbumAdapter

    private val albums: ArrayList<MediaAlbumModel> = arrayListOf()

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
                mediaRepository = MediaRepositoryImpl()
            )
        )

        adapter = AlbumAdapter()
        adapter.setCallback(this)
        adapter.setAlbums(albums)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fetchedAlbums = viewModel.getPhotoAlbums(this)
        albums.clear()
        albums.addAll(fetchedAlbums)
        adapter.setAlbums(albums)
    }

    override fun onClicked(album: MediaAlbumModel) {
        val intent = Intent(this, ListVideoActivity::class.java)
        intent.apply {
            putExtra("ALBUM_ID", album.id)
        }
        startActivity(intent)
    }
}