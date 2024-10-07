package com.sparta.cookietalk.comment.controller;

import com.sparta.cookietalk.comment.dto.CommentRequest;
import com.sparta.cookietalk.comment.dto.CommentResponse;
import com.sparta.cookietalk.comment.service.CommentService;
import com.sparta.cookietalk.common.dto.Response;
import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/v1/cookies/{cookieId}/comments")
    public ResponseEntity<ResponseDto<Response.Page<CommentResponse.List>>> getComments(@PathVariable("cookieId") long cookieId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) {

        return ResponseDto.toEntity(HttpStatus.OK, commentService.getCommentList(page, size, cookieId));
    }

    @PatchMapping("/v1/cookies/{cookieId}/comments/{commentId}")
    public ResponseEntity<ResponseDto<CommentResponse.List>> updateComment(@PathVariable("cookieId") long cookieId, @PathVariable("commentId") long commentId,
        @AuthenticationPrincipal AuthUser authUser,
        @RequestBody CommentRequest.Update request) {
        return ResponseDto.toEntity(HttpStatus.OK, commentService.updateComment(authUser, request, cookieId, commentId));
    }


    @PostMapping("/v1/cookies/{cookieId}/comments")
    public ResponseEntity<ResponseDto<CommentResponse.Create>> registComment(@PathVariable("cookieId") long cookieId,
        @AuthenticationPrincipal AuthUser authUser,
        @RequestBody CommentRequest.Create request) {

        return ResponseDto.toEntity(HttpStatus.CREATED, commentService.registComment(authUser, cookieId, request));
    }

    @DeleteMapping("/v1/cookies/{cookieId}/comments/{commentId}")
    public ResponseEntity<ResponseDto<Void>> deleteComment(@PathVariable("cookieId") long cookieId, @PathVariable("commentId") long commentId,
        @AuthenticationPrincipal AuthUser authUser) {
        commentService.deleteComment(authUser, cookieId, commentId);
        return ResponseDto.toEntity(HttpStatus.OK, "성공적으로 삭제되었습니다.", null);
    }
}
