package com.hwscs.backend.repository;

import com.hwscs.backend.entity.Department;
import com.hwscs.backend.entity.Nurse;
import com.hwscs.backend.entity.ShiftRequest;
import com.hwscs.backend.entity.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShiftRequestRepository extends JpaRepository<ShiftRequest, Integer> {

    List<ShiftRequest> findByRequesterNurse(Nurse nurse);

    List<ShiftRequest> findByPeerNurse(Nurse nurse);

    List<ShiftRequest> findByStatus(RequestStatus status);

    @Query("SELECT sr FROM ShiftRequest sr WHERE sr.requesterNurse = :nurse OR sr.peerNurse = :nurse")
    List<ShiftRequest> findAllInvolvingNurse(@Param("nurse") Nurse nurse);

    @Query("""
            SELECT sr FROM ShiftRequest sr
            WHERE sr.requesterNurse.department = :department
            AND sr.status = :status
            """)
    List<ShiftRequest> findByDepartmentAndStatus(
            @Param("department") Department department,
            @Param("status") RequestStatus status
    );

    @Query("""
            SELECT sr FROM ShiftRequest sr
            WHERE sr.requesterNurse.department = :department
            """)
    List<ShiftRequest> findByDepartment(@Param("department") Department department);
}
