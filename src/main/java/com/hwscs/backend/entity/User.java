package com.hwscs.backend.entity;

import com.hwscs.backend.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private Boolean isActive;

    @Column(name = "first_login")
    private Boolean firstLogin;

    @Column(name = "failed_attempts")
    private Integer failedAttempts;

    @Column(name = "account_locked")
    private Boolean accountLocked;

    @Column(name = "lock_time")
    private LocalDateTime lockTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // ✅ Required by JPA
    public User() {
    }

    // ✅ Private constructor used by Builder
    private User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.password = builder.password;
        this.role = builder.role;
        this.department = builder.department;
        this.isActive = builder.isActive;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.failedAttempts = builder.failedAttempts;
        this.accountLocked = builder.accountLocked;
        this.lockTime = builder.lockTime;
    }

    // ✅ Manual Builder Implementation
    public static Builder builder() {
        return new Builder();
    }

    // ✅ JPA lifecycle hooks
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isActive == null) {
            this.isActive = true;
        }
        if (this.failedAttempts == null) {
            this.failedAttempts = 0;
        }
        if (this.accountLocked == null) {
            this.accountLocked = false;
        }
        if (this.firstLogin == null) {
            this.firstLogin = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ Getters & Setters

    public static class Builder {
        private Long id;
        private String username;
        private String password;
        private Role role;
        private Department department;
        private Boolean isActive;
        private Integer failedAttempts;
        private Boolean accountLocked;
        private LocalDateTime lockTime;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        @SuppressWarnings("unused")
        private Boolean firstLogin;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder department(Department department) {
            this.department = department;
            return this;
        }

        public Builder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder failedAttempts(Integer failedAttempts) {
            this.failedAttempts = failedAttempts;
            return this;
        }

        public Builder accountLocked(Boolean accountLocked) {
            this.accountLocked = accountLocked;
            return this;
        }

        public Builder lockTime(LocalDateTime lockTime) {
            this.lockTime = lockTime;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder firstLogin(Boolean firstLogin) {
            this.firstLogin = firstLogin;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}