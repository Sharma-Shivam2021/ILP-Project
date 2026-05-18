package com.hwscs.backend.dto.response;

public class LoginResponseDto {

    private String token;
    private String username;
    private String role;
    private Long userId;
    private Boolean firstLogin;

    public LoginResponseDto(String token, String username, String role, Long userId, Boolean firstLogin) {
        super();
        this.token = token;
        this.username = username;
        this.role = role;
        this.userId = userId;
        this.firstLogin = firstLogin;
    }

    public LoginResponseDto() {
    }

    // Private constructor for Builder
    private LoginResponseDto(Builder builder) {
        this.token = builder.token;
        this.username = builder.username;
        this.role = builder.role;
        this.userId = builder.userId;
        this.firstLogin = builder.firstLogin;
    }

    // Manual builder() method
    public static Builder builder() {
        return new Builder();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(Boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    // Builder implementation
    public static class Builder {
        private String token;
        private String username;
        private String role;
        private Long userId;
        private Boolean firstLogin;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder firstLogin(Boolean firstLogin) {
            this.firstLogin = firstLogin;
            return this;
        }

        public LoginResponseDto build() {
            return new LoginResponseDto(this);
        }
    }
}