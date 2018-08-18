package com.github.graalogosh.DemoApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Anton Mukovozov (graalogosh@gmail.com) on 18.08.2018.
 */
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    @Getter
    private Long id;

    @JsonIgnore
    @Getter
    private String uuid;

    @Enumerated(EnumType.STRING)
    @Getter
    private TaskStatus status;

    @Getter
    private Date timestamp;

    public Task() {
        uuid = java.util.UUID.randomUUID().toString();
        setStatus(TaskStatus.CREATED);
    }

    public void setStatus(@NonNull TaskStatus status) {
        this.status = status;
        this.timestamp = new Date();
    }
}
