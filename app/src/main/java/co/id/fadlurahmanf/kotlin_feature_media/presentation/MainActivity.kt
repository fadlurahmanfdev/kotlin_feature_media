package co.id.fadlurahmanf.kotlin_feature_media.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import co.id.fadlurahmanf.kotlin_feature_media.R
import co.id.fadlurahmanf.kotlin_feature_media.data.FeatureModel
import co.id.fadlurahmanf.kotlin_feature_media.domain.ExampleMediaUseCaseImpl
import co.id.fadlurahmanf.kotlin_feature_media.presentation.adapter.ListExampleAdapter
import com.github.fadlurahmanfdev.kotlin_feature_media.data.repositories.MediaRepositoryImpl

class MainActivity : AppCompatActivity(), ListExampleAdapter.Callback {
    lateinit var viewModel: MainViewModel

    private val features: List<FeatureModel> = listOf<FeatureModel>(
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Get List Image Album",
            desc = "Get list of all image album",
            enum = "GET_IMAGE_ALBUMS"
        ),
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Get List Video Album",
            desc = "Get list of all video album",
            enum = "GET_VIDEO_ALBUMS"
        ),
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Grid of all Images",
            desc = "Get list of all images",
            enum = "GET_ALL_IMAGE"
        ),
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Grid of all Video",
            desc = "Get list of all video",
            enum = "GET_ALL_VIDEO"
        ),
    )

    private lateinit var rv: RecyclerView

    private lateinit var adapter: ListExampleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        rv = findViewById<RecyclerView>(R.id.rv)

        viewModel = MainViewModel(
            exampleMediaUseCase = ExampleMediaUseCaseImpl(
                mediaRepository = MediaRepositoryImpl()
            )
        )

        rv.setItemViewCacheSize(features.size)
        rv.setHasFixedSize(true)

        adapter = ListExampleAdapter()
        adapter.setCallback(this)
        adapter.setList(features)
        adapter.setHasStableIds(true)
        rv.adapter = adapter
    }

    override fun onClicked(item: FeatureModel) {
        when (item.enum) {
            "GET_IMAGE_ALBUMS" -> {
                val intent = Intent(this, ListImageAlbumActivity::class.java)
                startActivity(intent)
            }

            "GET_VIDEO_ALBUMS" -> {
                val intent = Intent(this, ListVideoAlbumActivity::class.java)
                startActivity(intent)
            }

            "GET_ALL_IMAGE" -> {
                val intent = Intent(this, ListImageActivity::class.java)
                startActivity(intent)
            }

            "GET_ALL_VIDEO" -> {
                val intent = Intent(this, ListVideoActivity::class.java)
                startActivity(intent)
            }
        }
    }
}