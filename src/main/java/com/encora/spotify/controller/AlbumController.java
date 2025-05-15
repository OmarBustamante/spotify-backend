package com.encora.spotify.controller;

import com.encora.spotify.dto.SpotifyAlbumDto;
import com.encora.spotify.service.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService){
        this.albumService = albumService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpotifyAlbumDto> getAlbumById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String id) {

        if(!authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        try {
            String accessToken = authorizationHeader.substring(7);
            SpotifyAlbumDto album = albumService.getAlbumById(accessToken, id);
            return ResponseEntity.ok(album);
        } catch (RuntimeException e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }
}
