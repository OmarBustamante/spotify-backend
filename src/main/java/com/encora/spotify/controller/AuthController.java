package com.encora.spotify.controller;

import com.encora.spotify.dto.SpotifyAuthResponseDto;
import com.encora.spotify.dto.SpotifyAuthRequestDto;
import com.encora.spotify.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/spotify")
    public ResponseEntity<SpotifyAuthResponseDto> authenticateWithSpotify(@RequestBody SpotifyAuthRequestDto request){
        SpotifyAuthResponseDto response = authService.exchangeCodeForToken(request);
        return ResponseEntity.ok(response);
    }

}
