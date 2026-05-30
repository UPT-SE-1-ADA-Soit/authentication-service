package authservice.service;

import authservice.dto.*;
import authservice.entity.User;
import authservice.exception.AuthException;
import authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest req) {
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .build();
        user = userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(user), user.getId(), user.getEmail(),
                user.getName(), null, null);
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new AuthException("Invalid credentials"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new AuthException("Invalid credentials");
        }
        return new AuthResponse(jwtService.generateToken(user), user.getId(), user.getEmail(),
                user.getName(), user.getLocation(), user.getAvatarUrl());
    }

    public ValidateResponse validate(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        return new ValidateResponse(user.getId(), user.getEmail(), user.getName(),
                user.getLocation(), user.getAvatarUrl());
    }

    public ValidateResponse updateProfile(Integer userId, UpdateProfileRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (req.getName() != null && !req.getName().isBlank()) user.setName(req.getName());
        if (req.getLocation() != null) user.setLocation(req.getLocation());
        if (req.getAvatarUrl() != null) user.setAvatarUrl(req.getAvatarUrl());
        user = userRepository.save(user);
        return new ValidateResponse(user.getId(), user.getEmail(), user.getName(),
                user.getLocation(), user.getAvatarUrl());
    }

    public UserProfileResponse getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new UserProfileResponse(user.getId(), user.getName(), user.getLocation(), user.getAvatarUrl());
    }
}
