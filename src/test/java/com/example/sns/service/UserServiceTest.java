package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.fixture.UserEntityFixture;
import com.example.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository repository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void 회원가입이_정상적으로_동작하는_경우 () {
        String userName = "himina";
        String password = "password";

        when(repository.findByUserName(userName)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encrypt_password");
        when(repository.save(any())).thenReturn(mock(UserEntity.class));

        Assertions.assertDoesNotThrow(() -> userService.join(userName, password));
    }

    @Test
    public void 회원가입시_userName으로_회원가입한_유저가_이미_있는경우 () {
        String userName = "himina";
        String password = "password";

        when(repository.findByUserName(userName)).thenReturn(Optional.of(UserEntityFixture.get(userName, password)));
        when(passwordEncoder.encode(password)).thenReturn("encrypt_password");
        when(repository.save(any())).thenReturn(Optional.of(UserEntityFixture.get(userName, password)));

        Assertions.assertThrows(SnsApplicationException.class, () -> userService.join(userName, password));
    }

    @Test
    public void 로그인시_정상적으로_동작하는_경우 () {
        String userName = "himina";
        String password = "password";
        UserEntity userEntity = UserEntityFixture.get(userName, password);

        when(repository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(password, userEntity.getPassword())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.login(userName, password));
    }

    @Test
    public void 로그인시_userName으로_회원가입한_유저가_없는경우 () {
        String userName = "himina";
        String password = "password";

        when(repository.findByUserName(userName)).thenReturn(Optional.empty());

        SnsApplicationException exception = Assertions
                .assertThrows(SnsApplicationException.class,
                        () -> userService.login(userName, password));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void 로그인시_패스워드가_틀린_경우 () {
        String userName = "himina";
        String password = "password";
        String wrongPassword = "wrogPassword";

        when(repository.findByUserName(userName)).thenReturn(Optional.of(UserEntityFixture.get(userName, password)));
        when(passwordEncoder.matches(password, wrongPassword)).thenReturn(false);

        SnsApplicationException exception = Assertions.assertThrows(SnsApplicationException.class
                , () -> userService.login(userName, password));

        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }
}
