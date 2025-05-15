package com.encora.spotify.service.impl;

import com.encora.spotify.config.SpotifyProperties;
import com.encora.spotify.dto.SpotifyAuthRequestDto;
import com.encora.spotify.dto.SpotifyAuthResponseDto;
import com.encora.spotify.service.AuthService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final RestTemplate restTemplate;
    private final SpotifyProperties spotifyProperties;

    public AuthServiceImpl(RestTemplate restTemplate, SpotifyProperties spotifyProperties){
        this.restTemplate = restTemplate;
        this.spotifyProperties = spotifyProperties;
    }

    @Override
    public SpotifyAuthResponseDto exchangeCodeForToken(SpotifyAuthRequestDto request){
        //False response
        //return new SpotifyAuthResponseDto("access-token", "refresh-token", 3600);

        //Real response
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String credentials = spotifyProperties.getClientId() + ":" + spotifyProperties.getClientSecret();
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        header.set("Authorization", "Basic " + encodedCredentials);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", request.getCode());
        formData.add("redirect_uri", request.getRedirectUri());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(formData, header);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://accounts.spotify.com/api/token",
                httpEntity,
                Map.class
            );

            Map<String, Object> body = response.getBody();
            if(body == null){
                throw new RuntimeException("Empty response from Spotify.");
            }

            return new SpotifyAuthResponseDto(
                    (String) body.get("access_token"),
                    (String) body.get("refresh_token"),
                    ((Number) body.get("expires_in")).intValue()
            );
        } catch(HttpClientErrorException e){
            throw new RuntimeException("Spotify authentication failed: " + e.getResponseBodyAsString(), e);
        }

    }
}
