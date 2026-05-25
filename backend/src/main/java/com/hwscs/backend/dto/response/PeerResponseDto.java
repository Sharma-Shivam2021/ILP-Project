package com.hwscs.backend.dto.response;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class PeerResponseDto {
    @NotNull(message = "Shift request ID is required")
    private Integer shiftRequestId;
    private boolean accepted;
    private String remarks;

    public PeerResponseDto() {
    }

    public PeerResponseDto(@NotNull(message = "Shift request ID is required") Integer shiftRequestId, boolean accepted,
                           String remarks) {
        super();
        this.shiftRequestId = shiftRequestId;
        this.accepted = accepted;
        this.remarks = remarks;
    }

    // Private constructor for Builder
    private PeerResponseDto(Builder builder) {
        this.shiftRequestId = builder.shiftRequestId;
        this.accepted = builder.accepted;
        this.remarks = builder.remarks;
    }

    // Manual builder() method
    public static Builder builder() {
        return new Builder();
    }

    public Integer getShiftRequestId() {
        return shiftRequestId;
    }

    public void setShiftRequestId(Integer shiftRequestId) {
        this.shiftRequestId = shiftRequestId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "PeerResponseDto [shiftRequestId=" + shiftRequestId + ", accepted=" + accepted + ", remarks=" + remarks
                + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(accepted, remarks, shiftRequestId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PeerResponseDto other = (PeerResponseDto) obj;
        return accepted == other.accepted && Objects.equals(remarks, other.remarks)
                && Objects.equals(shiftRequestId, other.shiftRequestId);
    }

    // Builder implementation
    public static class Builder {
        private Integer shiftRequestId;
        private boolean accepted;
        private String remarks;

        public Builder shiftRequestId(Integer shiftRequestId) {
            this.shiftRequestId = shiftRequestId;
            return this;
        }

        public Builder accepted(boolean accepted) {
            this.accepted = accepted;
            return this;
        }

        public Builder remarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public PeerResponseDto build() {
            return new PeerResponseDto(this);
        }
    }

}