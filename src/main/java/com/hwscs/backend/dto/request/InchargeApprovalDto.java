package com.hwscs.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InchargeApprovalDto {

    @NotNull(message = "Shift request ID is required")
    private Integer shiftRequestId;

    private boolean approved;

    private String remarks;
}
