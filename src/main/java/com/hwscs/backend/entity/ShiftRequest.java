package com.hwscs.backend.entity;

import com.hwscs.backend.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shift_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Nurse who initiated request
    @ManyToOne
    @JoinColumn(name = "requester_nurse_id", nullable = false)
    private Nurse requesterNurse;

    // Nurse receiving request
    @ManyToOne
    @JoinColumn(name = "peer_nurse_id", nullable = false)
    private Nurse peerNurse;

    // Requester's current shift
    @ManyToOne
    @JoinColumn(name = "requester_nurse_shift_id", nullable = false)
    private NurseShift requesterNurseShift;

    // Peer's current shift
    @ManyToOne
    @JoinColumn(name = "peer_nurse_shift_id", nullable = false)
    private NurseShift peerNurseShift;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = RequestStatus.PENDING_PEER;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}