package com.vaultbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePinRequest {

    @NotBlank(message = "Old PIN is required")
    @Pattern(
        regexp = "\\d{4}",
        message = "Old PIN must be exactly 4 digits"
    )
    private String oldPin;

    @NotBlank(message = "New PIN is required")
    @Pattern(
        regexp = "\\d{4}",
        message = "New PIN must be exactly 4 digits"
    )
    private String newPin;

	public String getOldPin() {
		return oldPin;
	}

	public void setOldPin(String oldPin) {
		this.oldPin = oldPin;
	}

	public String getNewPin() {
		return newPin;
	}

	public void setNewPin(String newPin) {
		this.newPin = newPin;
	}
}