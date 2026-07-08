package org.codleto.cloudstorage.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.codleto.cloudstorage.dto.AuthRequest;
import org.codleto.cloudstorage.dto.AuthResponse;
import org.codleto.cloudstorage.entity.UserEntity;
import org.codleto.cloudstorage.exception.UsernameAlreadyTakenException;
import org.codleto.cloudstorage.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Transactional
    public AuthResponse signUp(
            AuthRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyTakenException();
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        UserEntity user = new UserEntity(request.username(), encodedPassword);
        userRepository.save(user);

        authenticateAndSaveContext(
                request.username(),
                request.password(),
                httpRequest,
                httpResponse
        );

        return new AuthResponse(user.getUsername());
    }

    private void authenticateAndSaveContext(
            String username,
            String rawPassword,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, rawPassword)
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }
}
