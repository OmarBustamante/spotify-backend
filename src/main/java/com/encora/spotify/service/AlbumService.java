package com.encora.spotify.service;

import com.encora.spotify.dto.SpotifyAlbumDto;

public interface AlbumService {
    SpotifyAlbumDto getAlbumById(String accessToken, String albumId);
}
