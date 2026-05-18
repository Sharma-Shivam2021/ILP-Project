package com.hwscs.backend.dto.requests;

import jakarta.validation.constraints.Email;

import java.util.Objects;

public class UpdateProfileDto {
    private String fullName;
    private String contactPhone;
    @Email(message = "Invalid email format")
    private String contactEmail;

    public UpdateProfileDto(String fullName, String contactPhone,
                            @Email(message = "Invalid email format") String contactEmail) {
        super();
        this.fullName = fullName;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
    }

    public UpdateProfileDto(Builder builder) {
        this.fullName = builder.fullName;
        this.contactPhone = builder.contactPhone;
        this.contactEmail = builder.contactEmail;
    }

    // Builder entry method
    public static Builder builder() {
        return new Builder();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Override
    public String toString() {
        return "UpdateProfileDto [fullName=" + fullName + ", contactPhone=" + contactPhone + ", contactEmail="
                + contactEmail + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactEmail, contactPhone, fullName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UpdateProfileDto other = (UpdateProfileDto) obj;
        return Objects.equals(contactEmail, other.contactEmail) && Objects.equals(contactPhone, other.contactPhone)
                && Objects.equals(fullName, other.fullName);
    }

    public static class Builder {
        private String fullName;
        private String contactPhone;
        private String contactEmail;

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder contactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
            return this;
        }

        public Builder contactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
            return this;
        }

        public UpdateProfileDto build() {
            return new UpdateProfileDto(this);
        }
    }

}