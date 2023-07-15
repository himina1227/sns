package com.example.sns.fixture;

import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;

public class PostEntityFixture {

    public static PostEntity get(String userName, Integer postId, Integer userId) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUserName(userName);
        PostEntity postEntity = new PostEntity();
        postEntity.setId(1);
        postEntity.setUser(user);
        postEntity.setTitle("title");
        postEntity.setBody("body");
        return postEntity;
    }
}
