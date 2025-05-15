package com.encora.spotify.controller;

import com.encora.spotify.dto.SpotifyArtistDto;
import com.encora.spotify.service.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService){
        this.artistService = artistService;
    }

    @GetMapping("/me/top/artists")
    public ResponseEntity<List<SpotifyArtistDto>> getTopArtist(@RequestHeader(value = "Authorization", required = false) String authorizationHeader){
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String accessToken = authorizationHeader.substring(7);
            List<SpotifyArtistDto> topArtists = artistService.getTopArtists(accessToken);
            return ResponseEntity.ok(topArtists);
        } catch(RuntimeException ex){
            System.out.println(ex);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<SpotifyArtistDto> getArtistDetails(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String id) {

        if(!authorizationHeader.startsWith("Bearer ")){
            return ResponseEntity.badRequest().build();
        }

        String accessToken = authorizationHeader.substring(7);
        try{
            SpotifyArtistDto artist = artistService.getArtistDetails(accessToken, id);
            return ResponseEntity.ok(artist);
        } catch(RuntimeException e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }
}
