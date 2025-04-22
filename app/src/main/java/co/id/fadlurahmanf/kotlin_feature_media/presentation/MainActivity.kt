package co.id.fadlurahmanf.kotlin_feature_media.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import co.id.fadlurahmanf.kotlin_feature_media.R
import co.id.fadlurahmanf.kotlin_feature_media.data.FeatureModel
import co.id.fadlurahmanf.kotlin_feature_media.domain.ExampleMediaUseCaseImpl
import co.id.fadlurahmanf.kotlin_feature_media.presentation.adapter.ListExampleAdapter
import com.fadlurahmanfdev.media_grab.MediaGrab
import com.fadlurahmanfdev.media_grab.data.repositories.MediaGrabRepositoryImpl


class MainActivity : AppCompatActivity(), ListExampleAdapter.Callback {
    lateinit var viewModel: MainViewModel
    lateinit var mediaGrab: MediaGrab

    private val features: List<FeatureModel> = listOf<FeatureModel>(
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Single Pick Image Visual Media",
            desc = "Single Pick image via pick visual media",
            enum = "SINGLE_PICK_VISUAL_MEDIA"
        ),
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Multiple Pick Visual Media",
            desc = "Multiple Pick via pick visual media",
            enum = "MULTIPLE_PICK_VISUAL_MEDIA"
        ),
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Single Pick Content",
            desc = "Single Pick content",
            enum = "SINGLE_PICK_CONTENT"
        ),
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Pick Image Via Activity For Result",
            desc = "Pick image via activity for result",
            enum = "PICK_IMAGE_ACTIVITY_FOR_RESULT"
        ),
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "----------------",
            desc = "----------------------------------------",
            enum = "DIVIDER-PICK-IMAGE"
        ),
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Get List of Album",
            desc = "Get list of album",
            enum = "GET_LIST_OF_ALBUM"
        ),
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Activity List of Album",
            desc = "Activity list of album",
            enum = "ACTIVITY_LIST_OF_ALBUM"
        ),
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
            title = "Get list of all images",
            desc = "Get list of all images",
            enum = "GET_ALL_IMAGES"
        ),
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Grid of all Video",
            desc = "Get list of all video",
            enum = "GET_ALL_VIDEO"
        ),
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Start File Explorer",
            desc = "Start File Explorer",
            enum = "START_FILE_EXPLORER"
        ),
    )

    private var singlePickVisualMediaLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                val mediaItem = mediaGrab.getMediaItemModelFromUri(this, uri)
                Log.d(
                    this::class.java.simpleName,
                    "Example-MediaGrab-LOG %%% media item: $mediaItem"
                )
            }
        }

    private var multiplePickVisualMediaLauncher =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            uris?.forEach { uri ->
                val mediaItem = mediaGrab.getMediaItemModelFromUri(this, uri)
                Log.d(
                    this::class.java.simpleName,
                    "Example-MediaGrab-LOG %%% media item: $mediaItem"
                )
            }
        }

    private var singlePickContentLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val mediaItem = mediaGrab.getMediaItemModelFromUri(this, uri)
                Log.d(
                    this::class.java.simpleName,
                    "Example-MediaGrab-LOG %%% media item: $mediaItem"
                )
            }
        }

        private var pickMediaActivityForResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val itemCount = result.data?.clipData?.itemCount

                    if (itemCount != null) {
                        var currentItem = 0
                        while (currentItem < itemCount) {
                            val uri = result.data?.clipData?.getItemAt(currentItem)?.uri
                            val mediaItem = mediaGrab.getMediaItemModelFromUri(this, uri!!)
                            Log.d(
                                this::class.java.simpleName,
                                "Example-MediaGrab-LOG %%% media item: $mediaItem"
                            )
                            currentItem++
                        }
                    }
                }
            }

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

        mediaGrab = MediaGrab()

        viewModel = MainViewModel(
            exampleMediaUseCase = ExampleMediaUseCaseImpl(
                mediaRepository = MediaGrabRepositoryImpl()
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
            "SINGLE_PICK_IMAGE_VISUAL_MEDIA" -> {
                singlePickVisualMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
            }

            "MULTIPLE_PICK_IMAGE_VISUAL_MEDIA" -> {
                multiplePickVisualMediaLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageAndVideo
                    )
                )

                // if want to pick image only
                // multiplePickImageVisualMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                // if want to pick video only
                // multiplePickImageVisualMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
            }

            "SINGLE_PICK_CONTENT" -> {
                singlePickContentLauncher.launch("image/*")
            }

            "PICK_IMAGE_ACTIVITY_FOR_RESULT" -> {
                // pick image only intent
                val intent = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )

                // pick video only intent
//                 val intent = Intent(
//                     Intent.ACTION_PICK,
//                     android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//                 )

                // if want to allow multiple
                intent.apply {
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }

                pickMediaActivityForResultLauncher.launch(intent)
            }

            "ACTIVITY_LIST_OF_ALBUM" -> {
                val intent = Intent(this, ListAlbumActivity::class.java)
                startActivity(intent)
            }

            "GET_LIST_OF_ALBUM" -> {

            }

            "GET_IMAGE_ALBUMS" -> {
                val intent = Intent(this, ListImageAlbumActivity::class.java)
                startActivity(intent)
            }

            "GET_VIDEO_ALBUMS" -> {
                val intent = Intent(this, ListVideoAlbumActivity::class.java)
                startActivity(intent)
            }

            "GET_ALL_IMAGES" -> {
                val photos = mediaGrab.getPhotos(this)
                Log.d(this::class.java.simpleName, "Example-MediaGrab-LOG %%% total photos: ${photos.size}")
            }

            "GET_ALL_VIDEO" -> {
                val intent = Intent(this, ListVideoActivity::class.java)
                startActivity(intent)
            }

//            "PICK_IMAGE" -> {
//                val intent = Intent(
//                    Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                )
//                val uri =
//                    Uri.parse(Environment.getExternalStorageDirectory().path + File.separator);
//                intent.setDataAndType(uri, "image/*")
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                startActivityForResult(Intent.createChooser(intent, null), 900)
//            }
        }
    }
}