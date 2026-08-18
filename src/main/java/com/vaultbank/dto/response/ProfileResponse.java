package com.vaultbank.dto.response;

import java.time.LocalDateTime;

public class ProfileResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private boolean enabled;
    private LocalDateTime createdAt;

    public ProfileResponse(
            Long id,
            String fullName,
            String email,
            String phoneNumber,
            String address,
            boolean enabled,
            LocalDateTime createdAt) {

        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.enabled = enabled;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}