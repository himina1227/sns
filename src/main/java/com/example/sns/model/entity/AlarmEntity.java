package com.example.sns.model.entity;

import com.example.sns.enums.AlarmType;
import com.example.sns.model.AlarmArgs;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "\"alarm\"")
@SQLDelete(sql = "UPDATE \"alarm\" SET removed_at = NOW() WHERE id=?")
@Where(clause = "delete_at is NULL")
@NoArgsConstructor
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

//    @Type(type = "json")
//    @Column(columnDefinition = "longtext")
//    private AlarmArgs args;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "removed_at")
    private Timestamp removedAt;


    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static AlarmEntity of(AlarmType alarmType, AlarmArgs args, UserEntity user) {
        AlarmEntity entity = new AlarmEntity();
        entity.setAlarmType(alarmType);
//        entity.setArgs(args);
        entity.setUser(user);
        return entity;
    }
}
