package com.jdm.videoeditapp.repository

import com.jdm.videoeditapp.model.SourceMedia
import kotlinx.coroutines.flow.Flow

interface SourceRepository {
    fun getVideoList(): Flow<MutableList<SourceMedia>>
    fun getPhotoList(): Flow<MutableList<SourceMedia>>
}