package com.contactsSystem.dto;

import com.contactsSystem.domain.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}

