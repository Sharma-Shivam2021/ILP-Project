package com.hwscs.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DutyOfficerDashboardDto {

    private long totalDepartments;

    private long totalNurses;

    private long totalAssignedToday;

    private long totalPendingPeerRequests;

    private long totalPendingInchargeRequests;

    private long totalApprovedRequests;
}