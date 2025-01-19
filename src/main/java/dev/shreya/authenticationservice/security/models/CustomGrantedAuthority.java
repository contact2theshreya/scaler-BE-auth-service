package dev.shreya.authenticationservice.security.models;



import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.shreya.authenticationservice.models.Role;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;


@JsonDeserialize
@NoArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {
    public void setAuthority(String authority) {
        this.authority = authority;
    }
    public CustomGrantedAuthority() {

    }
    //    private Role role;
    private String authority;

    public CustomGrantedAuthority(Role role) {
        this.authority = role.getName();
    }


    @Override
    public String getAuthority() {//method with get jackson expect there is a field with authority
        return this.authority;
    }
}