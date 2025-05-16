package com.encora.spotify.dto;

import java.util.List;

public class SpotifyTrackDto {
    private String id;
    private String name;
    private List<String> artists;
    private String album;
    private int durationMs;
    private String previewUrl;


    public SpotifyTrackDto(String id, String name, List<String> artists, String album, int durationMs, String previewUrl){
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.album = album;
        this.durationMs = durationMs;
        this.previewUrl = previewUrl;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }
}
