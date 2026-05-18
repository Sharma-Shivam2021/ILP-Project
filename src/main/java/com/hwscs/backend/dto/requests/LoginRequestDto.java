package com.hwscs.backend.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

public class LoginRequestDto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{13,}$",
            message = "Password must be atleast 13 characters and contain uppercase, lowercase, number, and special characters."
    )
    private String password;

    public LoginRequestDto() {
    }

    public LoginRequestDto(@NotBlank(message = "Username is required") String username,
                           @NotBlank(message = "Password is required") String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequestDto [username=" + username + ", password=" + password + ", getUsername()=" + getUsername()
                + ", getPassword()=" + getPassword() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
                + ", toString()=" + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LoginRequestDto other = (LoginRequestDto) obj;
        return Objects.equals(password, other.password) && Objects.equals(username, other.username);
    }


}