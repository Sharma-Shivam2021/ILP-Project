package com.hwscs.backend.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PeerResponseDto {

    @NotNull(message = "Shift request ID is required")
    private Integer shiftRequestId;

    private boolean accepted;

    private String remarks;
}
