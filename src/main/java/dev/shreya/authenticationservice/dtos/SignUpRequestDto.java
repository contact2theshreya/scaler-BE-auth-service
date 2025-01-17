package dev.shreya.authenticationservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String email;

    public String getPassword() {
        return password;
    }

    private String password;

    public String getEmail() {
        return email;
    }
}