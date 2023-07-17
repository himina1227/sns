package com.example.sns.repository;

import com.example.sns.model.entity.CommentEntity;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
    Page<CommentEntity> findAllByPost(PostEntity post, Pageable pageable);
}
