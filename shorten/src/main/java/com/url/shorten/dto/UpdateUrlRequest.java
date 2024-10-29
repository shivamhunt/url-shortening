package com.url.shorten.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUrlRequest {
    private String customAlias;
    private String originalUrl;
}
