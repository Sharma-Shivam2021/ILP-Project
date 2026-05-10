package com.hwscs.backend.service.interfaces;

import com.hwscs.backend.dto.response.NurseResponseDto;
import com.hwscs.backend.dto.response.NurseShiftResponseDto;

import java.util.List;

public interface NurseService {

    // Nurse views their own profile
    NurseResponseDto getMyProfile(String username);

    // Nurse views their own upcoming/all shifts
    List<NurseShiftResponseDto> getMyShifts(String username);

    // Incharge/DutyOfficer: list all nurses in a department
    List<NurseResponseDto> getNursesByDepartment(Integer departmentId);

    // View a single nurse by ID (Incharge/DutyOfficer)
    NurseResponseDto getNurseById(Integer nurseId);
}
