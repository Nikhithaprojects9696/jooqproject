package com.example.JOOQPROJECT;

public record UserProfileRequest(
        String username,
        String email,
        String displayName,
        String bio,
        Integer age,
        String gender
) {}
