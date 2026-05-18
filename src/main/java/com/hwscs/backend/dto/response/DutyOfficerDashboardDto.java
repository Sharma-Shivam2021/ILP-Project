package com.hwscs.backend.dto.response;


public class DutyOfficerDashboardDto {
    private String departmentName;
    private long totalNurses;
    private long totalAssignedToday;
    private long totalUnassignedToday;
    private long totalPendingPeerRequests;
    private long totalPendingInchargeRequests;
    private long totalApprovedRequests;

    public DutyOfficerDashboardDto() {
        super();
    }

    public DutyOfficerDashboardDto(String departmentName, long totalNurses, long totalAssignedToday,
                                   long totalUnassignedToday, long totalPendingPeerRequests, long totalPendingInchargeRequests,
                                   long totalApprovedRequests) {
        super();
        this.departmentName = departmentName;
        this.totalNurses = totalNurses;
        this.totalAssignedToday = totalAssignedToday;
        this.totalUnassignedToday = totalUnassignedToday;
        this.totalPendingPeerRequests = totalPendingPeerRequests;
        this.totalPendingInchargeRequests = totalPendingInchargeRequests;
        this.totalApprovedRequests = totalApprovedRequests;
    }

    private DutyOfficerDashboardDto(Builder builder) {
        this.departmentName = builder.departmentName;
        this.totalNurses = builder.totalNurses;
        this.totalAssignedToday = builder.totalAssignedToday;
        this.totalUnassignedToday = builder.totalUnassignedToday;
        this.totalPendingPeerRequests = builder.totalPendingPeerRequests;
        this.totalPendingInchargeRequests = builder.totalPendingInchargeRequests;
        this.totalApprovedRequests = builder.totalApprovedRequests;
    } // Builder entry method

    public static Builder builder() {
        return new Builder();
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public long getTotalNurses() {
        return totalNurses;
    }

    public void setTotalNurses(long totalNurses) {
        this.totalNurses = totalNurses;
    }

    public long getTotalAssignedToday() {
        return totalAssignedToday;
    }

    public void setTotalAssignedToday(long totalAssignedToday) {
        this.totalAssignedToday = totalAssignedToday;
    }

    public long getTotalUnassignedToday() {
        return totalUnassignedToday;
    }

    public void setTotalUnassignedToday(long totalUnassignedToday) {
        this.totalUnassignedToday = totalUnassignedToday;
    }

    public long getTotalPendingPeerRequests() {
        return totalPendingPeerRequests;
    }

    public void setTotalPendingPeerRequests(long totalPendingPeerRequests) {
        this.totalPendingPeerRequests = totalPendingPeerRequests;
    }

    public long getTotalPendingInchargeRequests() {
        return totalPendingInchargeRequests;
    }

    public void setTotalPendingInchargeRequests(long totalPendingInchargeRequests) {
        this.totalPendingInchargeRequests = totalPendingInchargeRequests;
    }

    public long getTotalApprovedRequests() {
        return totalApprovedRequests;
    }

    public void setTotalApprovedRequests(long totalApprovedRequests) {
        this.totalApprovedRequests = totalApprovedRequests;
    }

    public static class Builder {
        private String departmentName;
        private long totalNurses;
        private long totalAssignedToday;
        private long totalUnassignedToday;
        private long totalPendingPeerRequests;
        private long totalPendingInchargeRequests;
        private long totalApprovedRequests;

        public Builder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public Builder totalNurses(long totalNurses) {
            this.totalNurses = totalNurses;
            return this;
        }

        public Builder totalAssignedToday(long totalAssignedToday) {
            this.totalAssignedToday = totalAssignedToday;
            return this;
        }

        public Builder totalUnassignedToday(long totalUnassignedToday) {
            this.totalUnassignedToday = totalUnassignedToday;
            return this;
        }

        public Builder totalPendingPeerRequests(long totalPendingPeerRequests) {
            this.totalPendingPeerRequests = totalPendingPeerRequests;
            return this;
        }

        public Builder totalPendingInchargeRequests(long totalPendingInchargeRequests) {
            this.totalPendingInchargeRequests = totalPendingInchargeRequests;
            return this;
        }

        public Builder totalApprovedRequests(long totalApprovedRequests) {
            this.totalApprovedRequests = totalApprovedRequests;
            return this;
        }

        public DutyOfficerDashboardDto build() {
            return new DutyOfficerDashboardDto(this);
        }
    }
}