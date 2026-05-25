package com.hwscs.backend.repository;

import com.hwscs.backend.entity.Department;
import com.hwscs.backend.entity.Nurse;
import com.hwscs.backend.entity.NurseShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NurseShiftRepository extends JpaRepository<NurseShift, Integer> {

    List<NurseShift> findByNurse(Nurse nurse);

    List<NurseShift> findByShiftDate(LocalDate shiftDate);

    Optional<NurseShift> findByNurseAndShiftDate(Nurse nurse, LocalDate shiftDate);

    @Query("SELECT ns FROM NurseShift ns WHERE ns.nurse.department = :department AND ns.shiftDate = :date")
    List<NurseShift> findByDepartmentAndDate(
            @Param("department") Department department,
            @Param("date") LocalDate date
    );

    @Query("SELECT ns FROM NurseShift ns WHERE ns.nurse.department = :department AND ns.shiftDate BETWEEN :from AND :to")
    List<NurseShift> findByDepartmentAndDateRange(
            @Param("department") Department department,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query("""
                SELECT ns
                FROM NurseShift ns
                WHERE ns.nurse.department = :department
                AND ns.shiftDate = :shiftDate
                AND ns.nurse.id <> :requesterNurseId
                AND ns.shift.id <> :requesterShiftTypeId
                AND ns.isSwapped = false
            """)
    List<NurseShift> findEligiblePeers(
            @Param("department") Department department,
            @Param("shiftDate") LocalDate shiftDate,
            @Param("requesterNurseId") Integer requesterNurseId,
            @Param("requesterShiftTypeId") Integer requesterShiftTypeId
    );

    long countByShiftDate(LocalDate shiftDate);
}
