package com.hwscs.backend.entity;

import com.hwscs.backend.entity.enums.RequestAction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "shift_request_id", nullable = false)
    private ShiftRequest shiftRequest;

    @ManyToOne
    @JoinColumn(name = "actor_user_id", nullable = false)
    private User actorUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestAction action;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private LocalDateTime actedAt;

    @PrePersist
    public void prePersist() {
        actedAt = LocalDateTime.now();
    }
}