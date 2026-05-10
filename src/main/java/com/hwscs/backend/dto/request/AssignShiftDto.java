package com.hwscs.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AssignShiftDto {

    @NotNull(message = "Nurse ID is required")
    private Integer nurseId;

    @NotNull(message = "Shift ID is required")
    private Integer shiftId;

    @NotNull(message = "Shift date is required")
    private LocalDate shiftDate;
}
