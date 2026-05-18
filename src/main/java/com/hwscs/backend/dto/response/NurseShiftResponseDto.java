package com.hwscs.backend.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class NurseShiftResponseDto {
    private Integer id;
    private Integer nurseId;
    private String nurseFullName;
    private LocalDate shiftDate;
    private String shiftName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isSwapped;

    public NurseShiftResponseDto() {
    }

    public NurseShiftResponseDto(Integer id, Integer nurseId, String nurseFullName, LocalDate shiftDate,
                                 String shiftName, LocalTime startTime, LocalTime endTime, Boolean isSwapped) {
        super();
        this.id = id;
        this.nurseId = nurseId;
        this.nurseFullName = nurseFullName;
        this.shiftDate = shiftDate;
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isSwapped = isSwapped;
    }

    // Private constructor for Builder
    private NurseShiftResponseDto(Builder builder) {
        this.id = builder.id;
        this.nurseId = builder.nurseId;
        this.nurseFullName = builder.nurseFullName;
        this.shiftDate = builder.shiftDate;
        this.shiftName = builder.shiftName;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.isSwapped = builder.isSwapped;
    }

    // Manual builder() method
    public static Builder builder() {
        return new Builder();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
    }

    public String getNurseFullName() {
        return nurseFullName;
    }

    public void setNurseFullName(String nurseFullName) {
        this.nurseFullName = nurseFullName;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
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

    public Boolean getIsSwapped() {
        return isSwapped;
    }

    public void setIsSwapped(Boolean isSwapped) {
        this.isSwapped = isSwapped;
    }

    @Override
    public String toString() {
        return "NurseShiftResponseDto [id=" + id + ", nurseId=" + nurseId + ", nurseFullName=" + nurseFullName
                + ", shiftDate=" + shiftDate + ", shiftName=" + shiftName + ", startTime=" + startTime + ", endTime="
                + endTime + ", isSwapped=" + isSwapped + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(endTime, id, isSwapped, nurseFullName, nurseId, shiftDate, shiftName, startTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NurseShiftResponseDto other = (NurseShiftResponseDto) obj;
        return Objects.equals(endTime, other.endTime) && Objects.equals(id, other.id)
                && Objects.equals(isSwapped, other.isSwapped) && Objects.equals(nurseFullName, other.nurseFullName)
                && Objects.equals(nurseId, other.nurseId) && Objects.equals(shiftDate, other.shiftDate)
                && Objects.equals(shiftName, other.shiftName) && Objects.equals(startTime, other.startTime);
    }

    // Builder implementation
    public static class Builder {
        private Integer id;
        private Integer nurseId;
        private String nurseFullName;
        private LocalDate shiftDate;
        private String shiftName;
        private LocalTime startTime;
        private LocalTime endTime;
        private Boolean isSwapped;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder nurseId(Integer nurseId) {
            this.nurseId = nurseId;
            return this;
        }

        public Builder nurseFullName(String nurseFullName) {
            this.nurseFullName = nurseFullName;
            return this;
        }

        public Builder shiftDate(LocalDate shiftDate) {
            this.shiftDate = shiftDate;
            return this;
        }

        public Builder shiftName(String shiftName) {
            this.shiftName = shiftName;
            return this;
        }

        public Builder startTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder isSwapped(Boolean isSwapped) {
            this.isSwapped = isSwapped;
            return this;
        }

        public NurseShiftResponseDto build() {
            return new NurseShiftResponseDto(this);
        }
    }
}