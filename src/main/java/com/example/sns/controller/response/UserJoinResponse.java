package com.example.sns.controller.response;

import com.example.sns.model.User;
import com.example.sns.model.UserRole;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserJoinResponse {
    private Integer id;

    private String userName;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUsername()
        );
    }



}
