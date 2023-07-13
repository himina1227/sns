package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
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
}
