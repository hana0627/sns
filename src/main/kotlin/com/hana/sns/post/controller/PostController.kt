package com.hana.sns.post.controller

import com.hana.sns.common.controller.response.Response
import com.hana.sns.post.controller.port.PostService
import com.hana.sns.post.controller.request.PostCreateRequest
import lombok.RequiredArgsConstructor
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RequiredArgsConstructor
@RestController
class PostController(
    private val postService: PostService
) {


    @PostMapping("/api/v1/posts")
    fun create(@RequestBody request: PostCreateRequest, authentication: Authentication): Response<Any> {
        return Response.success(postService.create(request.title, request.body, authentication.name));
    }

}
