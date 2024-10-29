package com.url.shorten.service;
//
//import com.url.shorten.dto.*;

import com.url.shorten.dto.*;

public interface IUrlShortenService {
    public ShortUrlResponse shortUrl(ShortUrlRequest req);
    public ResponseWithCode redirecting(String alias);
    public ResponseWithCode updateUrl(String alias, UpdateUrlRequest request);
    public ResponseWithCode deleteUrl(String alias);
    public UrlInfo analytics(String alias);
    }

