package dev.shreya.authenticationservice.dtos;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequestDto {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private String token;
    private Long userId;
}