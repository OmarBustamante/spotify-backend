package com.encora.spotify.service;

public interface TokenManagerService {
    String getAccessToken(String userId);
    void storeTokens(String userId, String accessToken, String refreshToken, int expiresIn);
}
