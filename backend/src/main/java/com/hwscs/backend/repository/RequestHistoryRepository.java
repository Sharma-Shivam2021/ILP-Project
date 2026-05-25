package com.hwscs.backend.repository;

import com.hwscs.backend.entity.RequestHistory;
import com.hwscs.backend.entity.ShiftRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestHistoryRepository extends JpaRepository<RequestHistory, Integer> {
    List<RequestHistory> findByShiftRequest(ShiftRequest shiftRequest);
}