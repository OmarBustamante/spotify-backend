package com.encora.spotify.controller;


import com.encora.spotify.dto.SpotifyAlbumDto;
import com.encora.spotify.service.AlbumService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

@WebMvcTest(AlbumController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumService albumService;

    @Test
    void shouldReturnAlbumDetailsWhenAuthorized() throws Exception{
        String token = "Bearer valid-token";
        String albumId = "qweasdzxc";

        SpotifyAlbumDto albumDto = new SpotifyAlbumDto(
                albumId,
                "Album Title",
                List.of("Artist 1"),
                "https://image.url/cover.jpg",
                "2020-0101"
        );

        Mockito.when(albumService.getAlbumById("valid-token", albumId)).thenReturn(albumDto);

        mockMvc.perform(get("/albums/{id}", albumId)
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(albumId))
                .andExpect(jsonPath("$.name").value("Album Title"));
    }
}
