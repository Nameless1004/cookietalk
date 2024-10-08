package com.sparta.cookietalk.comment.dto;

import com.sparta.cookietalk.comment.dto.CommentResponse.Create;
import com.sparta.cookietalk.comment.dto.CommentResponse.List;
import java.time.LocalDateTime;
import lombok.Builder;

public sealed interface CommentResponse permits Create, List {

    @Builder
    record Create(Long id, String contents) implements CommentResponse {}

    @Builder
    record List(Long userId, String nickname, Long id, String contents, LocalDateTime createdAt) implements CommentResponse { }
}
