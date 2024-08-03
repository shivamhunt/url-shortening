package com.url.shorten.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlRequest  implements Serializable {
    private String longUrl;
    private String alias;
    private int ttl;
}














