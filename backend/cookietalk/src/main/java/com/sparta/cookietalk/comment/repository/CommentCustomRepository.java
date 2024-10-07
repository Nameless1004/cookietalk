package com.sparta.cookietalk.comment.repository;

import com.sparta.cookietalk.comment.dto.CommentResponse;
import com.sparta.cookietalk.common.dto.Response;
import org.springframework.data.domain.Pageable;

public interface CommentCustomRepository {

    Response.Page<CommentResponse.List> getCommentsByCookieId(Pageable pageable, Long cookieId);
}
