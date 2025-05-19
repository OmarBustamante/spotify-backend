package com.encora.spotify.service.impl;

import com.encora.spotify.dto.SpotifyAlbumDto;
import com.encora.spotify.dto.SpotifyTrackDto;
import com.encora.spotify.service.AlbumService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImpl implements AlbumService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public SpotifyAlbumDto getAlbumById(String accessToken, String albumId){
        String url = "https://api.spotify.com/v1/albums/" + albumId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                Map.class
        );

        if(!response.getStatusCode().is2xxSuccessful() || response.getBody() == null){
            throw new RuntimeException("Failed to fetch album details from Spotify");
        }

        try {
            Map<String, Object> body = response.getBody();

            String name = (String) body.get("name");
            String releaseDate = (String) body.get("release_date");

            List<Map<String, Object>> artistList = (List<Map<String, Object>>) body.get("artists");
            List<String> artists = artistList.stream()
                    .map(artist -> (String) artist.get("name"))
                    .collect(Collectors.toList());

            List<String> artistsId = artistList.stream()
                    .map(artist -> (String) artist.get("id"))
                    .collect(Collectors.toList());

            List<Map<String, Object>> images = (List<Map<String, Object>>) body.get("images");
            String imageUrl = images != null && !images.isEmpty() ? (String) images.get(0).get("url") : null;

            Map<String, Object> trackMap = (Map<String, Object>) body.get("tracks");
            List<Map<String, Object>> trackItems = (List<Map<String, Object>>) trackMap.get("items");
            List<SpotifyTrackDto> tracks = trackItems.stream()
                    .map(track -> {
                        String trackId = (String) track.get("id");
                        String trackName = (String) track.get("name");
                        int durationMs = (int) track.get("duration_ms");
                        String previewUrl = (String) track.get("preview_url");
                        String albumName = name;

                        List<Map<String, Object>> trackArtists = (List<Map<String, Object>>) track.get("artists");
                        List<String> artistNames = trackArtists.stream()
                                .map(artist -> (String) artist.get("name"))
                                .collect(Collectors.toList());

                        return new SpotifyTrackDto(
                                trackId,
                                trackName,
                                artistNames,
                                albumName,
                                durationMs,
                                previewUrl
                        );
                    }).collect(Collectors.toList());

            return new SpotifyAlbumDto(
                    albumId,
                    name,
                    artists,
                    imageUrl,
                    releaseDate,
                    tracks,
                    artistsId
            );
        } catch (HttpClientErrorException e){
            throw new RuntimeException("Spotify API error: " + e.getMessage(), e);
        }
    }
}
