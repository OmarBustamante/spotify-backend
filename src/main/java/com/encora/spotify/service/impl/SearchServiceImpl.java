package com.encora.spotify.service.impl;

import com.encora.spotify.dto.SpotifyAlbumDto;
import com.encora.spotify.dto.SpotifyArtistDto;
import com.encora.spotify.dto.SpotifyTrackDto;
import com.encora.spotify.service.SearchService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpEntity<Void> createHeaders(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        return new HttpEntity<>(headers);
    }

    @Override
    public List<SpotifyArtistDto> searchArtists(String query, String accessToken){
        String url = "https://api.spotify.com/v1/search?q=" + query + "&type=artist&limit=10";
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, createHeaders(accessToken), Map.class);

        Map<String, Object> body = Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new RuntimeException("Empty response from Spotify"));

        Map<String, Object> artistsMap = (Map<String, Object>) body.get("artists");
        List<Map<String, Object>> items = (List<Map<String, Object>>) artistsMap.get("items");

        return items.stream().map(item -> {

            List<Map<String, Object>> images = (List<Map<String, Object>>) item.get("images");
            String imageUrl = images.isEmpty() ? null : (String) images.get(0).get("url");

            return new SpotifyArtistDto(
                    (String) item.get("id"),
                    (String) item.get("name"),
                    (List<String>) item.get("genres"),
                    imageUrl
            );
        }).collect(Collectors.toList());
    }

    @Override
    public List<SpotifyAlbumDto> searchAlbums(String query, String accessToken){
        String url = "https://api.spotify.com/v1/search?q=" + query + "&type=album&limit=10";
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, createHeaders(accessToken), Map.class);

        Map<String, Object> body = Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new RuntimeException("Empty response from Spotify"));

        Map<String, Object> albumsMap = (Map<String, Object>) body.get("albums");
        List<Map<String, Object>> items = (List<Map<String, Object>>) albumsMap.get("items");

        return items.stream().map(item -> {
            List<Map<String, Object>> artistsList = (List<Map<String, Object>>) item.get("artists");
            List<String> artistNames = artistsList.stream()
                    .map(artist -> (String) artist.get("name"))
                    .collect(Collectors.toList());

            List<Map<String, Object>> images = (List<Map<String, Object>>) item.get("images");
            String imageUrl = images.isEmpty() ? null : (String) images.get(0).get("url");

            return new SpotifyAlbumDto(
                    (String) item.get("id"),
                    (String) item.get("name"),
                    artistNames,
                    imageUrl,
                    (String) item.get("release_date")
            );
        }).collect(Collectors.toList());
    }

    @Override
    public List<SpotifyTrackDto> searchTracks(String query, String accessToken){
        String url = "https://api.spotify.com/v1/search?q=" + query + "&type=track&limit=10";
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, createHeaders(accessToken), Map.class);

        Map<String, Object> body = Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new RuntimeException("Empty response from Spotify"));

        Map<String, Object> tracksMap = (Map<String, Object>) body.get("tracks");
        List<Map<String, Object>> items = (List<Map<String, Object>>) tracksMap.get("items");

        return items.stream().map(item -> {
            List<Map<String, Object>> artistsList = (List<Map<String, Object>>) item.get("artists");
            List<String> artistNames = artistsList.stream()
                    .map(artist -> (String) artist.get("name"))
                    .collect(Collectors.toList());

            Map<String, Object> albumMap = (Map<String, Object>) item.get("album");
            String albumName = (String) item.get("name");

            Integer durationMs = (Integer) item.get("duration_ms");

            String previewUrl = (String) item.get("preview_url");

            return new SpotifyTrackDto(
                (String) item.get("id"),
                (String) item.get("name"),
                artistNames,
                albumName,
                durationMs,
                previewUrl
            );
        }).collect(Collectors.toList());
    }
}
