package com.sparta.cookietalk.comment.service;

import com.sparta.cookietalk.comment.dto.CommentRequest;
import com.sparta.cookietalk.comment.dto.CommentRequest.Update;
import com.sparta.cookietalk.comment.dto.CommentResponse;
import com.sparta.cookietalk.comment.dto.CommentResponse.Create;
import com.sparta.cookietalk.comment.entity.Comment;
import com.sparta.cookietalk.comment.repository.CommentRepository;
import com.sparta.cookietalk.common.dto.Response;
import com.sparta.cookietalk.common.exceptions.AuthException;
import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import com.sparta.cookietalk.cookie.entity.Cookie;
import com.sparta.cookietalk.cookie.repository.CookieRepository;
import com.sparta.cookietalk.security.AuthUser;
import com.sparta.cookietalk.user.entity.User;
import com.sparta.cookietalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CookieRepository cookieRepository;

    /**
     * 쿠키 생성
     * @param authUser 로그인한 유저
     * @param cookieId 쿠키의 고유 아이디
     * @param request
     * @return 등록한 댓글의 아이디, 내용
     */
    public CommentResponse.Create registComment(AuthUser authUser, Long cookieId, CommentRequest.Create request) {

        Cookie cookie = cookieRepository.findById(cookieId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 쿠키입니다."));

        User user = userRepository.findByIdOrElseThrow(authUser.getUserId());

        Comment newComment = Comment.builder()
            .cookie(cookie)
            .user(user)
            .contents(request.contents()).build();

        newComment = commentRepository.save(newComment);
        return Create.builder()
            .id(newComment.getId())
            .contents(newComment.getContents())
            .build();
    }

    /**
     * 쿠키의 댓글 페이징 조회
     * @param page 조회 페이지
     * @param size 페이지 크기
     * @param cookieId 조회할 쿠키 아이디
     * @return
     */
    @Transactional(readOnly = true)
    public Response.Page<CommentResponse.List> getCommentList(int page, int size, Long cookieId) {
        Cookie cookie = cookieRepository.findById(cookieId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 쿠키입니다."));

        Pageable pageable = PageRequest.of(page - 1, size);

        return commentRepository.getCommentsByCookieId(pageable, cookieId);
    }


    /**
     * 댓글 수정
     * @param authUser
     * @param request
     * @param cookieId
     * @param commentId
     * @return
     */
    public CommentResponse.List updateComment(AuthUser authUser, Update request, Long cookieId, Long commentId) {


        Comment comment = commentRepository.findWithUserAndCookieById(commentId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 댓글입니다."));

        if(comment.getCookie().getId() != cookieId) {
            throw new InvalidRequestException("해당 게시글에 해당 댓글이 존재하지 않습니다.");
        }

        if(comment.getAuthUser().getId() != authUser.getUserId()) {
            throw new AuthException("삭제 권한이 없습니다.");
        }

        comment.updateContents(request.contents());

        return CommentResponse.List.builder()
            .userId(comment.getAuthUser().getId())
            .nickname(comment.getAuthUser().getNickname())
            .id(commentId)
            .contents(comment.getContents())
            .createdAt(comment.getCreatedAt())
            .build();
    }

    /**
     * 코멘트 삭제
     * @param authUser 로그인 한 유저
     * @param cookieId 삭제할 댓글의 쿠키 아이디
     * @param commentId 삭제할 댓글의 아이디
     */
    public void deleteComment(AuthUser authUser, long cookieId, long commentId) {
        Comment comment = commentRepository.findWithUserAndCookieById(commentId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 댓글입니다."));

        if(comment.getCookie().getId() != cookieId) {
            throw new InvalidRequestException("해당 게시글에 해당 댓글이 존재하지 않습니다.");
        }

        if(comment.getAuthUser().getId() != authUser.getUserId()) {
            throw new AuthException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
