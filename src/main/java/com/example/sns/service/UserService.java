package com.example.sns.service;

import com.example.sns.entity.User;
import com.example.sns.entity.UserEntity;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserEntityRepository repository;

    public User join(String userNane, String password) {
        return null;
    }

    // TODO: implement
    public String login(String userName, String password) {
        UserEntity userEntity = repository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException());

        if (!userEntity.getPassword().equals(password)) {
            throw new SnsApplicationException();
        }
        return "";
    }
}
