package com.hana.sns.mock

import com.hana.sns.post.domain.Comment
import com.hana.sns.post.domain.Post
import com.hana.sns.post.service.port.CommentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.concurrent.atomic.AtomicLong

class FakeCommentRepository : CommentRepository {
    private val autoGeneratedId: AtomicLong = AtomicLong(0)
    var data = mutableListOf<Comment>()
    override fun save(comment: Comment): Comment {
        if(comment.id == 0L || comment.id == null) {
            comment.id = autoGeneratedId.incrementAndGet()
            data.add(comment)
        } else {
            data.removeIf { it.id == comment.id }
            comment.id = autoGeneratedId.incrementAndGet()
            data.add(comment)
        }
        return comment
    }

    override fun findAllByPost(post: Post, pageable: Pageable): Page<Comment> {
        val pageSize = pageable.pageSize
        val offset = pageable.offset.toInt()

        val startIdx = offset.coerceAtMost(data.size - 1)
        val endIdx = (offset + pageSize).coerceAtMost(data.size)

        val sublist = data.subList(startIdx, endIdx)

        return PageImpl(sublist, pageable, data.size.toLong())
    }

    fun findById(id: Long): Comment? {
        return data.find { it.id == id }
    }

}
