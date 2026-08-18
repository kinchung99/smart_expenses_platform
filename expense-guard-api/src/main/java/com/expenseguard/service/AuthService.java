package com.expenseguard.service;

import com.expenseguard.dto.AuthResponse;
import com.expenseguard.dto.LoginRequest;
import com.expenseguard.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
