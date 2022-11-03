package com.jdm.videoeditapp.datasource.local

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import com.jdm.videoeditapp.datasource.DataSource
import com.jdm.videoeditapp.model.MediaType
import com.jdm.videoeditapp.model.SourceMedia
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val context: Context
): DataSource {
    override fun getVideoList(): Flow<MutableList<SourceMedia>>  = flow {
        val result = mutableListOf<SourceMedia>()

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DURATION,
        )

        val collecton = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }

        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.SIZE + " > 0"
        } else {
            null
        }

        val query = context.contentResolver.query(
            collecton,
            projection,
            selection,
            null,
            sortOrder
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val uriColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val dateAddedColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
            val mineColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val mediaUri = getVideoMediaUri(id, cursor.getString(uriColumn))
                val datedAddedSecond = cursor.getLong(dateAddedColumn)
                var duration = cursor.getInt(durationColumn)
                result.add(SourceMedia(id = id, uri = mediaUri, dateAddedSecond = datedAddedSecond, duration = duration, type = MediaType.VIDEO))
            }
        }
        query?.close()
        emit(result)

    }.catch {
        emit(mutableListOf())
    }

    override fun getPhotoList(): Flow<MutableList<SourceMedia>> = flow {
        val result = mutableListOf<SourceMedia>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.MIME_TYPE,
        )

        val collecton = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.SIZE + " > 0"
        } else {
            null
        }

        val query = context.contentResolver.query(
            collecton,
            projection,
            selection,
            null,
            sortOrder
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val uriColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val dateAddedColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            val mineColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val mediaUri = getImageMediaUri(id, cursor.getString(uriColumn))
                val datedAddedSecond = cursor.getLong(dateAddedColumn)
                var mineType = cursor.getString(mineColumn)
                if (TextUtils.equals(mineType, "image/gif"))
                    result.add(SourceMedia(id = id, uri = mediaUri, dateAddedSecond = datedAddedSecond, type = MediaType.GIF))
                else
                    result.add(SourceMedia(id = id, uri = mediaUri, dateAddedSecond = datedAddedSecond, type = MediaType.PHOTO))
            }
        }
        query?.close()
        emit(result)
    }.catch {
        emit(mutableListOf())
    }

    private fun getVideoMediaUri(id: Long, str: String): Uri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            ContentUris.withAppendedId(contentUri, id)
        } else {
            Uri.fromFile(File(str))
        }
    private fun getImageMediaUri(id: Long, str: String): Uri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ContentUris.withAppendedId(contentUri, id)
        } else {
            Uri.fromFile(File(str))
        }
}