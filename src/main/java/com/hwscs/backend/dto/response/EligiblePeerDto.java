package com.hwscs.backend.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class EligiblePeerDto {
    private Integer nurseId;
    private String nurseName;
    private Integer nurseShiftId;
    private String shiftName;
    private LocalTime shiftStart;
    private LocalTime shiftEnd;
    private LocalDate shiftDate;

    public EligiblePeerDto() {
    }

    public EligiblePeerDto(Integer nurseId, String nurseName, Integer nurseShiftId, String shiftName,
                           LocalTime shiftStart, LocalTime shiftEnd, LocalDate shiftDate) {
        super();
        this.nurseId = nurseId;
        this.nurseName = nurseName;
        this.nurseShiftId = nurseShiftId;
        this.shiftName = shiftName;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
        this.shiftDate = shiftDate;
    }

    // Private constructor for Builder
    private EligiblePeerDto(Builder builder) {
        this.nurseId = builder.nurseId;
        this.nurseName = builder.nurseName;
        this.nurseShiftId = builder.nurseShiftId;
        this.shiftName = builder.shiftName;
        this.shiftStart = builder.shiftStart;
        this.shiftEnd = builder.shiftEnd;
        this.shiftDate = builder.shiftDate;
    }

    // Manual builder() method
    public static Builder builder() {
        return new Builder();
    }

    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public Integer getNurseShiftId() {
        return nurseShiftId;
    }

    public void setNurseShiftId(Integer nurseShiftId) {
        this.nurseShiftId = nurseShiftId;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public LocalTime getShiftStart() {
        return shiftStart;
    }

    public void setShiftStart(LocalTime shiftStart) {
        this.shiftStart = shiftStart;
    }

    public LocalTime getShiftEnd() {
        return shiftEnd;
    }

    public void setShiftEnd(LocalTime shiftEnd) {
        this.shiftEnd = shiftEnd;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nurseId, nurseName, nurseShiftId, shiftDate, shiftEnd, shiftName, shiftStart);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EligiblePeerDto other = (EligiblePeerDto) obj;
        return Objects.equals(nurseId, other.nurseId) && Objects.equals(nurseName, other.nurseName)
                && Objects.equals(nurseShiftId, other.nurseShiftId) && Objects.equals(shiftDate, other.shiftDate)
                && Objects.equals(shiftEnd, other.shiftEnd) && Objects.equals(shiftName, other.shiftName)
                && Objects.equals(shiftStart, other.shiftStart);
    }

    @Override
    public String toString() {
        return "EligiblePeerDto [nurseId=" + nurseId + ", nurseName=" + nurseName + ", nurseShiftId=" + nurseShiftId
                + ", shiftName=" + shiftName + ", shiftStart=" + shiftStart + ", shiftEnd=" + shiftEnd + ", shiftDate="
                + shiftDate + "]";
    }

    // Builder implementation
    public static class Builder {
        private Integer nurseId;
        private String nurseName;
        private Integer nurseShiftId;
        private String shiftName;
        private LocalTime shiftStart;
        private LocalTime shiftEnd;
        private LocalDate shiftDate;

        public Builder nurseId(Integer nurseId) {
            this.nurseId = nurseId;
            return this;
        }

        public Builder nurseName(String nurseName) {
            this.nurseName = nurseName;
            return this;
        }

        public Builder nurseShiftId(Integer nurseShiftId) {
            this.nurseShiftId = nurseShiftId;
            return this;
        }

        public Builder shiftName(String shiftName) {
            this.shiftName = shiftName;
            return this;
        }

        public Builder shiftStart(LocalTime shiftStart) {
            this.shiftStart = shiftStart;
            return this;
        }

        public Builder shiftEnd(LocalTime shiftEnd) {
            this.shiftEnd = shiftEnd;
            return this;
        }

        public Builder shiftDate(LocalDate shiftDate) {
            this.shiftDate = shiftDate;
            return this;
        }

        public EligiblePeerDto build() {
            return new EligiblePeerDto(this);
        }
    }
}