package com.hana.sns.post.controller.port

interface PostService {

    fun create(title: String, body: String, userName: String)
}