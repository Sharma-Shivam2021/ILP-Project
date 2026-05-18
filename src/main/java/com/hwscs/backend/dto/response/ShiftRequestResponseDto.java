package com.hwscs.backend.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

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

    public ShiftRequestResponseDto() {
    }

    public ShiftRequestResponseDto(Integer id, String status, String remarks, LocalDateTime createdAt,
                                   Integer requesterNurseId, String requesterNurseName, LocalDate requesterShiftDate,
                                   String requesterShiftName, LocalTime requesterShiftStart, LocalTime requesterShiftEnd, Integer peerNurseId,
                                   String peerNurseName, LocalDate peerShiftDate, String peerShiftName, LocalTime peerShiftStart,
                                   LocalTime peerShiftEnd) {
        super();
        this.id = id;
        this.status = status;
        this.remarks = remarks;
        this.createdAt = createdAt;
        this.requesterNurseId = requesterNurseId;
        this.requesterNurseName = requesterNurseName;
        this.requesterShiftDate = requesterShiftDate;
        this.requesterShiftName = requesterShiftName;
        this.requesterShiftStart = requesterShiftStart;
        this.requesterShiftEnd = requesterShiftEnd;
        this.peerNurseId = peerNurseId;
        this.peerNurseName = peerNurseName;
        this.peerShiftDate = peerShiftDate;
        this.peerShiftName = peerShiftName;
        this.peerShiftStart = peerShiftStart;
        this.peerShiftEnd = peerShiftEnd;
    }

    // Private constructor for Builder
    private ShiftRequestResponseDto(Builder builder) {
        this.id = builder.id;
        this.status = builder.status;
        this.remarks = builder.remarks;
        this.createdAt = builder.createdAt;

        this.requesterNurseId = builder.requesterNurseId;
        this.requesterNurseName = builder.requesterNurseName;
        this.requesterShiftDate = builder.requesterShiftDate;
        this.requesterShiftName = builder.requesterShiftName;
        this.requesterShiftStart = builder.requesterShiftStart;
        this.requesterShiftEnd = builder.requesterShiftEnd;

        this.peerNurseId = builder.peerNurseId;
        this.peerNurseName = builder.peerNurseName;
        this.peerShiftDate = builder.peerShiftDate;
        this.peerShiftName = builder.peerShiftName;
        this.peerShiftStart = builder.peerShiftStart;
        this.peerShiftEnd = builder.peerShiftEnd;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getRequesterNurseId() {
        return requesterNurseId;
    }

    public void setRequesterNurseId(Integer requesterNurseId) {
        this.requesterNurseId = requesterNurseId;
    }

    public String getRequesterNurseName() {
        return requesterNurseName;
    }

    public void setRequesterNurseName(String requesterNurseName) {
        this.requesterNurseName = requesterNurseName;
    }

    public LocalDate getRequesterShiftDate() {
        return requesterShiftDate;
    }

    public void setRequesterShiftDate(LocalDate requesterShiftDate) {
        this.requesterShiftDate = requesterShiftDate;
    }

    public String getRequesterShiftName() {
        return requesterShiftName;
    }

    public void setRequesterShiftName(String requesterShiftName) {
        this.requesterShiftName = requesterShiftName;
    }

    public LocalTime getRequesterShiftStart() {
        return requesterShiftStart;
    }

    public void setRequesterShiftStart(LocalTime requesterShiftStart) {
        this.requesterShiftStart = requesterShiftStart;
    }

    public LocalTime getRequesterShiftEnd() {
        return requesterShiftEnd;
    }

    public void setRequesterShiftEnd(LocalTime requesterShiftEnd) {
        this.requesterShiftEnd = requesterShiftEnd;
    }

    public Integer getPeerNurseId() {
        return peerNurseId;
    }

    public void setPeerNurseId(Integer peerNurseId) {
        this.peerNurseId = peerNurseId;
    }

    public String getPeerNurseName() {
        return peerNurseName;
    }

    public void setPeerNurseName(String peerNurseName) {
        this.peerNurseName = peerNurseName;
    }

    public LocalDate getPeerShiftDate() {
        return peerShiftDate;
    }

    public void setPeerShiftDate(LocalDate peerShiftDate) {
        this.peerShiftDate = peerShiftDate;
    }

    public String getPeerShiftName() {
        return peerShiftName;
    }

    public void setPeerShiftName(String peerShiftName) {
        this.peerShiftName = peerShiftName;
    }

    public LocalTime getPeerShiftStart() {
        return peerShiftStart;
    }

    public void setPeerShiftStart(LocalTime peerShiftStart) {
        this.peerShiftStart = peerShiftStart;
    }

    public LocalTime getPeerShiftEnd() {
        return peerShiftEnd;
    }

    public void setPeerShiftEnd(LocalTime peerShiftEnd) {
        this.peerShiftEnd = peerShiftEnd;
    }

    @Override
    public String toString() {
        return "ShiftRequestResponseDto [id=" + id + ", status=" + status + ", remarks=" + remarks + ", createdAt="
                + createdAt + ", requesterNurseId=" + requesterNurseId + ", requesterNurseName=" + requesterNurseName
                + ", requesterShiftDate=" + requesterShiftDate + ", requesterShiftName=" + requesterShiftName
                + ", requesterShiftStart=" + requesterShiftStart + ", requesterShiftEnd=" + requesterShiftEnd
                + ", peerNurseId=" + peerNurseId + ", peerNurseName=" + peerNurseName + ", peerShiftDate="
                + peerShiftDate + ", peerShiftName=" + peerShiftName + ", peerShiftStart=" + peerShiftStart
                + ", peerShiftEnd=" + peerShiftEnd + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, id, peerNurseId, peerNurseName, peerShiftDate, peerShiftEnd, peerShiftName,
                peerShiftStart, remarks, requesterNurseId, requesterNurseName, requesterShiftDate, requesterShiftEnd,
                requesterShiftName, requesterShiftStart, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShiftRequestResponseDto other = (ShiftRequestResponseDto) obj;
        return Objects.equals(createdAt, other.createdAt) && Objects.equals(id, other.id)
                && Objects.equals(peerNurseId, other.peerNurseId) && Objects.equals(peerNurseName, other.peerNurseName)
                && Objects.equals(peerShiftDate, other.peerShiftDate)
                && Objects.equals(peerShiftEnd, other.peerShiftEnd)
                && Objects.equals(peerShiftName, other.peerShiftName)
                && Objects.equals(peerShiftStart, other.peerShiftStart) && Objects.equals(remarks, other.remarks)
                && Objects.equals(requesterNurseId, other.requesterNurseId)
                && Objects.equals(requesterNurseName, other.requesterNurseName)
                && Objects.equals(requesterShiftDate, other.requesterShiftDate)
                && Objects.equals(requesterShiftEnd, other.requesterShiftEnd)
                && Objects.equals(requesterShiftName, other.requesterShiftName)
                && Objects.equals(requesterShiftStart, other.requesterShiftStart)
                && Objects.equals(status, other.status);
    }

    // Builder implementation
    public static class Builder {
        private Integer id;
        private String status;
        private String remarks;
        private LocalDateTime createdAt;

        private Integer requesterNurseId;
        private String requesterNurseName;
        private LocalDate requesterShiftDate;
        private String requesterShiftName;
        private LocalTime requesterShiftStart;
        private LocalTime requesterShiftEnd;

        private Integer peerNurseId;
        private String peerNurseName;
        private LocalDate peerShiftDate;
        private String peerShiftName;
        private LocalTime peerShiftStart;
        private LocalTime peerShiftEnd;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder remarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder requesterNurseId(Integer requesterNurseId) {
            this.requesterNurseId = requesterNurseId;
            return this;
        }

        public Builder requesterNurseName(String requesterNurseName) {
            this.requesterNurseName = requesterNurseName;
            return this;
        }

        public Builder requesterShiftDate(LocalDate requesterShiftDate) {
            this.requesterShiftDate = requesterShiftDate;
            return this;
        }

        public Builder requesterShiftName(String requesterShiftName) {
            this.requesterShiftName = requesterShiftName;
            return this;
        }

        public Builder requesterShiftStart(LocalTime requesterShiftStart) {
            this.requesterShiftStart = requesterShiftStart;
            return this;
        }

        public Builder requesterShiftEnd(LocalTime requesterShiftEnd) {
            this.requesterShiftEnd = requesterShiftEnd;
            return this;
        }

        public Builder peerNurseId(Integer peerNurseId) {
            this.peerNurseId = peerNurseId;
            return this;
        }

        public Builder peerNurseName(String peerNurseName) {
            this.peerNurseName = peerNurseName;
            return this;
        }

        public Builder peerShiftDate(LocalDate peerShiftDate) {
            this.peerShiftDate = peerShiftDate;
            return this;
        }

        public Builder peerShiftName(String peerShiftName) {
            this.peerShiftName = peerShiftName;
            return this;
        }

        public Builder peerShiftStart(LocalTime peerShiftStart) {
            this.peerShiftStart = peerShiftStart;
            return this;
        }

        public Builder peerShiftEnd(LocalTime peerShiftEnd) {
            this.peerShiftEnd = peerShiftEnd;
            return this;
        }

        public ShiftRequestResponseDto build() {
            return new ShiftRequestResponseDto(this);
        }
    }

}