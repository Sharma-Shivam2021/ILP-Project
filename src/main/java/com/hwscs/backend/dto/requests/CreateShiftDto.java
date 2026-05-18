package com.hwscs.backend.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.Objects;

public class CreateShiftDto {

    @NotBlank(message = "Shift name is required")
    private String shiftName;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    public CreateShiftDto(@NotBlank(message = "Shift name is required") String shiftName,
                          @NotNull(message = "Start time is required") LocalTime startTime,
                          @NotNull(message = "End time is required") LocalTime endTime) {
        super();
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public CreateShiftDto() {
        super();
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "CreateShiftDto [shiftName=" + shiftName + ", startTime=" + startTime + ", endTime=" + endTime + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(endTime, shiftName, startTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CreateShiftDto other = (CreateShiftDto) obj;
        return Objects.equals(endTime, other.endTime) && Objects.equals(shiftName, other.shiftName)
                && Objects.equals(startTime, other.startTime);
    }


}