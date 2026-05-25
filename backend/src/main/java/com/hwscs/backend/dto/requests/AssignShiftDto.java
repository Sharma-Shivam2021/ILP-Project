package com.hwscs.backend.dto.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

public class AssignShiftDto {
    @NotNull(message = "Nurse ID is required")
    private Integer nurseId;

    @NotNull(message = "Shift ID is required")
    private Integer shiftId;

    @NotNull(message = "Shift date is required")
    private LocalDate shiftDate;

    public AssignShiftDto() {
    }

    public AssignShiftDto(@NotNull(message = "Nurse ID is requires") Integer nurseId,
                          @NotNull(message = "Shift ID is required") Integer shiftId,
                          @NotNull(message = "Shift date is required") LocalDate shiftDate) {
        super();
        this.nurseId = nurseId;
        this.shiftId = shiftId;
        this.shiftDate = shiftDate;
    }

    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
    }

    public Integer getShiftId() {
        return shiftId;
    }

    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nurseId, shiftDate, shiftId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AssignShiftDto other = (AssignShiftDto) obj;
        return Objects.equals(nurseId, other.nurseId) && Objects.equals(shiftDate, other.shiftDate)
                && Objects.equals(shiftId, other.shiftId);
    }

    @Override
    public String toString() {
        return "AssignShiftDto [nurseId=" + nurseId + ", shiftId=" + shiftId + ", shiftDate=" + shiftDate + "]";
    }

}