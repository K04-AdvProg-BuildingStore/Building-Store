package id.ac.ui.cs.advprog.buildingstore.auth.service;

import id.ac.ui.cs.advprog.buildingstore.auth.controller.AuthenticationRequest;
import id.ac.ui.cs.advprog.buildingstore.auth.controller.AuthenticationResponse;
import id.ac.ui.cs.advprog.buildingstore.auth.controller.RegisterRequest;
import id.ac.ui.cs.advprog.buildingstore.auth.model.Role;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request){
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CASHIER) //user will get a role of 0 according to the enum
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
