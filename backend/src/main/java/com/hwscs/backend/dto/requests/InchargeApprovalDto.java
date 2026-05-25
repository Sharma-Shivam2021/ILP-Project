package com.hwscs.backend.dto.requests;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class InchargeApprovalDto {
    @NotNull(message = "Shift request ID is required")
    private Integer shiftRequestId;
    private boolean approved;
    private String remarks;

    public InchargeApprovalDto() {
    }

    public InchargeApprovalDto(@NotNull(message = "Shift request ID is required") Integer shiftRequestId,
                               boolean approved, String remarks) {
        super();
        this.shiftRequestId = shiftRequestId;
        this.approved = approved;
        this.remarks = remarks;
    }

    public Integer getShiftRequestId() {
        return shiftRequestId;
    }

    public void setShiftRequestId(Integer shiftRequestId) {
        this.shiftRequestId = shiftRequestId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "InchargeApprovalDto [shiftRequestId=" + shiftRequestId + ", approved=" + approved + ", remarks="
                + remarks + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(approved, remarks, shiftRequestId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InchargeApprovalDto other = (InchargeApprovalDto) obj;
        return approved == other.approved && Objects.equals(remarks, other.remarks)
                && Objects.equals(shiftRequestId, other.shiftRequestId);
    }

}