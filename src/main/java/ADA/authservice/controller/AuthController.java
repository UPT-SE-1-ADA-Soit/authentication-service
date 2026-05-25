package ADA.authservice.controller;

import ADA.authservice.dto.AuthResponse;
import ADA.authservice.dto.LoginRequest;
import ADA.authservice.dto.RegisterRequest;
import ADA.authservice.dto.RegisterResponse;
import ADA.authservice.service.AuthService;
import ADA.authservice.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest req) {
        return authService.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

    @GetMapping("/validate")
    public Map<String, Object> validate(@RequestHeader("Authorization") String authHeader) {
        Claims claims = jwtService.validateToken(authHeader.substring(7));
        return Map.of(
                "userId", Integer.parseInt(claims.getSubject()),
                "email", claims.get("email", String.class),
                "name", claims.get("name", String.class)
        );
    }
}
