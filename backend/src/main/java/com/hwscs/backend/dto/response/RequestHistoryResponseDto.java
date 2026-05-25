package com.hwscs.backend.dto.response;

import java.time.LocalDateTime;
import java.util.Objects;

public class RequestHistoryResponseDto {
    private Integer id;
    private String actorUsername;
    private String actorRole;
    private String action;
    private String remarks;
    private LocalDateTime actedAt;

    public RequestHistoryResponseDto() {
    }

    public RequestHistoryResponseDto(Integer id, String actorUsername, String actorRole, String action, String remarks,
                                     LocalDateTime actedAt) {
        super();
        this.id = id;
        this.actorUsername = actorUsername;
        this.actorRole = actorRole;
        this.action = action;
        this.remarks = remarks;
        this.actedAt = actedAt;
    }

    // Private constructor for Builder
    private RequestHistoryResponseDto(Builder builder) {
        this.id = builder.id;
        this.actorUsername = builder.actorUsername;
        this.actorRole = builder.actorRole;
        this.action = builder.action;
        this.remarks = builder.remarks;
        this.actedAt = builder.actedAt;
    }

    // Manual builder() method
    public static Builder builder() {
        return new Builder();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActorUsername() {
        return actorUsername;
    }

    public void setActorUsername(String actorUsername) {
        this.actorUsername = actorUsername;
    }

    public String getActorRole() {
        return actorRole;
    }

    public void setActorRole(String actorRole) {
        this.actorRole = actorRole;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getActedAt() {
        return actedAt;
    }

    public void setActedAt(LocalDateTime actedAt) {
        this.actedAt = actedAt;
    }

    @Override
    public String toString() {
        return "RequestHistoryResponseDto [id=" + id + ", actorUsername=" + actorUsername + ", actorRole=" + actorRole
                + ", action=" + action + ", remarks=" + remarks + ", actedAt=" + actedAt + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(actedAt, action, actorRole, actorUsername, id, remarks);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RequestHistoryResponseDto other = (RequestHistoryResponseDto) obj;
        return Objects.equals(actedAt, other.actedAt) && Objects.equals(action, other.action)
                && Objects.equals(actorRole, other.actorRole) && Objects.equals(actorUsername, other.actorUsername)
                && Objects.equals(id, other.id) && Objects.equals(remarks, other.remarks);
    }

    // Builder implementation
    public static class Builder {
        private Integer id;
        private String actorUsername;
        private String actorRole;
        private String action;
        private String remarks;
        private LocalDateTime actedAt;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder actorUsername(String actorUsername) {
            this.actorUsername = actorUsername;
            return this;
        }

        public Builder actorRole(String actorRole) {
            this.actorRole = actorRole;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder remarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public Builder actedAt(LocalDateTime actedAt) {
            this.actedAt = actedAt;
            return this;
        }

        public RequestHistoryResponseDto build() {
            return new RequestHistoryResponseDto(this);
        }
    }
}