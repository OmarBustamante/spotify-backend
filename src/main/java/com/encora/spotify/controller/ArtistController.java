package com.encora.spotify.controller;

import com.encora.spotify.dto.SpotifyArtistDto;
import com.encora.spotify.service.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/me/top/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService){
        this.artistService = artistService;
    }

    @GetMapping
    public ResponseEntity<List<SpotifyArtistDto>> getTopArtist(@RequestHeader("Authorization") String authorizationHeader){
        if(!authorizationHeader.startsWith("Bearer ")){
            return ResponseEntity.badRequest().build();
        }

        String accessToken = authorizationHeader.substring(7);
        List<SpotifyArtistDto> topArtists = artistService.getTopArtists(accessToken);
        return ResponseEntity.ok(topArtists);
    }
}
