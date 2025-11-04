package com.arteaga.stream.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public class CreateUserRequest {
    @NotBlank(message = "Username es requerido")
    @Size(min = 3, max = 20, message = "Username debe tener entre 3 y 20 caracteres")
    private String username;

    @NotBlank(message = "Display name es requerido")
    @Size(max = 50, message = "Display name no puede exceder 50 caracteres")
    private String displayName;

    @Email(message = "Email debe ser válido")
    private String email;

    @NotBlank(message = "Password es requerido")
    @Size(min = 6, message = "Password debe tener al menos 6 caracteres")
    private String password;

    public CreateUserRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}