package com.finpulse.fin.service;

import com.finpulse.fin.dto.*;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}