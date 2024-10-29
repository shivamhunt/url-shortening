package com.url.shorten.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UrlInfo {

    private String longUrl;
    private String alias;
    private long   expireTime;
    private int  accessCount;
    private List<Date> accessTime;
    private ResponseWithCode responseCode;
}
