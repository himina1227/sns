package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.fixture.PostEntityFixture;
import com.example.sns.fixture.UserEntityFixture;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.PostEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostEntityRepository repository;

    @MockBean
    private UserEntityRepository userEntityRepository;
    @Test
    public void 포스트작성_성공할경우() {
        String title = "title";
        String body = "body";
        String userName = "userName";
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(repository.save(any())).thenReturn(mock(PostEntity.class));
        Assertions.assertDoesNotThrow(() -> postService.create(title, body, userName));
    }

    @Test
    void 포스트생성시_유저가_존재하지_않으면_에러를_내뱉는다() {
        String title = "title";
        String body = "body";
        String userName = "userName";
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(mock(PostEntity.class));
        SnsApplicationException exception = Assertions.assertThrows(SnsApplicationException.class, () -> postService.create(title, body, userName));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void 포스트_수정() {
        String title = "title";
        String body = "body";
        String userName = "userName";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(repository.save(any())).thenReturn(mock(PostEntity.class));
        Assertions.assertDoesNotThrow(() -> postService.create(title, body, userName));
    }

    @Test
    void 포스트_수정시_포스트가_존재하지_않으면_에러를_내뱉는다() {
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get("userName", postId, 1);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(repository.findById(postEntity.getId())).thenReturn(Optional.empty());

        SnsApplicationException exception = Assertions.assertThrows(SnsApplicationException.class, () ->
                postService.modify(postEntity.getUser().getUserName(), postEntity.getId(), postEntity.getTitle(), postEntity.getBody()));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 포스트_수정시_권한이_없는_경우() {
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get("userName", postId, 1);
        UserEntity writer = UserEntityFixture.get("userName", "password", 2);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(repository.findById(postEntity.getId())).thenReturn(Optional.of(postEntity));

        SnsApplicationException exception = Assertions.assertThrows(SnsApplicationException.class, () ->
                postService.modify(postEntity.getUser().getUserName(), postEntity.getId(), postEntity.getTitle(), postEntity.getBody()));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    public void 피드목록요청이_성공한경우() {
        Pageable pageable = mock(Pageable.class);
        when(repository.findAll(pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(() -> postService.list(pageable));
    }

    @Test
    public void 내피드목록요청이_성공한경우() {
        Pageable pageable = mock(Pageable.class);
        when(repository.findAllByUser(any(), pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(() -> postService.my("aa", pageable));
    }
}
