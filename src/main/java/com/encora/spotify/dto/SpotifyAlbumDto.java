package com.encora.spotify.dto;

import java.util.List;

public class SpotifyAlbumDto {
    private String id;
    private String name;
    private List<String> artists;
    private String imageUrl;
    private String releaseDate;
    private List<SpotifyTrackDto> tracks;

    public SpotifyAlbumDto(){}

    public SpotifyAlbumDto(String id, String name, List<String> artists, String imageUrl, String releaseDate, List<SpotifyTrackDto> tracks) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.imageUrl = imageUrl;
        this.releaseDate = releaseDate;
        this.tracks = tracks;
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

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<SpotifyTrackDto> getTracks() {
        return tracks;
    }

    public void setTracks(List<SpotifyTrackDto> tracks) {
        this.tracks = tracks;
    }
}
