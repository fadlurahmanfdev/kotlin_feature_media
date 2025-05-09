# Overview

Library to simplify pick media operation, such as from gallery or files.

## Installation

```kotlin
implementation("com.fadlurahmanfdev.pixmed:x.y.z")
```

### Usage

#### Get List Photo/Video Album

Get list all album (photo/video/all)

```kotlin
// get photo albums
val photoAlbums = pixMed.getPhotoAlbums(this)

// get video albums
val videoAlbums = pixMed.getVideoAlbums(this)

// get all album (photo + video)
val allAlbums = pixMed.getAlbums(this)
```

#### Get List Photo/Video

Get list all image or video

```kotlin
// Get list of all photo
val photos = pixMed.getPhotos(this)

// Get 20 photos with offset start from 0
val photosWithPagination = pixMed.getPhotos(this, 0, 20)

// Get list of all video
val videos = pixMed.getVideos(this)

// Get 20 videos with offset start from 0
val photosWithPagination = pixMed.getVideos(this, 0, 20)
```

