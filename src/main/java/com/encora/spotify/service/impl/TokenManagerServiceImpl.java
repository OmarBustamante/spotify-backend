package com.encora.spotify.service.impl;

import com.encora.spotify.config.SpotifyProperties;
import com.encora.spotify.service.TokenManagerService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenManagerServiceImpl implements TokenManagerService {

    private final RestTemplate restTemplate;
    private final SpotifyProperties spotifyProperties;

    private static class TokenInfo {
        String accessToken;
        String refreshToken;
        Instant expiresAt;

        TokenInfo(String accessToken, String refreshToken, int expiresIn){
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.expiresAt = Instant.now().plusSeconds(expiresIn-60);
        }

        boolean isExpired(){
            return Instant.now().isAfter(expiresAt);
        }
    }

    private final Map<String, TokenInfo> userTokenStore = new HashMap<>();

    public TokenManagerServiceImpl(RestTemplate restTemplate, SpotifyProperties spotifyProperties){
        this.restTemplate = restTemplate;
        this.spotifyProperties = spotifyProperties;
    }

    @Override
    public String getAccessToken(String userId){
        TokenInfo tokenInfo = userTokenStore.get(userId);
        if(tokenInfo == null){
            throw new RuntimeException("No token found for user: " + userId);
        }

        if(tokenInfo.isExpired()){
            refreshAccessToken(userId, tokenInfo);
        }

        return tokenInfo.accessToken;
    }

    @Override
    public void storeTokens(String userId, String accessToken, String refreshToken, int expiresIn){
        userTokenStore.put(userId, new TokenInfo(accessToken, refreshToken, expiresIn));
    }

    private void refreshAccessToken(String userId, TokenInfo tokenInfo){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String credentials = spotifyProperties.getClientId() + ":" + spotifyProperties.getClientSecret();
        String encodeCredentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
        headers.add("refresh_token", tokenInfo.refreshToken);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", tokenInfo.refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://accounts.spotify.com/api/token",
                HttpMethod.POST,
                request,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        if(responseBody == null || !responseBody.containsKey("access_token")){
            throw new RuntimeException("Failed to refresh token for user: " + userId);
        }

        String newAccessToken = (String) responseBody.get("access_token");
        int expiresIn = ((Number) responseBody.get("expires_in")).intValue();

        tokenInfo.accessToken = newAccessToken;
        tokenInfo.expiresAt = Instant.now().plusSeconds(expiresIn - 60);
    }
}
