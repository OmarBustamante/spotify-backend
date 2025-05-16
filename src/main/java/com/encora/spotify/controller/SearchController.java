package com.encora.spotify.controller;

import com.encora.spotify.dto.SpotifyAlbumDto;
import com.encora.spotify.dto.SpotifyArtistDto;
import com.encora.spotify.dto.SpotifyTrackDto;
import com.encora.spotify.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService){
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<?> search(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("q") String query,
            @RequestParam("type") String type
    ) {
        if(!authorizationHeader.startsWith("Bearer ") || authorizationHeader == null){
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }

        String accessToken = authorizationHeader.substring(7);

        try {
            switch (type.toLowerCase()) {
                case "artist":
                    List<SpotifyArtistDto> artists = searchService.searchArtists(query, accessToken);
                    return ResponseEntity.ok(artists);
                case "album":
                    List<SpotifyAlbumDto> albums = searchService.searchAlbums(query, accessToken);
                    return ResponseEntity.ok(albums);
                case "track":
                    List<SpotifyTrackDto> tracks = searchService.searchTracks(query, accessToken);
                    return ResponseEntity.ok(tracks);
                default:
                    return ResponseEntity.badRequest().body("Invalid search type. Must be: artist, album, track");
            }
        } catch(HttpClientErrorException e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }
}
