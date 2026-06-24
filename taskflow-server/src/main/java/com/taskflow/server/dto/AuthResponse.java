package com.taskflow.server.dto;

public record AuthResponse(String token, UserDto user) {}
