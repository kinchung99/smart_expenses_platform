package com.expenseguard.dto;

public record AuthResponse(
        String token,
        String email) {
}
