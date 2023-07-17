package com.example.sns.controller.response;

import com.example.sns.model.Comment;
import com.example.sns.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Integer id;

    private String comment;

    private String userName;

    private Integer postId;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;

    public static CommentResponse fromComment(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getUserName(),
                comment.getPostId(),
                comment.getRegisteredAt(),
                comment.getUpdatedAt(),
                comment.getRemovedAt()
        );
    }
}
