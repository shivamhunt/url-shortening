package com.url.shorten.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ShortUrlResponse implements Serializable {
    private String shortUrl;
}
