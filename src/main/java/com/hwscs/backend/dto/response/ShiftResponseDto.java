package com.hwscs.backend.dto.response;

import java.time.LocalTime;
import java.util.Objects;

public class ShiftResponseDto {

    private Integer id;
    private String shiftName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean active;

    // Private constructor for Builder
    private ShiftResponseDto(Builder builder) {
        this.id = builder.id;
        this.shiftName = builder.shiftName;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.active = builder.active;
    }

    public ShiftResponseDto() {
        // default constructor
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "ShiftResponseDto [id=" + id + ", shiftName=" + shiftName + ", startTime=" + startTime + ", endTime="
                + endTime + ", active=" + active + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, endTime, id, shiftName, startTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShiftResponseDto other = (ShiftResponseDto) obj;
        return Objects.equals(active, other.active) &&
                Objects.equals(endTime, other.endTime) &&
                Objects.equals(id, other.id) &&
                Objects.equals(shiftName, other.shiftName) &&
                Objects.equals(startTime, other.startTime);
    }

    // Builder Class
    public static class Builder {
        private Integer id;
        private String shiftName;
        private LocalTime startTime;
        private LocalTime endTime;
        private Boolean active;

        public Builder id(Integer id) {
            this.id = id;
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

        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        public ShiftResponseDto build() {
            return new ShiftResponseDto(this);
        }
    }
}