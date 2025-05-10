package com.encora.spotify.controller;

import com.encora.spotify.dto.SpotifyAuthRequestDto;
import com.encora.spotify.dto.SpotifyAuthResponseDto;
import com.encora.spotify.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnTokensWhenCodeIsValid() throws Exception {
        SpotifyAuthRequestDto request = new SpotifyAuthRequestDto("valid-code", "http://localhost:3000/callback");
        SpotifyAuthResponseDto response = new SpotifyAuthResponseDto("access-token", "refresh-token", 3600);

        Mockito.when(authService.exchangeCodeForToken(request)).thenReturn(response);

        mockMvc.perform(post("/auth/spotify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"))
                .andExpect(jsonPath("$.expiresIn").value(3600));
    }
}
