package com.hana.sns.post.controller

import com.hana.sns.common.controller.response.Response
import com.hana.sns.post.controller.port.PostService
import com.hana.sns.post.controller.request.PostCreateRequest
import com.hana.sns.post.controller.request.PostModifyRequest
import com.hana.sns.post.controller.response.PostResponse
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
    @PutMapping("/api/v1/posts/{postId}")
    fun modify(@PathVariable postId: Int, @RequestBody request: PostModifyRequest, authentication: Authentication): Response<PostResponse> {
        val result = PostResponse(postService.modify(postId, request.title, request.body, authentication.name))
        return Response.success(result)
    }

    @DeleteMapping("/api/v1/posts/{postId}")
    fun delete (@PathVariable postId: Int, authentication: Authentication): Response<Any> {
        return Response.success(postService.delete(postId, authentication.name));
    }

    @GetMapping("/api/v1/posts")
    fun list(pageable: Pageable) : Response<Page<PostResponse>>{
        return Response.success(postService.list(pageable))
    }
    @GetMapping("/api/v1/posts/my")
    fun my(pageable: Pageable, authentication: Authentication) : Response<Page<PostResponse>>{
        return Response.success(postService.my(pageable, authentication.name))
    }

    @PostMapping("/api/v1/posts/{postId}/likes")
    fun like(@PathVariable postId: Int, authentication: Authentication): Response<Any>  {
        return Response.success(postService.like(postId, authentication.name))
    }
}
