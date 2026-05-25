package com.hwscs.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyAssignmentDto {
    private LocalDate date;
    private String dayOfWeek;
    private String shiftName; // "Morning", "Evening", "Night", or "OFF"
    private String shiftTime; // e.g. "08:00 - 16:00" or null
}
