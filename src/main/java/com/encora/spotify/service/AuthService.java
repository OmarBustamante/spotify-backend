package com.encora.spotify.service;

import com.encora.spotify.dto.SpotifyAuthRequestDto;
import com.encora.spotify.dto.SpotifyAuthResponseDto;

public interface AuthService {
    SpotifyAuthResponseDto exchangeCodeForToken(SpotifyAuthRequestDto request);
}
