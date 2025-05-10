package com.encora.spotify.dto;

public class SpotifyAuthRequestDto {
    private String code;
    private String redirectUri;

    //Jackson (deserializacion JSON)
    public SpotifyAuthRequestDto(){}

    public SpotifyAuthRequestDto(String code, String redirectUri){
        this.code = code;
        this.redirectUri = redirectUri;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getRedirectUri(){
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri){
        this.redirectUri = redirectUri;
    }
}
