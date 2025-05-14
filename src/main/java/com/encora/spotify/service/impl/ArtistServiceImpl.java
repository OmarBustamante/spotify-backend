package com.encora.spotify.service.impl;

import com.encora.spotify.dto.SpotifyArtistDto;
import com.encora.spotify.service.ArtistService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {

    @Override
    public List<SpotifyArtistDto> getTopArtists(String accessToken){
        //Fake response
        return List.of(
                new SpotifyArtistDto("1", "Artist 1", List.of("pop"), "https://image.url/artist1.jpg"),
                new SpotifyArtistDto("2", "Artist 2", List.of("rock"), "https://image.url/artist2.jpg")
        );
    }
}
