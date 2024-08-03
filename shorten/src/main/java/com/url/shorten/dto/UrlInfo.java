package com.url.shorten.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UrlInfo {
    private String longUrl;
    private String alias;
    long   expireTime;
    private int  accessCount;
    private List<Long> accessTime;
}
