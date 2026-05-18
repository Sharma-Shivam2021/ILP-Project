package com.hwscs.backend.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NurseDashboardDto {
    private int upcomingShiftCount;
    private int pendingRequestCount;
    private int approvedSwapCount;
    private List<NurseShiftResponseDto> upcomingShifts;
    private List<ShiftRequestResponseDto> recentRequests;

    public NurseDashboardDto(int upcomingShiftCount, int pendingRequestCount, int approvedSwapCount,
                             List<NurseShiftResponseDto> upcomingShifts, List<ShiftRequestResponseDto> recentRequests) {
        super();
        this.upcomingShiftCount = upcomingShiftCount;
        this.pendingRequestCount = pendingRequestCount;
        this.approvedSwapCount = approvedSwapCount;
        this.upcomingShifts = upcomingShifts;
        this.recentRequests = recentRequests;
    }

    public NurseDashboardDto() {
        super();
    }

    private NurseDashboardDto(Builder builder) {
        this.upcomingShiftCount = builder.upcomingShiftCount;
        this.pendingRequestCount = builder.pendingRequestCount;
        this.approvedSwapCount = builder.approvedSwapCount;
        this.upcomingShifts = builder.upcomingShifts != null ? builder.upcomingShifts : new ArrayList<>();
        this.recentRequests = builder.recentRequests != null ? builder.recentRequests : new ArrayList<>();
    }

    // Builder entry method
    public static Builder builder() {
        return new Builder();
    }

    public int getUpcomingShiftCount() {
        return upcomingShiftCount;
    }

    public void setUpcomingShiftCount(int upcomingShiftCount) {
        this.upcomingShiftCount = upcomingShiftCount;
    }

    public int getPendingRequestCount() {
        return pendingRequestCount;
    }

    public void setPendingRequestCount(int pendingRequestCount) {
        this.pendingRequestCount = pendingRequestCount;
    }

    public int getApprovedSwapCount() {
        return approvedSwapCount;
    }

    public void setApprovedSwapCount(int approvedSwapCount) {
        this.approvedSwapCount = approvedSwapCount;
    }

    public List<NurseShiftResponseDto> getUpcomingShifts() {
        return upcomingShifts;
    }

    public void setUpcomingShifts(List<NurseShiftResponseDto> upcomingShifts) {
        this.upcomingShifts = upcomingShifts;
    }

    public List<ShiftRequestResponseDto> getRecentRequests() {
        return recentRequests;
    }

    public void setRecentRequests(List<ShiftRequestResponseDto> recentRequests) {
        this.recentRequests = recentRequests;
    }

    @Override
    public String toString() {
        return "NurseDashboardDto [upcomingShiftCount=" + upcomingShiftCount + ", pendingRequestCount="
                + pendingRequestCount + ", approvedSwapCount=" + approvedSwapCount + ", upcomingShifts="
                + upcomingShifts + ", recentRequests=" + recentRequests + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(approvedSwapCount, pendingRequestCount, recentRequests, upcomingShiftCount, upcomingShifts);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NurseDashboardDto other = (NurseDashboardDto) obj;
        return approvedSwapCount == other.approvedSwapCount && pendingRequestCount == other.pendingRequestCount
                && Objects.equals(recentRequests, other.recentRequests)
                && upcomingShiftCount == other.upcomingShiftCount
                && Objects.equals(upcomingShifts, other.upcomingShifts);
    }

    public static class Builder {
        private int upcomingShiftCount;
        private int pendingRequestCount;
        private int approvedSwapCount;
        private List<NurseShiftResponseDto> upcomingShifts;
        private List<ShiftRequestResponseDto> recentRequests;

        public Builder upcomingShiftCount(int upcomingShiftCount) {
            this.upcomingShiftCount = upcomingShiftCount;
            return this;
        }

        public Builder pendingRequestCount(int pendingRequestCount) {
            this.pendingRequestCount = pendingRequestCount;
            return this;
        }

        public Builder approvedSwapCount(int approvedSwapCount) {
            this.approvedSwapCount = approvedSwapCount;
            return this;
        }

        public Builder upcomingShifts(List<NurseShiftResponseDto> upcomingShifts) {
            this.upcomingShifts = upcomingShifts;
            return this;
        }

        public Builder recentRequests(List<ShiftRequestResponseDto> recentRequests) {
            this.recentRequests = recentRequests;
            return this;
        }

        public NurseDashboardDto build() {
            return new NurseDashboardDto(this);
        }
    }
}