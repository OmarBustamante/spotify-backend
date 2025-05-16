package com.encora.spotify.controller;

import com.encora.spotify.dto.SpotifyArtistDto;
import com.encora.spotify.service.ArtistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArtistController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistService artistService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnTopArtists() throws Exception {
        String accessToken = "mocked-access-token";
        List<SpotifyArtistDto> artists = List.of(
                new SpotifyArtistDto("1", "Artist 1", List.of("pop"), "https://image.url/artist1.jpg"),
                new SpotifyArtistDto("2", "Artist 2", List.of("rock"), "https://image.url/artist2.jpg")
        );

        Mockito.when(artistService.getTopArtists(accessToken)).thenReturn(artists);

        mockMvc.perform(get("/me/top/artists")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Artist 1"))
                .andExpect(jsonPath("$[0].genres[0]").value("pop"))
                .andExpect(jsonPath("$[0].imageUrl").value("https://image.url/artist1.jpg"));
    }

    @Test
    void shouldReturnUnauthorizedWhenNoTokenProvided() throws Exception{
        mockMvc.perform(get("/me/top/artists"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnBadGatewayWhenSpotifyFails() throws Exception{
        Mockito.when(artistService.getTopArtists(Mockito.anyString()))
                .thenThrow(new RuntimeException("Spotify API failed"));

        mockMvc.perform(get("/me/top/artists")
                .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isBadGateway());
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoTopArtists() throws Exception{
        String token = "Bearer valid-token";

        Mockito.when(artistService.getTopArtists(token))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/me/top/artists")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // artist/{id}
    @Test
    void shouldReturnArtistDetailsWhenIdIsValid() throws Exception{
        String token = "Bearer valid-token";
        String artistId = "12345";

        SpotifyArtistDto artist = new SpotifyArtistDto("12345", "Artist name", List.of("pop"), "https://example.com/image.jpg");

        Mockito.when(artistService.getArtistDetails("valid-token", artistId)).thenReturn(artist);

        mockMvc.perform(get("/artists/{id}", artistId)
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("12345"))
                .andExpect(jsonPath("$.name").value("Artist name"))
                .andExpect(jsonPath("$.genres[0]").value("pop"))
                .andExpect(jsonPath("$.imageUrl").value("https://example.com/image.jpg"));
    }
}
