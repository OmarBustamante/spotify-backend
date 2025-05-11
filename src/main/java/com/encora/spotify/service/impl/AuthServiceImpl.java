package com.encora.spotify.service.impl;

import com.encora.spotify.dto.SpotifyAuthRequestDto;
import com.encora.spotify.dto.SpotifyAuthResponseDto;
import com.encora.spotify.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public SpotifyAuthResponseDto exchangeCodeForToken(SpotifyAuthRequestDto request){
        //False response
        return new SpotifyAuthResponseDto("access-token", "refresh-token", 3600);
    }
}
