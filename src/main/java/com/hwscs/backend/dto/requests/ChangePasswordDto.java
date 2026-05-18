package com.hwscs.backend.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

public class ChangePasswordDto {

    @NotBlank
    private String currentPassword;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{13,}$",
            message = "Password must be atleast 13 characters and contain uppercase, lowercase, number, and special characters."
    )
    private String newPassword;

    public ChangePasswordDto(@NotBlank String currentPassword, @NotBlank String newPassword) {
        super();
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public ChangePasswordDto() {
        super();
    }

    // Builder entry method
    public static Builder builder() {
        return new Builder();
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ChangePasswordDto [currentPassword=" + currentPassword + ", newPassword=" + newPassword + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPassword, newPassword);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChangePasswordDto other = (ChangePasswordDto) obj;
        return Objects.equals(currentPassword, other.currentPassword) && Objects.equals(newPassword, other.newPassword);
    }

    public static class Builder {
        private String currentPassword;
        private String newPassword;

        public Builder currentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
            return this;
        }

        public Builder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public ChangePasswordDto build() {
            return new ChangePasswordDto();
        }
    }
}