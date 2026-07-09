package org.codleto.cloudstorage.controller;

import org.codleto.cloudstorage.dto.AuthResponse;
import org.codleto.cloudstorage.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/api/user/me")
    public ResponseEntity<AuthResponse> getCurrentUser (
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        return ResponseEntity.ok(new AuthResponse(userDetails.getUsername()));
    }
}
