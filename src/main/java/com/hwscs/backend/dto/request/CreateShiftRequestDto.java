package com.hwscs.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateShiftRequestDto {

    @NotNull(message = "Requester nurse ID is required")
    private Integer requesterNurseId;

    @NotNull(message = "Peer nurse ID is required")
    private Integer peerNurseId;

    @NotNull(message = "Requester shift ID is required")
    private Integer requesterShiftId;

    @NotNull(message = "Peer shift ID is required")
    private Integer peerShiftId;

    private String remarks;
}
