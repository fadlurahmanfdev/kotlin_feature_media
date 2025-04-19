package co.id.fadlurahmanf.kotlin_feature_media.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import java.io.File


class MainActivity : AppCompatActivity(), ListExampleAdapter.Callback {
    lateinit var viewModel: MainViewModel
    lateinit var mediaGrab: MediaGrab

    private val features: List<FeatureModel> = listOf<FeatureModel>(
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Pick Image Visual Media",
            desc = "Pick image via pick visual media",
            enum = "PICK_IMAGE_VISUAL_MEDIA"
        ),
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "----------------",
            desc = "----------------------------------------",
            enum = "DIVIDER-PICK-IMAGE"
        ),
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Get List Album",
            desc = "Get list of all album",
            enum = "GET_ALBUMS"
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
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Start File Explorer",
            desc = "Start File Explorer",
            enum = "START_FILE_EXPLORER"
        ),
    )

    private var pickImageVisualMediaLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val mediaItem = mediaGrab.getMediaItemModelFromUri(this, uri)
                Log.d(
                    this::class.java.simpleName,
                    "Example-MediaGrab-LOG %%% media item: $mediaItem"
                )
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
            "PICK_IMAGE_VISUAL_MEDIA" -> {
                pickImageVisualMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            "GET_ALBUMS" -> {
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

            "GET_ALL_IMAGE" -> {
                val intent = Intent(this, ListImageActivity::class.java)
                startActivity(intent)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("MASUK REQUEST CODE: $requestCode")
        println("MASUK RESULT CODE: $resultCode")
        if (requestCode == 900 && resultCode == Activity.RESULT_OK) {
            val itemCount = data?.clipData?.itemCount

            if (itemCount != null) {
                var currentItem = 0
                while (currentItem < itemCount) {
                    val currentUri = data.clipData?.getItemAt(currentItem)?.uri
                    println("MASUK PATH: $currentUri")
                    println("MASUK REAL PATH: ${getRealPathFromURI(this, currentUri!!)}")
                    currentItem++
                }
            }


            println("MASUK ITEM COUNT: ${data?.clipData?.itemCount}")
        }
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String?>?,
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )

        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    fun getRealPathFromURI(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                val docId = DocumentsContract.getDocumentId(uri);
//                val split = docId . split (":");
//                val type = split[0];
//
//                if ("primary".equals(type, ignoreCase = true)) {
//                    return "${Environment.getExternalStorageDirectory().path}/${split[1]}"
//                }
//
//                // TODO handle non-primary volumes
//            } else if (isDownloadsDocument(uri)) {
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id)
//                );
//
//                return getDataColumn(context, contentUri, null, null);
//            } else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String [] split = docId . split (":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String [] selectionArgs = new String[] {
//                    split[1]
//                };
//
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        } else

//            else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }

        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            println("MASUK SINI CONTENT SCHEME")

            // Return the remote address
//            if (isGooglePhotosUri(uri))
//                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        return null;
    }
}