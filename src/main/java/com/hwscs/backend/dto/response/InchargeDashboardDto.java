package com.hwscs.backend.dto.response;

import java.util.List;
import java.util.Objects;

public class InchargeDashboardDto {
    private String departmentName;
    private int totalNurses;
    private int assignedToday;
    private int unassignedToday;
    private int pendingApprovals;
    private int activeSwapRequests;
    private List<NurseShiftResponseDto> todayAssignments;

    public InchargeDashboardDto() {
        super();
    }

    public InchargeDashboardDto(String departmentName, int totalNurses, int assignedToday, int unassignedToday,
                                int pendingApprovals, int activeSwapRequests, List<NurseShiftResponseDto> todayAssignments) {
        super();
        this.departmentName = departmentName;
        this.totalNurses = totalNurses;
        this.assignedToday = assignedToday;
        this.unassignedToday = unassignedToday;
        this.pendingApprovals = pendingApprovals;
        this.activeSwapRequests = activeSwapRequests;
        this.todayAssignments = todayAssignments;
    }

    private InchargeDashboardDto(Builder builder) {
        this.departmentName = builder.departmentName;
        this.totalNurses = builder.totalNurses;
        this.assignedToday = builder.assignedToday;
        this.unassignedToday = builder.unassignedToday;
        this.pendingApprovals = builder.pendingApprovals;
        this.activeSwapRequests = builder.activeSwapRequests;
        this.todayAssignments = builder.todayAssignments;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "InchargeDashboardDto [departmentName=" + departmentName + ", totalNurses=" + totalNurses
                + ", assignedToday=" + assignedToday + ", unassignedToday=" + unassignedToday + ", pendingApprovals="
                + pendingApprovals + ", activeSwapRequests=" + activeSwapRequests + ", todayAssignments="
                + todayAssignments + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeSwapRequests, assignedToday, departmentName, pendingApprovals, todayAssignments,
                totalNurses, unassignedToday);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InchargeDashboardDto other = (InchargeDashboardDto) obj;
        return activeSwapRequests == other.activeSwapRequests && assignedToday == other.assignedToday
                && Objects.equals(departmentName, other.departmentName) && pendingApprovals == other.pendingApprovals
                && Objects.equals(todayAssignments, other.todayAssignments) && totalNurses == other.totalNurses
                && unassignedToday == other.unassignedToday;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getTotalNurses() {
        return totalNurses;
    }

    public void setTotalNurses(int totalNurses) {
        this.totalNurses = totalNurses;
    }

    public int getAssignedToday() {
        return assignedToday;
    }

    public void setAssignedToday(int assignedToday) {
        this.assignedToday = assignedToday;
    }

    public int getUnassignedToday() {
        return unassignedToday;
    }

    public void setUnassignedToday(int unassignedToday) {
        this.unassignedToday = unassignedToday;
    }

    public int getPendingApprovals() {
        return pendingApprovals;
    }

    public void setPendingApprovals(int pendingApprovals) {
        this.pendingApprovals = pendingApprovals;
    }

    public int getActiveSwapRequests() {
        return activeSwapRequests;
    }

    public void setActiveSwapRequests(int activeSwapRequests) {
        this.activeSwapRequests = activeSwapRequests;
    }

    public List<NurseShiftResponseDto> getTodayAssignments() {
        return todayAssignments;
    }

    public void setTodayAssignments(List<NurseShiftResponseDto> todayAssignments) {
        this.todayAssignments = todayAssignments;
    }

    public static class Builder {
        private String departmentName;
        private int totalNurses;
        private int assignedToday;
        private int unassignedToday;
        private int pendingApprovals;
        private int activeSwapRequests;
        private List<NurseShiftResponseDto> todayAssignments;

        public Builder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public Builder totalNurses(int totalNurses) {
            this.totalNurses = totalNurses;
            return this;
        }

        public Builder assignedToday(int assignedToday) {
            this.assignedToday = assignedToday;
            return this;
        }

        public Builder unassignedToday(int unassignedToday) {
            this.unassignedToday = unassignedToday;
            return this;
        }

        public Builder pendingApprovals(int pendingApprovals) {
            this.pendingApprovals = pendingApprovals;
            return this;
        }

        public Builder activeSwapRequests(int activeSwapRequests) {
            this.activeSwapRequests = activeSwapRequests;
            return this;
        }

        public Builder todayAssignments(List<NurseShiftResponseDto> todayAssignments) {
            this.todayAssignments = todayAssignments;
            return this;
        }

        public InchargeDashboardDto build() {
            return new InchargeDashboardDto(this);
        }

    }
}