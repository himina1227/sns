package com.example.sns.controller;

import com.example.sns.controller.request.PostCommentRequest;
import com.example.sns.controller.request.PostModifyRequest;
import com.example.sns.controller.request.PostWriteRequest;
import com.example.sns.controller.response.CommentResponse;
import com.example.sns.controller.response.PostResponse;
import com.example.sns.controller.response.Response;
import com.example.sns.model.Comment;
import com.example.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostWriteRequest request, Authentication authentication) {
        postService.create(authentication.getName(), request.getTitle(), request.getBody());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<Void> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication) {
        postService.modify(authentication.getName(), postId, request.getTitle(), request.getBody());
        return Response.success();

    }

    @DeleteMapping ("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);
        return Response.success();

    }

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable, Authentication authentication) {
        return Response.success(postService.list(pageable).map(PostResponse::fromPost));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> my(Pageable pageable, Authentication authentication) {
        return Response.success(postService.my(authentication.getName(), pageable).map(PostResponse::fromPost));
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> like(@PathVariable Integer postId, Authentication authentication) {
        postService.like(postId, authentication.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    public Response<Integer> likeCount(@PathVariable Integer postId, Authentication authentication) {
        return Response.success(postService.likeCount(postId, authentication.getName()));
    }

    @PostMapping("/{postId}/comments")
    public Response<Integer> comment(@PathVariable Integer postId, PostCommentRequest request, Authentication authentication) {
        postService.comment(postId, request.getComment(), authentication.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> getComments(@PathVariable Integer postId, Pageable pageable, Authentication authentication) {
        Page<Comment> comments = postService.getComments(postId, pageable);
        return Response.success(comments.map(CommentResponse::fromComment));
    }
}
