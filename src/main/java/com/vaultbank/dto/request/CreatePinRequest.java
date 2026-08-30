package com.vaultbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePinRequest {

    @NotBlank(message = "PIN is required")
    @Pattern(
        regexp = "\\d{4}",
        message = "PIN must be exactly 4 digits"
    )
    private String pin;

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}
}