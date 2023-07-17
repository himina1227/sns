package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.Post;
import com.example.sns.model.entity.LikeEntity;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.LikeEntityRepository;
import com.example.sns.repository.PostEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostEntityRepository repository;
    private final UserEntityRepository userRepository;
    private final LikeEntityRepository likeRepository;

    public void create(String title, String body, String userName) {
        UserEntity userEntity = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName)));
//        PostEntity postEntity = PostEntity.of(title, body, userEntity);
        PostEntity postEntity = repository.save(PostEntity.of(title, body, userEntity));
        repository.save(postEntity);
    }

    public Post modify(String userName, Integer postId, String title, String body) {
        PostEntity postEntity = repository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        UserEntity user = userRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));
        if (!Objects.equals(postEntity.getUser().getId(), user.getId())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userName, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(repository.saveAndFlush(postEntity));
    }

    public void delete(String userName, Integer postId) {
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));
        PostEntity postEntity = repository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        if (!Objects.equals(postEntity.getUser().getId(), userEntity.getId())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userName, postId));
        }
        repository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable) {
        return repository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName, Pageable pageable) {
        UserEntity userEntity = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));
        return repository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    public void like(Integer postId, String userName) {
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));
        PostEntity postEntity = repository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));

        likeRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED_POST, String.format("userName %s already like post %d", userName, postId));
        });

        likeRepository.save(LikeEntity.of(userEntity, postEntity));
    }

    public int likeCount(Integer postId, String userName) {
        PostEntity postEntity = repository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        return likeRepository.countByPost(postEntity);
//        List<LikeEntity> likeEntities = likeRepository.findAllByPost(postEntity);
//        return likeEntities.size();
    }

    public void comment(Integer postId, String userName) {

    }
}
