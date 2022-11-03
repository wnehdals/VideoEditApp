package com.jdm.videoeditapp.repository

import com.jdm.videoeditapp.datasource.local.LocalDataSource
import com.jdm.videoeditapp.model.SourceMedia
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip
import java.util.*
import javax.inject.Inject

class SourceRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : SourceRepository {
    override fun getVideoList(): Flow<MutableList<SourceMedia>> {
        return localDataSource.getVideoList()
    }

    override fun getPhotoList(): Flow<MutableList<SourceMedia>> {
        return localDataSource.getPhotoList()
    }

}