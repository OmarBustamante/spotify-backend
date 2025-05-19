package com.encora.spotify.service.impl;

import com.encora.spotify.dto.SpotifyArtistDto;
import com.encora.spotify.service.ArtistService;
import org.apache.coyote.Response;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final RestTemplate restTemplate;

    public ArtistServiceImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public List<SpotifyArtistDto> getTopArtists(String accessToken){
        //Fake response
        /*return List.of(
                new SpotifyArtistDto("1", "Artist 1", List.of("pop"), "https://image.url/artist1.jpg"),
                new SpotifyArtistDto("2", "Artist 2", List.of("rock"), "https://image.url/artist2.jpg")
        );*/

        //real response
        String url = "https://api.spotify.com/v1/me/top/artists?limit=10";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try{
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            Map<String, Object> body = response.getBody();
            if (body == null || !body.containsKey("items")){
                throw new RuntimeException("Invalid response from Spotify");
            }

            List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
            List<SpotifyArtistDto> artists = new ArrayList<>();

            for (Map<String, Object> item : items){
                String id = (String) item.get("id");
                String name = (String) item.get("name");

                List<String> genres = (List<String>) item.getOrDefault("genres", List.of());

                String imageUrl = "";
                List<?> images = (List<?>) item.get("images");
                if(images != null && !images.isEmpty()){
                    Map<?, ?> image = (Map<?, ?>) images.get(0);
                    imageUrl = (String) image.get("url");
                }

                SpotifyArtistDto artist = new SpotifyArtistDto(
                        id,
                        name,
                        genres,
                        imageUrl,
                        null
                );
                artists.add(artist);
            }

            return artists;
        } catch(HttpClientErrorException e){
            throw new RuntimeException("Spotify API error: " + e.getMessage(), e);
        }
    }

    @Override
    public SpotifyArtistDto getArtistDetails(String accessToken, String artistId){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try{
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.spotify.com/v1/artists/" + artistId,
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            Map<String, Object> body = Optional.ofNullable(response.getBody())
                    .orElseThrow(() -> new RuntimeException("Empty response from Spotify"));

            String id = (String) body.get("id");
            String name = (String) body.get("name");

            List<String> genres = Optional.ofNullable((List<?>) body.get("genres"))
                    .orElse(List.of()).stream()
                    .map(Object::toString)
                    .toList();

            List<Map<String, Object>> images = Optional.ofNullable((List<Map<String, Object>>) body.get("images"))
                    .orElse(List.of());

            String imageUrl = images.isEmpty() ? null : (String) images.get(0).get("url");

            Map<String, Object> followersMap = (Map<String, Object>) body.get("followers");
            Integer followers = followersMap != null ? (Integer) followersMap.get("total") : null;

            return new SpotifyArtistDto(id, name, genres, imageUrl, followers);
        } catch (HttpClientErrorException e){
            throw new RuntimeException("Spotify API failed: " + e.getResponseBodyAsString(), e);
        }
    }
}
