package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.Alarm;
import com.example.sns.model.User;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.AlarmEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserEntityRepository repository;
    private final AlarmEntityRepository alarmEntityReposiory;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public User join(String userNane, String password) {
        repository.findByUserName(userNane).ifPresent(
                it -> {
                    throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, "유저이름이 중복되었습니다.");
                }
        );

        UserEntity userEntity = repository.save(UserEntity.of(userNane, passwordEncoder.encode(password)));
        return User.fromEntity(userEntity);
    }

    // TODO: implement
    public String login(String userName, String password) {
        UserEntity userEntity = repository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s, not founded", userName)));

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
//        if (!userEntity.getPassword().equals(password)) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        return "";
    }

    public User loadUserByUsername(String userName) {
        return repository.findByUserName(userName).map(User::fromEntity).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    public Page<Alarm> alarmList(String userName, Pageable pageable) {
        UserEntity userEntity = repository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s, not founded", userName)));

        return alarmEntityReposiory.findAllByUser(userEntity, pageable).map(Alarm::fromEntity);
    }
}
