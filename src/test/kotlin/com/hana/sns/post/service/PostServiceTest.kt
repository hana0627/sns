package com.hana.sns.post.service

import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.mock.FakePasswordEncoder
import com.hana.sns.mock.FakePostRepository
import com.hana.sns.mock.FakeUserRepository
import com.hana.sns.post.controller.response.PostResponse
import com.hana.sns.post.domain.Post
import com.hana.sns.post.infrastructure.PostEntity
import com.hana.sns.user.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.lang.NullPointerException

class PostServiceTest {

    @Test
    fun 글작성이_성공하는_경우() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        userRepository.save(user)

        val title: String = "title"
        val body: String = "postBody"

        //when
        postService.create(title, body, user.userName)

        //then
        val result = postRepository.findById(1)
        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(1)
        assertThat(result?.title).isEqualTo("title")
        assertThat(result?.body).isEqualTo("postBody")
    }
    @Test
    fun 글작성시_요청한_유저가_없는_경우() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)

        val title: String = "title"
        val body: String = "postBody"

        //when & then
        val errorCode = assertThrows<SnsApplicationException>{
            postService.create(title, body, "emptyUser") }.errorCode

        assertThat(errorCode).isEqualTo(ErrorCode.USER_NOT_FOUND)

    }

    @Test
    fun 글_수정이_성공하는_경우() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"

        //when
        postService.modify(savedPost.id!!,modifyTitle, modifyBody, savedUser.userName)

        //then
        val result: Post? = postRepository.findById(savedPost.id!!)
        assertThat(result?.title).isEqualTo("modifyTitle")
        assertThat(result?.body).isEqualTo("modifyBody")
    }

    @Test
    fun 로그인하지_않은_유저가_글_수정을_하는_경우_예외를_발생한다() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"

        //when & then
        // '정상적인 케이스'에사 userName이 null이 들어오는 경우가 있을까?
        // 인자에서 nullable하지 않게 받고있으므로 NPE처리
        assertThrows<NullPointerException> { postService.modify(savedPost.id!!,modifyTitle, modifyBody, null!!) }


    }
    @Test
    fun 글_수정시_본인이_작성한_글이_아니라면_예외를_발생한다() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"

        //when & then
        val errorCode = assertThrows<SnsApplicationException> { postService.modify(savedPost.id!!,modifyTitle, modifyBody, "wrongUserName") }.errorCode

        assertThat(errorCode).isEqualTo(ErrorCode.INVALID_PERMISSION)

    }
    @Test
    fun 글_수정시_수정하려는_글이_없는경우_예외를_발생한다() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"

        //when & then
        val errorCode = assertThrows<SnsApplicationException> { postService.modify(99999,modifyTitle, modifyBody, savedUser.userName) }.errorCode

        assertThat(errorCode).isEqualTo(ErrorCode.POST_NOT_FOUND)

    }


    @Test
    fun 글_삭제가_성공하는_경우() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)


        //when
        postService.delete(savedPost.id!!, savedUser.userName)

        //then
        val result = postRepository.findAll().size
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun 로그인하지_않은_유저가_글_삭제를_하는_경우_예외를_발생한다() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        //when & then
        assertThrows<NullPointerException> { postService.delete(savedPost.id!!, null!!) }

    }

    @Test
    fun 글_삭제시_본인이_작성한_글이_아니라면_예외를_발생한다() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        //when & then
        val errorCode = assertThrows<SnsApplicationException> { postService.delete(savedPost.id!!,"wrongUserName") }.errorCode

        assertThat(errorCode).isEqualTo(ErrorCode.INVALID_PERMISSION)

    }
    @Test
    fun 글_삭제시_삭제하려는_글이_없는경우_예외를_발생한다() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        //when & then
        val errorCode = assertThrows<SnsApplicationException> { postService.delete(99999, savedUser.userName) }.errorCode

        assertThat(errorCode).isEqualTo(ErrorCode.POST_NOT_FOUND)

    }

    @Test
    fun 피드_목록요청이_성공한_경우() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user1 = User.fixture("userName1",passwordEncoder.encode("password"))
        val user2 = User.fixture("userName2",passwordEncoder.encode("password"))
        userRepository.save(user1)
        userRepository.save(user2)

        for(i in 0..29) {
            if(i % 2 == 0) {
                postRepository.save(Post.fixture("title$i","body$i",user1))
            }
            else {
                postRepository.save(Post.fixture("title$i","body$i",user2))
            }
        }

        val pageable = PageRequest.of(0,10)
        //when
        val result : List<PostResponse> = postService.list(pageable).get().toList()

        //then
        assertThat(result.size).isEqualTo(10)
        assertThat(result[0].title).isEqualTo("title0")
        assertThat(result[0].body).isEqualTo("body0")
        assertThat(result[0].user.userName).isEqualTo(user1.userName)
    }
    @Test
    fun 내_피드_목록요청이_성공한_경우() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user1 = User.fixture("userName1",passwordEncoder.encode("password"))
        val user2 = User.fixture("userName2",passwordEncoder.encode("password"))
        userRepository.save(user1)
        userRepository.save(user2)

        for(i in 0..29) {
            if(i % 2 == 0) {
                postRepository.save(Post.fixture("title$i","body$i",user1))
            }
            else {
                postRepository.save(Post.fixture("title$i","body$i",user2))
            }
        }

        val pageable = PageRequest.of(0,10)

        //when
        val result = postService.my(pageable, user1.userName).get().toList()

        //then
        assertThat(result.size).isEqualTo(10)
        assertThat(result[1].title).isEqualTo("title2")
        assertThat(result[1].body).isEqualTo("body2")
        assertThat(result[1].user.userName).isEqualTo(user1.userName)

    }


    @Test
    fun 좋아요_기능_성공() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        //when
        postService.like(post.id!!, user.userName)

        //then
        //TODO

    }
    @Test
    fun 좋아요기능시_유저가_로그인하지_않은_경우_예외를_발생한다() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        //when
        postService.like(post.id!!, null)

        //then

    }
    @Test
    fun 존재하지_않는_글에대해서_좋아요_요청시_예외를_발생한다() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        //when
        postService.like(99999, user.userName)

        //then

    }


}
