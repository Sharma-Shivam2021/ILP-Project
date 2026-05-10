package com.hwscs.backend.service;

import com.hwscs.backend.dto.request.CreateShiftRequestDto;
import com.hwscs.backend.dto.request.InchargeApprovalDto;
import com.hwscs.backend.dto.response.PeerResponseDto;
import com.hwscs.backend.dto.response.ShiftRequestResponseDto;

import java.util.List;

public interface ShiftRequestService {

    ShiftRequestResponseDto createRequest(CreateShiftRequestDto dto, String requesterUsername);

    ShiftRequestResponseDto peerResponse(PeerResponseDto dto, String peerUsername);

    ShiftRequestResponseDto inchargeReview(InchargeApprovalDto dto, String inchargeUsername);

    // Nurse: view all requests they are involved in
    List<ShiftRequestResponseDto> getMyRequests(String nurseUsername);

    // Nursing Incharge: view all PEER_ACCEPTED requests in their department
    List<ShiftRequestResponseDto> getPendingInchargeReview(String inchargeUsername);

    // View a single request by ID
    ShiftRequestResponseDto getRequestById(Integer requestId);
}
