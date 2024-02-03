package com.hana.sns.post.service

import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.mock.*
import com.hana.sns.post.controller.port.PostService
import com.hana.sns.post.controller.response.CommentResponse
import com.hana.sns.post.controller.response.PostResponse
import com.hana.sns.post.domain.Comment
import com.hana.sns.post.domain.Post
import com.hana.sns.post.domain.PostLike
import com.hana.sns.user.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageRequest

class PostServiceTest {

    private lateinit var passwordEncoder: FakePasswordEncoder
    private lateinit var userRepository: FakeUserRepository
    private lateinit var postRepository: FakePostRepository
    private lateinit var postLikeRepository: FakePostLikeRepository
    private lateinit var commentRepository: FakeCommentRepository
    private lateinit var alramRepository: FakeAlarmRepository
    private lateinit var postService: PostService

    @BeforeEach
    fun setUp() {
        passwordEncoder = FakePasswordEncoder()
        userRepository = FakeUserRepository()
        postRepository = FakePostRepository()
        postLikeRepository = FakePostLikeRepository()
        commentRepository = FakeCommentRepository()
        alramRepository = FakeAlarmRepository()
        postService = PostServiceImpl(postRepository, postLikeRepository, commentRepository, alramRepository)
    }

    @Test
    fun 글작성이_성공하는_경우() {
        //given
        val user = User.fixture("userName",passwordEncoder.encode("password"))

        val title: String = "title"
        val body: String = "postBody"

        //when
        postService.create(title, body, user)

        //then
        val result = postRepository.findById(1)
        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(1)
        assertThat(result?.title).isEqualTo("title")
        assertThat(result?.body).isEqualTo("postBody")
    }
    
    // Controller에서 Authentication 객체로 유저검증
//    @Test
//    fun 글작성시_요청한_유저가_없는_경우() {
//        //given
//        val title: String = "title"
//        val body: String = "postBody"
//
//        val user = User.fixture("userName",passwordEncoder.encode("password"))
//
//
//        //when & then
//        val errorCode = assertThrows<SnsApplicationException>{
//            postService.create(title, body, user) }.errorCode
//
//        assertThat(errorCode).isEqualTo(ErrorCode.USER_NOT_FOUND)
//
//    }

    @Test
    fun 글_수정이_성공하는_경우() {
        //given
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"

        //when
        postService.modify(savedPost.id!!,modifyTitle, modifyBody, savedUser)

        //then
        val result: Post? = postRepository.findById(savedPost.id!!)
        assertThat(result?.title).isEqualTo("modifyTitle")
        assertThat(result?.body).isEqualTo("modifyBody")
    }
//
    @Test
    fun 로그인하지_않은_유저가_글_수정을_하는_경우_예외를_발생한다() {
        //given
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
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val wrongUser = User.fixture("userName1",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"

        //when & then
        val error = assertThrows<SnsApplicationException> { postService.modify(savedPost.id!!,modifyTitle, modifyBody, wrongUser) }

        assertThat(error.errorCode).isEqualTo(ErrorCode.INVALID_PERMISSION)
        assertThat(error.message).isEqualTo("userName1 has no permission with post(1)")

    }

    @Test
    fun 글_수정시_수정하려는_글이_없는경우_예외를_발생한다() {
        //given
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"

        //when & then
        val error = assertThrows<SnsApplicationException> { postService.modify(99999,modifyTitle, modifyBody, savedUser) }

        assertThat(error.errorCode).isEqualTo(ErrorCode.POST_NOT_FOUND)
        assertThat(error.message).isEqualTo("post(99999) is not founded")

    }


    @Test
    fun 글_삭제가_성공하는_경우() {
        //given
        val user1 = User.fixture("userName1",passwordEncoder.encode("password"))
        val user2 = User.fixture("userName2",passwordEncoder.encode("password"))
        val user3 = User.fixture("userName3",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user1)

        val savedUser: User = userRepository.save(user1)
        val savedPost: Post = postRepository.save(post)

        commentRepository.save(Comment(user1, post, "comment1!"))
        commentRepository.save(Comment(user2, post, "comment2!"))
        postLikeRepository.save(PostLike(user1, post))
        postLikeRepository.save(PostLike(user2, post))
        // val beforeComments = commentRepository.findAll()
        // val beforeLikes = postLikeRepository.findAll()
        // assertThat(beforeComments.size).isEqualTo(2)
        // assertThat(beforeLikes.size).isEqualTo(2)

        //when
        postService.delete(savedPost.id!!, savedUser)

        //then
        val result = postRepository.findAll().size

        val pageable = PageRequest.of(0,10)
        val afterComments = commentRepository.findAll()
        val afterLikes = postLikeRepository.findAll()
        assertThat(afterComments.size).isEqualTo(0)
        assertThat(afterLikes.size).isEqualTo(0)
        assertThat(result).isEqualTo(0)
    }

    // Controller에서 Authentication 객체로 유저검증
//    @Test
//    fun 로그인하지_않은_유저가_글_삭제를_하는_경우_예외를_발생한다() {
//        //given
//        val user = User.fixture("userName",passwordEncoder.encode("password"))
//        val post = Post.fixture("title","body",user)
//
//        val savedUser: User = userRepository.save(user)
//        val savedPost: Post = postRepository.save(post)
//
//        //when & then
//        assertThrows<NullPointerException> { postService.delete(savedPost.id!!, null!!) }
//
//    }

    @Test
    fun 글_삭제시_본인이_작성한_글이_아니라면_예외를_발생한다() {
        //given
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val wrongUser = User.fixture("userName1",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        //when & then
        val error = assertThrows<SnsApplicationException> { postService.delete(savedPost.id!!,wrongUser) }

        assertThat(error.errorCode).isEqualTo(ErrorCode.INVALID_PERMISSION)
        assertThat(error.message).isEqualTo("userName1 has no permission with post(1)")

    }

    @Test
    fun 글_삭제시_삭제하려는_글이_없는경우_예외를_발생한다() {
        //given
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        //when & then
        val error = assertThrows<SnsApplicationException> { postService.delete(99999, user) }

        assertThat(error.errorCode).isEqualTo(ErrorCode.POST_NOT_FOUND)
        assertThat(error.message).isEqualTo("post(99999) is not founded")

    }

    @Test
    fun 피드_목록요청이_성공한_경우() {
        //given
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
        val result = postService.my(pageable, user1).get().toList()

        //then
        assertThat(result.size).isEqualTo(10)
        assertThat(result[1].title).isEqualTo("title2")
        assertThat(result[1].body).isEqualTo("body2")
        assertThat(result[1].user.userName).isEqualTo(user1.userName)

    }


    @Test
    fun 좋아요_기능_성공() {
        //given
        val user1 = User.fixture("userName1",passwordEncoder.encode("password"))
        val user2 = User.fixture("userName2",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user1)

        val savedUser: User = userRepository.save(user1)
        val savedUser2: User = userRepository.save(user2)
        val savedPost: Post = postRepository.save(post)

        //when
        val id = postService.like(post.id!!, user2)

        //then
        val result = postLikeRepository.findById(id)
        val alarm = alramRepository.findAll()
        assertThat(result).isNotNull
        assertThat(alarm.size).isEqualTo(1)
    }

    // Controller에서 Authentication 객체로 유저검증
//    @Test
//    fun 좋아요기능시_유저가_로그인하지_않은_경우_예외를_발생한다() {
//        //given
//        val user = User.fixture("userName",passwordEncoder.encode("password"))
//        val post = Post.fixture("title","body",user)
//
//        postRepository.save(post)
//
//        //when & then
//        val error = assertThrows<NullPointerException> { postService.like(post.id!!, null!!) }
//    }

    @Test
    fun 존재하지_않는_글에대해서_좋아요_요청시_예외를_발생한다() {
        //given
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        //when & then
        val error = assertThrows<SnsApplicationException> { postService.like(999999, savedUser) }
        assertThat(error.errorCode).isEqualTo(ErrorCode.POST_NOT_FOUND)
        assertThat(error.message).isEqualTo("post(999999) is not founded")

    }


    @Test
    fun 이미_좋아요_누른_게시글에_좋아요를_다시_누르면_예외를_발생시킨다() {
        //given
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)
        postLikeRepository.save(PostLike(savedUser, savedPost))

        //when & then
        val error = assertThrows<SnsApplicationException> { postService.like(post.id!!, savedUser) }

        assertThat(error.errorCode).isEqualTo(ErrorCode.ALREADY_LIKED)
        assertThat(error.message).isEqualTo("userName userName already liked post 1")
    }


    @Test
    fun 게시글_조회시_해당게시글의_좋아요_숫자도_함께_응답한다() {
        //given
        val user1 = User.fixture("userName1",passwordEncoder.encode("password"))
        val user2 = User.fixture("userName2",passwordEncoder.encode("password"))
        val user3 = User.fixture("userName3",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user1)

        userRepository.save(user1)
        userRepository.save(user2)
        userRepository.save(user3)
        val savedPost = postRepository.save(post)

        postLikeRepository.save(PostLike(user1, post))
        postLikeRepository.save(PostLike(user2, post))
        postLikeRepository.save(PostLike(user3, post))

        //when
        val result = postService.likeCount(savedPost.id!!)

        //then
        assertThat(result).isEqualTo(3)
    }

    @Test
    fun 댓글작성_기능_성공() {
        //given
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val comment = "comment"

        //when
        val result: Long = postService.comment(savedPost.id!!, savedUser, comment)
        val savedEntity = commentRepository.findById(result)
        //then
        assertThat(savedEntity).isNotNull
        assertThat(savedEntity?.post?.id).isEqualTo(savedPost.id)
        assertThat(savedEntity?.user?.id).isEqualTo(savedUser.id)
        assertThat(savedEntity?.comment).isEqualTo("comment")

    }

    @Test
    fun 댓글_작성시_유저가_로그인하지_않은_경우_예외를_발생한다() {
        //given
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        postRepository.save(post)
        val comment = "comment"

        //when & then
        val error = assertThrows<NullPointerException> { postService.comment(post.id!!, null!!, comment) }
    }

    @Test
    fun 존재하지_않는_글에대해서_댓글_요청시_예외를_발생한다() {
        //given
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)
        val comment = "comment"

        //when & then
        val error = assertThrows<SnsApplicationException> { postService.comment(999999, savedUser, comment) }
        assertThat(error.errorCode).isEqualTo(ErrorCode.POST_NOT_FOUND)
        assertThat(error.message).isEqualTo("post(999999) is not founded")

    }

    @Test
    fun 댓글_목록_요청이_성공한_경우() {
        //given

        val savedPost: Post = postRepository.save(Post.fixture())
        for(i in 0..29) {
            val user = User.fixture("userName$i",passwordEncoder.encode("password"))
            userRepository.save(user)
            commentRepository.save(Comment(user,savedPost,"comments$i"))
        }


        val pageable = PageRequest.of(1,10)
        //when
        val result: List<CommentResponse> = postService.getComments(savedPost.id!!, pageable).get().toList()

        //then
        assertThat(result.size).isEqualTo(10)
        assertThat(result[1].post.id).isEqualTo(savedPost.id)
        assertThat(result[1].post.body).isEqualTo(savedPost.body)
        assertThat(result[1].comment).isEqualTo("comments11")
        assertThat(result[1].user.userName).isEqualTo("userName11")
    }

    @Test
    fun 없는_게시글에_대한_댓글을_요청할_경우() {
        //given
        val savedPost: Post = postRepository.save(Post.fixture())
        for(i in 0..5) {
            val user = User.fixture("userName$i",passwordEncoder.encode("password"))
            userRepository.save(user)
            commentRepository.save(Comment(user,savedPost,"comment$i"))
        }


        val pageable = PageRequest.of(1,10)
        //when & then
        val error = assertThrows<SnsApplicationException> { postService.getComments(999999, pageable).get().toList() }

        assertThat(error.errorCode).isEqualTo(ErrorCode.POST_NOT_FOUND)
        assertThat(error.message).isEqualTo("post(999999) is not founded")
    }


}
