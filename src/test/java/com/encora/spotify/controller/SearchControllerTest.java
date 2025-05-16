package com.encora.spotify.controller;

import com.encora.spotify.dto.SearchResultDto;
import com.encora.spotify.dto.SpotifyAlbumDto;
import com.encora.spotify.dto.SpotifyArtistDto;
import com.encora.spotify.dto.SpotifyTrackDto;
import com.encora.spotify.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    private final String token = "Bearer valid-token";

    @BeforeEach
    void setUp(){
        Mockito.reset(searchService);
    }

    @Test
    void shouldReturnArtistsWhenTypeIsArtist() throws Exception{
        List<SpotifyArtistDto> mockArtists = List.of(new SpotifyArtistDto("1", "Turnstile", List.of("punk"), "image.jpg"));
        Mockito.when(searchService.searchArtists(anyString(), anyString())).thenReturn(mockArtists);

        mockMvc.perform(get("/search?q=turnstile&type=artist")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Turnstile"));
    }

    @Test
    void shouldReturnAlbumsWhenTypeIsAlbum() throws Exception{
        List<SpotifyAlbumDto> mockAlbums = List.of(new SpotifyAlbumDto("1", "Glow On", List.of("Turnstile"), "cover.jpg", "2020-10-10"));
        Mockito.when(searchService.searchAlbums(anyString(), anyString())).thenReturn(mockAlbums);

        mockMvc.perform(get("/search?q=glow+on&type=album")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Glow On"));
    }

    @Test
    void shouldReturnTracksWhenTypeIsTrack() throws Exception {
        List<SpotifyTrackDto> mockTracks = List.of(new SpotifyTrackDto("1", "Mystery", List.of("Turnstile"), "Glow On", 410, "album.jpg"));
        Mockito.when(searchService.searchTracks(anyString(), anyString())).thenReturn(mockTracks);

        mockMvc.perform(get("/search?q=mystery&type=track")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mystery"));
    }

    @Test
    void shouldReturnBadRequestWhenTypeIsInvalid() throws Exception{
        mockMvc.perform(get("/search?q=turnstile&type=invalid")
                .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid search type. Must be: artist, album, track"));
    }

    @Test
    void shouldReturnBadRequestWhenNoAuthorizationHeader() throws Exception{
        mockMvc.perform(get("/search?q=turnstile&type=artist"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

}
