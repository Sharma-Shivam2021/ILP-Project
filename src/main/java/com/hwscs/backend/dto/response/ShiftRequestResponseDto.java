package com.hwscs.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftRequestResponseDto {

    private Integer id;
    private String status;
    private String remarks;
    private LocalDateTime createdAt;

    // Requester
    private Integer requesterNurseId;
    private String requesterNurseName;
    private LocalDate requesterShiftDate;
    private String requesterShiftName;
    private LocalTime requesterShiftStart;
    private LocalTime requesterShiftEnd;

    // Peer
    private Integer peerNurseId;
    private String peerNurseName;
    private LocalDate peerShiftDate;
    private String peerShiftName;
    private LocalTime peerShiftStart;
    private LocalTime peerShiftEnd;
}
