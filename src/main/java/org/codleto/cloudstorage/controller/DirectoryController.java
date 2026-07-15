package org.codleto.cloudstorage.controller;


import lombok.RequiredArgsConstructor;
import org.codleto.cloudstorage.security.UserDetailsImpl;
import org.codleto.cloudstorage.storage.StorageService;
import org.codleto.cloudstorage.storage.dto.ResourceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DirectoryController {

    private final StorageService storageService;

    @PostMapping("/api/directory")
    public ResponseEntity<ResourceResponse> createDirectory(
            @RequestParam String path,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        ResourceResponse response = storageService.createDirectory(userDetails.getId(), path);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
