package com.encora.spotify.controller;

import com.encora.spotify.dto.SpotifyAuthResponseDto;
import com.encora.spotify.dto.SpotifyAuthRequestDto;
import com.encora.spotify.service.AuthService;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> authenticateWithSpotify(@Valid @RequestBody SpotifyAuthRequestDto request){
        try{
            SpotifyAuthResponseDto response = authService.exchangeCodeForToken(request);
            return ResponseEntity.ok(response);
        } catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
