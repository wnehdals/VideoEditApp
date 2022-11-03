package com.jdm.videoeditapp.datasource

import com.jdm.videoeditapp.model.SourceMedia
import kotlinx.coroutines.flow.Flow

interface DataSource {
    fun getVideoList(): Flow<MutableList<SourceMedia>>
    fun getPhotoList(): Flow<MutableList<SourceMedia>>
}