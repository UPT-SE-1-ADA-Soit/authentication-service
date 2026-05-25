package ADA.authservice.service;

import ADA.authservice.dto.AuthResponse;
import ADA.authservice.dto.LoginRequest;
import ADA.authservice.dto.RegisterRequest;
import ADA.authservice.dto.RegisterResponse;
import ADA.authservice.entity.User;
import ADA.authservice.exception.AuthException;
import ADA.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponse register(RegisterRequest req) {
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .build();
        user = userRepository.save(user);
        return new RegisterResponse(user.getId(), user.getEmail(), user.getName());
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new AuthException("Invalid credentials"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new AuthException("Invalid credentials");
        }
        return new AuthResponse(jwtService.generateToken(user), user.getId(), user.getEmail());
    }
}
