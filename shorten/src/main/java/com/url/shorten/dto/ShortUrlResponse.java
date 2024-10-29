package com.url.shorten.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ShortUrlResponse implements Serializable {
//    @JsonIgnore(Unknow =true)
    private String shortUrl;
}
