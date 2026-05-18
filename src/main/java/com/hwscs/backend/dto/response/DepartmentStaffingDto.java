package com.hwscs.backend.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DepartmentStaffingDto {

    private String departmentName;
    private LocalDate date;
    private int totalNurse;
    private int assignedNurses;
    private int unassignedNurses;
    private List<NurseShiftResponseDto> assignments;

    public DepartmentStaffingDto() {
    }

    public DepartmentStaffingDto(String departmentName, LocalDate date, int totalNurse, int assignedNurses,
                                 int unassignedNurses, List<NurseShiftResponseDto> assignments) {
        super();
        this.departmentName = departmentName;
        this.date = date;
        this.totalNurse = totalNurse;
        this.assignedNurses = assignedNurses;
        this.unassignedNurses = unassignedNurses;
        this.assignments = assignments;
    }

    // Private constructor for Builder
    private DepartmentStaffingDto(Builder builder) {
        this.departmentName = builder.departmentName;
        this.date = builder.date;
        this.totalNurse = builder.totalNurse;
        this.assignedNurses = builder.assignedNurses;
        this.unassignedNurses = builder.unassignedNurses;
        this.assignments = builder.assignments;
    }

    // Manual builder() method
    public static Builder builder() {
        return new Builder();
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getTotalNurse() {
        return totalNurse;
    }

    public void setTotalNurse(int totalNurse) {
        this.totalNurse = totalNurse;
    }

    public int getAssignedNurses() {
        return assignedNurses;
    }

    public void setAssignedNurses(int assignedNurses) {
        this.assignedNurses = assignedNurses;
    }

    public int getUnassignedNurses() {
        return unassignedNurses;
    }

    public void setUnassignedNurses(int unassignedNurses) {
        this.unassignedNurses = unassignedNurses;
    }

    public List<NurseShiftResponseDto> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<NurseShiftResponseDto> assignments) {
        this.assignments = assignments;
    }

    @Override
    public String toString() {
        return "DepartmentStaffingDto [departmentName=" + departmentName + ", date=" + date + ", totalNurse="
                + totalNurse + ", assignedNurses=" + assignedNurses + ", unassignedNurses=" + unassignedNurses
                + ", assignments=" + assignments + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignedNurses, assignments, date, departmentName, totalNurse, unassignedNurses);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DepartmentStaffingDto other = (DepartmentStaffingDto) obj;
        return assignedNurses == other.assignedNurses && Objects.equals(assignments, other.assignments)
                && Objects.equals(date, other.date) && Objects.equals(departmentName, other.departmentName)
                && totalNurse == other.totalNurse && unassignedNurses == other.unassignedNurses;
    }

    // Builder implementation
    public static class Builder {
        private String departmentName;
        private LocalDate date;
        private int totalNurse;
        private int assignedNurses;
        private int unassignedNurses;
        private List<NurseShiftResponseDto> assignments;

        public Builder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder totalNurse(int totalNurse) {
            this.totalNurse = totalNurse;
            return this;
        }

        public Builder assignedNurses(int assignedNurses) {
            this.assignedNurses = assignedNurses;
            return this;
        }

        public Builder unassignedNurses(int unassignedNurses) {
            this.unassignedNurses = unassignedNurses;
            return this;
        }

        public Builder assignments(List<NurseShiftResponseDto> assignments) {
            this.assignments = assignments;
            return this;
        }

        public DepartmentStaffingDto build() {
            return new DepartmentStaffingDto(this);
        }
    }

}