package com.example.sns.model;

import com.example.sns.model.entity.CommentEntity;
import com.example.sns.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Comment {
    private Integer id;

    private String comment;

    private String userName;

    private Integer postId;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;

    public static Comment fromEntity(CommentEntity entity) {
        return new Comment(
                entity.getId(),
                entity.getComment(),
                entity.getUser().getUserName(),
                entity.getPost().getId(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }

    public static List<Comment> fromEntities(List<CommentEntity> entities) {
        return entities.stream().map(Comment::fromEntity).collect(Collectors.toList());
    }
}