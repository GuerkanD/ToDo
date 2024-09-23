package com.service.todo_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ca_id", nullable = false, updatable = false)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column()
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @Lazy
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    public Category() {
    }

    public Category(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public Category (String title,User user) {
        this.title = title;
        this.user = user;
    }

    public Long getCategoryId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
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

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
