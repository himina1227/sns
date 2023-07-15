package com.example.sns.controller;

import com.example.sns.controller.request.PostModifyRequest;
import com.example.sns.controller.request.PostWriteRequest;
import com.example.sns.controller.response.Response;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.PostEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import com.example.sns.service.PostService;
import lombok.RequiredArgsConstructor;
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
}
