package com.service.todo_backend.model;

import jakarta.persistence.*;
import jdk.jfr.Relational;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ca_id", nullable = false, updatable = false)
    private Long categoryId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column()
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @Lazy
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    public Category() {
    }

    public Category(String title, String description,User user) {
        this.title = title;
        this.description = description;
        this.user = user;
    }

    public Category (String title,User user) {
        this.title = title;
        this.user = user;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
