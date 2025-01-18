package dev.shreya.authenticationservice.service;

import dev.shreya.authenticationservice.dtos.ValidateRequestDto;
import dev.shreya.authenticationservice.exceptions.UserAlreadyExistsException;
import dev.shreya.authenticationservice.exceptions.UserNotFoundException;
import dev.shreya.authenticationservice.exceptions.WrongPasswordException;
import dev.shreya.authenticationservice.models.Session;
import dev.shreya.authenticationservice.models.SessionStatus;
import dev.shreya.authenticationservice.models.User;
import dev.shreya.authenticationservice.repositories.SessionRepository;
import dev.shreya.authenticationservice.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AuthService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SecretKey key = Keys.hmacShaKeyFor(
            "namanisveryveryveryveryveryveryverycool"
                    .getBytes(StandardCharsets.UTF_8));
    //    private SecretKey key = Jwts.SIG.HS256.key().build();
//    private SecretKey key = Keys.hmacShaKeyFor(
//            "namanisveryveryveryveryveryveryverycool"
//                    .getBytes(StandardCharsets.UTF_8));

    private SessionRepository sessionRepository;

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sessionRepository = sessionRepository;
    }
    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null;
        }

        Session session = sessionOptional.get();

        session.setSessionStatus(SessionStatus.LOGGED_OUT);

        sessionRepository.save(session);

        return ResponseEntity.ok().build();
    }

    public boolean signUp(String email, String password) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with email: " + email + " already exists");
        }

        User user = new User();

        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        userRepository.save(user);

        return true;
    }
    public String login(String email, String password) throws WrongPasswordException, UserNotFoundException      {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with email: " + email + " not found.");
        }

        boolean matches = bCryptPasswordEncoder.matches(
                password,
                userOptional.get().getPassword()
        );

        if (matches) {
            String token =  createJwtToken(userOptional.get().getId(),
                    new ArrayList<>(),
                    userOptional.get().getEmail());
       //     String token = RandomStringUtils.randomAscii(20);

            Session session = new Session();
            session.setToken(token);
            session.setSessionStatus(SessionStatus.ACTIVE);
            session.setUser(userOptional.get());

            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();

            calendar.add(Calendar.DAY_OF_MONTH, 30);
            Date datePlus30Days = calendar.getTime();
            session.setExpiringAt(datePlus30Days);

            sessionRepository.save(session);

            return token;
        } else {
            throw new WrongPasswordException("Wrong password.");
        }
    }

    public Optional<User> validate(ValidateRequestDto request) {
//       Optional<Session> session = sessionRepository.findByTokenAndUser_Id(request.getToken(),request.getUserId());
//       if(session.isEmpty()) {
//           return SessionStatus.INVALID;
//       }
//
//    if(!session.get().getSessionStatus().equals(SessionStatus.ACTIVE)) {
//        return SessionStatus.EXPIRED;
   // }
//        if(!(session.get().getExpiringAt() > new Date())) {
       //     return SessionStatus.EXPIRED;
        //}
        Optional<User> user = userRepository.findById(request.getUserId());
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(request.getToken());

            Date expiryAt = claims.getPayload().getExpiration();
            Long userId = claims.getPayload().get("user_id", Long.class);

        } catch (Exception e) {
            return Optional.empty();
        }

        return user;
    }
    private String createJwtToken(Long userId, List<String> roles, String email) {
        Map<String, Object> dataInJwt = new HashMap<>();
        dataInJwt.put("user_id", userId);
        dataInJwt.put("roles", roles);
        dataInJwt.put("email", email);

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date datePlus30Days = calendar.getTime();

        String token = Jwts.builder()
                .claims(dataInJwt)
                .expiration(datePlus30Days)
                .issuedAt(new Date())
                .signWith(key)
                .compact();

        return token;
    }
}
