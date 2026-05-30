package authservice.controller;

import authservice.dto.*;
import authservice.dto.AuthResponse;
import authservice.dto.LoginRequest;
import authservice.dto.RegisterRequest;
import authservice.dto.UpdateProfileRequest;
import authservice.dto.UserProfileResponse;
import authservice.dto.ValidateResponse;
import authservice.service.AuthService;
import authservice.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
        return authService.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

    @GetMapping("/validate")
    public ValidateResponse validate(@RequestHeader("Authorization") String authHeader) {
        Claims claims = jwtService.validateToken(authHeader.substring(7));
        return authService.validate(Integer.parseInt(claims.getSubject()));
    }

    @PutMapping("/profile")
    public ValidateResponse updateProfile(@RequestBody UpdateProfileRequest req, Authentication auth) {
        Integer userId = Integer.parseInt((String) auth.getPrincipal());
        return authService.updateProfile(userId, req);
    }

    @GetMapping("/user/{userId}")
    public UserProfileResponse getUserById(@PathVariable Integer userId) {
        return authService.getUserById(userId);
    }
}
