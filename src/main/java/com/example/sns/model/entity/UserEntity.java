package com.example.sns.model.entity;

import com.example.sns.model.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "\"user\"", indexes = @Index(name = "user_id_idx", columnList = "user_id"))
@SQLDelete(sql = "UPDATE \"user\" SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userName;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static UserEntity of(String userName, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        return userEntity;
    }

}
