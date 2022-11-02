package com.jdm.videoeditapp.repository

import com.jdm.videoeditapp.model.Photo
import com.jdm.videoeditapp.model.Video
import kotlinx.coroutines.flow.Flow

interface SourceRepository {
    fun getVideoList(): Flow<MutableList<Video>>
    fun getPhotoList(): Flow<MutableList<Photo>>
    fun getGifPhotoList(): Flow<MutableList<Photo>>
}