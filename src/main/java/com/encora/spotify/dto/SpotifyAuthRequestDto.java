package com.encora.spotify.dto;

import jakarta.validation.constraints.NotBlank;

public class SpotifyAuthRequestDto {

    @NotBlank(message = "Code must not be blank")
    private String code;

    @NotBlank(message = "Redirect URI must not be blank")
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
