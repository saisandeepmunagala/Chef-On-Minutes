package com.chefonminutes.service;

import com.chefonminutes.dto.AuthResponseDTO;
import com.chefonminutes.dto.LoginRequestDTO;
import com.chefonminutes.dto.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO request);
    AuthResponseDTO login(LoginRequestDTO request);
}
