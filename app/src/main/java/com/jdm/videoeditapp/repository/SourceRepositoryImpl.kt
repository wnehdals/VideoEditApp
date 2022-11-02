package com.jdm.videoeditapp.repository

import com.jdm.videoeditapp.datasource.local.LocalDataSource
import com.jdm.videoeditapp.model.Album
import com.jdm.videoeditapp.model.Photo
import com.jdm.videoeditapp.model.Video
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SourceRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
): SourceRepository {
    override fun getVideoList(): Flow<MutableList<Video>> {
        return localDataSource.getVideoList()
    }

    override fun getPhotoList(): Flow<MutableList<Photo>> {
        return localDataSource.getPhotoList()
    }

    override fun getGifPhotoList(): Flow<MutableList<Photo>> {
        return localDataSource.getGifPhotoList()
    }
}