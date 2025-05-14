package com.encora.spotify.dto;

import java.util.List;

public class SpotifyArtistDto {
    private String id;
    private String name;
    private List<String> genres;
    private String imageUrl;

    public SpotifyArtistDto(){}

    public SpotifyArtistDto(String id, String name, List<String> genres, String imageUrl){
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
