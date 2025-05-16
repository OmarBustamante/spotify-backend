package com.encora.spotify.service;

import com.encora.spotify.dto.SpotifyArtistDto;
import com.encora.spotify.dto.SpotifyTrackDto;
import com.encora.spotify.dto.SpotifyAlbumDto;

import java.util.List;

public interface SearchService {
    List<SpotifyArtistDto> searchArtists(String query, String accessToken);
    List<SpotifyAlbumDto> searchAlbums(String query, String accessToken);
    List<SpotifyTrackDto> searchTracks(String query, String accessToken);
}