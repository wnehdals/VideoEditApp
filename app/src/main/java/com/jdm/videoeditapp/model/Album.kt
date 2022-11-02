package com.jdm.videoeditapp.model

data class Album(
    val name: String,
    val photoList: MutableList<Video>
) {
    val mediaCount: Int = photoList.size
}