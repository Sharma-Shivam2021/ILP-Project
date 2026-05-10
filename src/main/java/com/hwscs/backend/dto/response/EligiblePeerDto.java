package com.hwscs.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligiblePeerDto {

    private Integer nurseId;
    private String nurseName;

    private Integer nurseShiftId;

    private String shiftName;

    private LocalTime shiftStart;
    private LocalTime shiftEnd;

    private LocalDate shiftDate;
}