package com.encora.spotify.dto;

import java.util.List;

public class SearchResultDto {
    private List<SpotifyArtistDto> artists;
    private List<SpotifyAlbumDto> albums;
    private List<SpotifyTrackDto> tracks;

    public SearchResultDto(List<SpotifyArtistDto> artists, List<SpotifyAlbumDto> albums, List<SpotifyTrackDto> tracks){
        this.artists = artists;
        this.albums = albums;
        this.tracks = tracks;
    }

    public List<SpotifyArtistDto> getArtists() {
        return artists;
    }

    public void setArtists(List<SpotifyArtistDto> artists) {
        this.artists = artists;
    }

    public List<SpotifyAlbumDto> getAlbums() {
        return albums;
    }

    public void setAlbums(List<SpotifyAlbumDto> albums) {
        this.albums = albums;
    }

    public List<SpotifyTrackDto> getTracks() {
        return tracks;
    }

    public void setTracks(List<SpotifyTrackDto> tracks) {
        this.tracks = tracks;
    }
}
