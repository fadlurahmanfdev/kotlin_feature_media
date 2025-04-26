package com.fadlurahmanfdev.example.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanfdev.example.MainViewModel
import com.fadlurahmanfdev.example.R
import com.fadlurahmanfdev.example.data.FeatureModel
import com.fadlurahmanfdev.example.domain.ExampleMediaUseCaseImpl
import com.fadlurahmanfdev.example.presentation.adapter.ListExampleAdapter
import com.fadlurahmanfdev.media_grab.MediaGrab


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
            title = "Multiple Pick Content",
            desc = "Multiple Pick content",
            enum = "MULTIPLE_PICK_CONTENT"
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
            title = "Get list of all videos",
            desc = "Get list of all videos",
            enum = "GET_ALL_VIDEOS"
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

    private var multiplePickContentLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            uris?.let {
             uris.forEach { uri ->
                 uri.let {
                     val mediaItem = mediaGrab.getMediaItemModelFromUri(this, uri)
                     Log.d(
                         this::class.java.simpleName,
                         "Example-MediaGrab-LOG %%% media item: $mediaItem"
                     )
                 }
             }
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
                mediaRepository = MediaGrab()
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
            "SINGLE_PICK_VISUAL_MEDIA" -> {
                singlePickVisualMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
            }

            "MULTIPLE_PICK_VISUAL_MEDIA" -> {
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
                // Image Only
                singlePickContentLauncher.launch("image/*")

                // Video Only
//                singlePickContentLauncher.launch("video/*")

                // All + Include File
//                singlePickContentLauncher.launch("*/*")
            }

            "MULTIPLE_PICK_CONTENT" -> {
                // Image Only
//                multiplePickContentLauncher.launch("image/*")

                // Video Only
//                multiplePickContentLauncher.launch("video/*")

                // All + Include File
                multiplePickContentLauncher.launch("*/*")
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

            "GET_LIST_OF_ALBUM" -> {
                Log.d(this::class.java.simpleName, "Example-MediaGrab-LOG %%% PHOTO ALBUMS")
                val photoAlbums = mediaGrab.getPhotoAlbums(this)
                photoAlbums.forEach {
                    Log.d(
                        this::class.java.simpleName,
                        "Example-MediaGrab-LOG %%% - photo album: $it"
                    )
                }
                val videoAlbums = mediaGrab.getVideoAlbums(this)
                Log.d(this::class.java.simpleName, "Example-MediaGrab-LOG %%% VIDEO ALBUMS")
                videoAlbums.forEach {
                    Log.d(
                        this::class.java.simpleName,
                        "Example-MediaGrab-LOG %%% - video album: $it"
                    )
                }
                Log.d(this::class.java.simpleName, "Example-MediaGrab-LOG %%% ALL ALBUMS")
                val allAlbums = mediaGrab.getAlbums(this)
                allAlbums.forEach {
                    Log.d(this::class.java.simpleName, "Example-MediaGrab-LOG %%% - album: $it")
                }
            }

            "ACTIVITY_LIST_OF_ALBUM" -> {
                val intent = Intent(this, ListAlbumActivity::class.java)
                startActivity(intent)
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
                Log.d(
                    this::class.java.simpleName,
                    "Example-MediaGrab-LOG %%% total photos: ${photos.size}"
                )
                photos.items?.forEach {
                    Log.d(this::class.java.simpleName, "Example-MediaGrab-LOG %%% item: ${it}")
                }

                // fetch images using pagination
//                val result = mediaGrab.getPhotos(this, offset = 0, size = 20)
//                Log.d(this::class.java.simpleName, "Example-MediaGrab-LOG %%% result: $result")
//                result.items?.forEach {
//                    Log.d(this::class.java.simpleName, "Example-MediaGrab-LOG %%% item: ${it}")
//                }
            }

            "GET_ALL_VIDEOS" -> {
                val videos = mediaGrab.getVideos(this)
                videos.items?.forEach {
                    Log.d(this::class.java.simpleName, "Example-MediaGrab-LOG %%% item: ${it}")
                }

                // fetch videos using pagination
//                val result = mediaGrab.getVideos(this, 0, 20)
//                Log.d(this::class.java.simpleName, "Example-MediaGrab-LOG %%% result: $result")
//                videos.forEach {
//                    Log.d(this::class.java.simpleName, "Example-MediaGrab-LOG %%% item: ${it}")
//                }
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