package com.hwscs.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action; // e.g. PASSWORD_CHANGE, SHIFT_APPROVAL, USER_CREATION, USER_DELETION, PERMISSION_CHANGE

    @Column(nullable = false)
    private String performedBy;

    private String targetUser;

    @Column(length = 1000)
    private String details;

    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }
}
