package com.hana.sns.post.controller

import com.hana.sns.common.controller.response.Response
import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.mock.FakePasswordEncoder
import com.hana.sns.mock.FakePostRepository
import com.hana.sns.mock.FakeUserRepository
import com.hana.sns.mock.TestContainer
import com.hana.sns.post.controller.request.PostCreateRequest
import com.hana.sns.post.controller.request.PostModifyRequest
import com.hana.sns.post.controller.response.PostResponse
import com.hana.sns.post.domain.Post
import com.hana.sns.post.domain.PostLike
import com.hana.sns.post.service.PostServiceImpl
import com.hana.sns.user.controller.response.UserJoinResponse
import com.hana.sns.user.domain.User
import com.hana.sns.user.domain.en.UserRole
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.springframework.data.domain.PageRequest
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.lang.NullPointerException

class PostControllerTest {


    @Test
    fun 글작성_성공 () {
        //given
        val testContainer = TestContainer.build()
        val userRepository = testContainer.userRepository
        val passwordEncoder = testContainer.passwordEncoder

        userRepository.save(User("userName",passwordEncoder.encode("password")))

        val postCreateRequest = PostCreateRequest("title", "body")
        val authentication = TestingAuthenticationToken("userName","password", mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))

        //when
        val result = testContainer.postController.create(postCreateRequest, authentication)

        //then
        assertThat(result.resultCode).isEqualTo("SUCCESS")
        assertThat(result.result).isEqualTo(1)
    }

    @Test
    fun 글작성시_없는userName이라면_예외발생 () {
        //given
        val testContainer = TestContainer.build()
        val userRepository = testContainer.userRepository
        val passwordEncoder = testContainer.passwordEncoder

        userRepository.save(User("userName",passwordEncoder.encode("password")))

        val postCreateRequest = PostCreateRequest("title", "body")
        val authentication = TestingAuthenticationToken("wrongUserName","password", mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))

        //when
        val result = assertThrows<SnsApplicationException> { testContainer.postController.create(postCreateRequest, authentication) }.errorCode

        //then
        assertThat(result).isEqualTo(ErrorCode.USER_NOT_FOUND)

    }

    @Test
    fun 글_수정이_성공하는_경우() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"
        val postModifyRequest = PostModifyRequest(modifyTitle, modifyBody)

        val authentication = TestingAuthenticationToken("userName","password", mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))
        //when
        val result: Response<PostResponse> = postController.modify(savedPost.id!!, postModifyRequest , authentication)

        //then
        assertThat(result.resultCode).isEqualTo("SUCCESS")
        assertThat(result.result.title).isEqualTo(modifyTitle)
        assertThat(result.result.body).isEqualTo(modifyBody)
        assertThat(result.result.user.userName).isEqualTo(savedUser.userName)
    }

    @Test
    fun 로그인하지_않은_유저가_글_수정을_하는_경우_예외를_발생한다() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"
        val postModifyRequest = PostModifyRequest(modifyTitle, modifyBody)

        //when & then
        assertThrows<NullPointerException>  { postController.modify(savedPost.id!!, postModifyRequest , null!!) }
    }
    @Test
    fun 글_수정시_본인이_작성한_글이_아니라면_예외를_발생한다() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"
        val postModifyRequest = PostModifyRequest(modifyTitle, modifyBody)

        val authentication = TestingAuthenticationToken("wrongUserName","password", mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))
        //when
        val errorCode = assertThrows<SnsApplicationException> {
            postController.modify(savedPost.id!!, postModifyRequest , authentication)
        }.errorCode

        //then
        assertThat(errorCode).isEqualTo(ErrorCode.INVALID_PERMISSION)
    }
    @Test
    fun 글_수정시_수정하려는_글이_없는경우_예외를_발생한다() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"
        val postModifyRequest = PostModifyRequest(modifyTitle, modifyBody)

        val authentication = TestingAuthenticationToken("userName","password", mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))
        //when
        val errorCode = assertThrows<SnsApplicationException> {
            postController.modify(9999, postModifyRequest , authentication)
        }.errorCode

        //then
        assertThat(errorCode).isEqualTo(ErrorCode.POST_NOT_FOUND)
    }



    @Test
    fun 글_삭제가_성공하는_경우() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val authentication = TestingAuthenticationToken("userName","password", mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))
        //when
        val result = postController.delete(savedPost.id!!, authentication)

        //then
        val size = postRepository.findAll().size
        assertThat(result.resultCode).isEqualTo("SUCCESS")
        assertThat(result.result).isEqualTo(savedPost.id)
        assertThat(size).isEqualTo(0)
    }

    @Test
    fun 로그인하지_않은_유저가_글_삭제를_하는_경우_예외를_발생한다() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        //when & then
        assertThrows<NullPointerException>  { postController.delete(savedPost.id!!, null!!) }
    }
    @Test
    fun 글_삭제시_본인이_작성한_글이_아니라면_예외를_발생한다() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val authentication = TestingAuthenticationToken("wrongUserName","password", mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))
        //when
        val errorCode = assertThrows<SnsApplicationException> {
            postController.delete(savedPost.id!! , authentication)
        }.errorCode

        //then
        assertThat(errorCode).isEqualTo(ErrorCode.INVALID_PERMISSION)
    }
    @Test
    fun 글_삭제시_삭제하려는_글이_없는경우_예외를_발생한다() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val authentication = TestingAuthenticationToken("userName","password", mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))
        //when
        val errorCode = assertThrows<SnsApplicationException> {
            postController.delete(9999 , authentication)
        }.errorCode

        //then
        assertThat(errorCode).isEqualTo(ErrorCode.POST_NOT_FOUND)
    }


    @Test
    fun 피드_목록요청이_성공한_경우() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

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
        val result = postController.list(pageable)
        val resultBody = result.result.get().toList()
        //then
        assertThat(result.resultCode).isEqualTo("SUCCESS")
        assertThat(resultBody.size).isEqualTo(10)
        assertThat(resultBody[0].title).isEqualTo("title0")
        assertThat(resultBody[0].body).isEqualTo("body0")
        assertThat(resultBody[0].user.userName).isEqualTo(user1.userName)
    }


    @Test
    fun 내_피드목록_요청이_성공한_경우() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

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
        val authentication = TestingAuthenticationToken(user1.userName,user1.password, mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))
        //when
        val result = postController.my(pageable, authentication)
        val resultBody = result.result.get().toList()
        //then
        assertThat(result.resultCode).isEqualTo("SUCCESS")
        assertThat(resultBody.size).isEqualTo(10)
        assertThat(resultBody[1].title).isEqualTo("title2")
        assertThat(resultBody[1].body).isEqualTo("body2")
        assertThat(resultBody[1].user.userName).isEqualTo(user1.userName)
    }


    @Test
    fun 좋아요_기능_성공() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val authentication = TestingAuthenticationToken(user.userName,user.password, mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))

        //when
        val result = postController.like(savedPost.id!!, authentication)

        //then
        assertThat(result.resultCode).isEqualTo("SUCCESS")
        assertThat(result.result).isEqualTo(1)
    }


    @Test
    fun 좋아요기능시_유저가_로그인하지_않은_경우_예외를_발생한다() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedPost: Post = postRepository.save(post)


        //when & then
        val result = assertThrows<NullPointerException> { postController.like(savedPost.id!!, null!!) }
    }


    @Test
    fun 존재하지_않는_글에대해서_좋아요_요청시_예외를_발생한다() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))

        val savedUser: User = userRepository.save(user)

        val authentication = TestingAuthenticationToken(user.userName,user.password, mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))

        //when & then
        val result = assertThrows<SnsApplicationException> { postController.like(999999, authentication) }
        assertThat(result.errorCode).isEqualTo(ErrorCode.POST_NOT_FOUND)
        assertThat(result.message).isEqualTo("post(999999) is not founded")

    }

    @Test
    fun 이미_좋아요_누른_게시글에_좋아요를_다시_누르면_예외를_발생시킨다() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val postRepository = testContainer.postRepository
        val userRepository = testContainer.userRepository
        val passwordEncoder = testContainer.passwordEncoder
        val postLikeRepository = testContainer.postLikeRepository


        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        postLikeRepository.save(PostLike(savedUser, savedPost))

        val authentication = TestingAuthenticationToken(user.userName,user.password, mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))

        //when & then
        val error = assertThrows<SnsApplicationException> { postController.like(savedPost.id!!, authentication) }
        assertThat(error.errorCode).isEqualTo(ErrorCode.ALREADY_LIKED)
        assertThat(error.message).isEqualTo("userName userName already liked post 1")

    }


}
