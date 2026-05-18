package com.hwscs.backend.dto.response;

import java.util.Objects;

public class NurseResponseDto {
    private Integer id;
    private String employeeCode;
    private String fullName;
    private String nurseType;
    private String contactPhone;
    private String contactEmail;
    private String departmentName;
    private String username;

    public NurseResponseDto() {
    }

    public NurseResponseDto(Integer id, String employeeCode, String fullName, String nurseType, String contactPhone,
                            String contactEmail, String departmentName, String username) {
        super();
        this.id = id;
        this.employeeCode = employeeCode;
        this.fullName = fullName;
        this.nurseType = nurseType;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.departmentName = departmentName;
        this.username = username;
    }

    // Private constructor for Builder
    private NurseResponseDto(Builder builder) {
        this.id = builder.id;
        this.employeeCode = builder.employeeCode;
        this.fullName = builder.fullName;
        this.nurseType = builder.nurseType;
        this.contactPhone = builder.contactPhone;
        this.contactEmail = builder.contactEmail;
        this.departmentName = builder.departmentName;
        this.username = builder.username;
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

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNurseType() {
        return nurseType;
    }

    public void setNurseType(String nurseType) {
        this.nurseType = nurseType;
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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "NurseResponseDto [id=" + id + ", employeeCode=" + employeeCode + ", fullName=" + fullName
                + ", nurseType=" + nurseType + ", contactPhone=" + contactPhone + ", contactEmail=" + contactEmail
                + ", departmentName=" + departmentName + ", username=" + username + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactEmail, contactPhone, departmentName, employeeCode, fullName, id, nurseType,
                username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NurseResponseDto other = (NurseResponseDto) obj;
        return Objects.equals(contactEmail, other.contactEmail) && Objects.equals(contactPhone, other.contactPhone)
                && Objects.equals(departmentName, other.departmentName)
                && Objects.equals(employeeCode, other.employeeCode) && Objects.equals(fullName, other.fullName)
                && Objects.equals(id, other.id) && Objects.equals(nurseType, other.nurseType)
                && Objects.equals(username, other.username);
    }

    // Builder implementation
    public static class Builder {
        private Integer id;
        private String employeeCode;
        private String fullName;
        private String nurseType;
        private String contactPhone;
        private String contactEmail;
        private String departmentName;
        private String username;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder employeeCode(String employeeCode) {
            this.employeeCode = employeeCode;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder nurseType(String nurseType) {
            this.nurseType = nurseType;
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

        public Builder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public NurseResponseDto build() {
            return new NurseResponseDto(this);
        }
    }
}