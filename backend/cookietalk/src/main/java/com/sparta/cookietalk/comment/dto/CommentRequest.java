package com.sparta.cookietalk.comment.dto;

import com.sparta.cookietalk.comment.dto.CommentRequest.Create;
import com.sparta.cookietalk.comment.dto.CommentRequest.Update;

public sealed interface CommentRequest permits Create, Update {
    record Create(String contents) implements CommentRequest{ }
    record Update(String contents) implements CommentRequest{ }
}
