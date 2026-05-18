package com.hwscs.backend.service.interfaces;

import com.hwscs.backend.dto.response.DutyOfficerDashboardDto;
import com.hwscs.backend.dto.response.InchargeDashboardDto;
import com.hwscs.backend.dto.response.NurseDashboardDto;

public interface DashboardService {
    NurseDashboardDto getNurseDashboard(String username);

    InchargeDashboardDto getInchargeDashboard(String username);

    DutyOfficerDashboardDto getDutyOfficerDashboard(String username);
}