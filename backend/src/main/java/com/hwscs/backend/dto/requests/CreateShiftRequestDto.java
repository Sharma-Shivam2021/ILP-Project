package com.hwscs.backend.dto.requests;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class CreateShiftRequestDto {

    @NotNull(message = "Requester shift ID is required")
    private Integer requesterShiftId;

    @NotNull(message = "Peer shift ID is required")
    private Integer peerShiftId;

    private String remarks;

    public CreateShiftRequestDto() {
    }

    public CreateShiftRequestDto(@NotNull(message = "Requester shift ID is required") Integer requesterShiftId,
                                 @NotNull(message = "Peer shift ID is required") Integer peerShiftId, String remarks) {
        super();
        this.requesterShiftId = requesterShiftId;
        this.peerShiftId = peerShiftId;
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "CreateShiftRequestDto  requesterShiftId=" + requesterShiftId
                + ", peerShiftId=" + peerShiftId + ", remarks=" + remarks + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(peerShiftId, remarks, requesterShiftId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CreateShiftRequestDto other = (CreateShiftRequestDto) obj;
        return Objects.equals(peerShiftId, other.peerShiftId)
                && Objects.equals(remarks, other.remarks) && Objects.equals(requesterShiftId, other.requesterShiftId);
    }

    public Integer getRequesterShiftId() {
        return requesterShiftId;
    }

    public void setRequesterShiftId(Integer requesterShiftId) {
        this.requesterShiftId = requesterShiftId;
    }

    public Integer getPeerShiftId() {
        return peerShiftId;
    }

    public void setPeerShiftId(Integer peerShiftId) {
        this.peerShiftId = peerShiftId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}