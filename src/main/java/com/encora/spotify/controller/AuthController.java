package com.encora.spotify.controller;

import com.encora.spotify.dto.SpotifyAuthResponseDto;
import com.encora.spotify.dto.SpotifyAuthRequestDto;
import com.encora.spotify.service.AuthService;
import com.encora.spotify.service.TokenManagerService;
import com.encora.spotify.config.SpotifyProperties;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final SpotifyProperties spotifyProperties;
    private final TokenManagerService tokenManagerService;

    @Autowired
    public AuthController(AuthService authService, SpotifyProperties spotifyProperties, TokenManagerService tokenManagerService){
        this.authService = authService;
        this.spotifyProperties = spotifyProperties;
        this.tokenManagerService = tokenManagerService;
    }

    @PostMapping("/spotify")
    public ResponseEntity<?> authenticateWithSpotify(@Valid @RequestBody SpotifyAuthRequestDto request){

        try{
            SpotifyAuthResponseDto response = authService.exchangeCodeForToken(request);

            String userId = "userId";

            tokenManagerService.storeTokens(
                    userId,
                    response.getAccessToken(),
                    response.getRefreshToken(),
                    response.getExpiresIn()
            );

            return ResponseEntity.ok(response);
        } catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
