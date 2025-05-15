package com.encora.spotify.service;

import com.encora.spotify.dto.SpotifyArtistDto;

import java.util.List;

public interface ArtistService {
    List<SpotifyArtistDto> getTopArtists(String accessToken);
    SpotifyArtistDto getArtistDetails(String accessToken, String artistId);
}
