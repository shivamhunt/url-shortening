package com.url.shorten.api;

import com.url.shorten.dto.*;
import com.url.shorten.service.IUrlShortenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class UrlShortenAPI {
    @Autowired
    private IUrlShortenService service;

    @PostMapping("/shorten")
    public ShortUrlResponse urlShort(@RequestBody ShortUrlRequest req) {
        ShortUrlResponse shortUrlResponse = service.shortUrl(req);
        return shortUrlResponse;

    }

    @GetMapping("/{alias}")
    public ResponseWithCode redirecting(@PathVariable String alias) {

        return service.redirecting(alias);
    }

    //
    @GetMapping("/analytics/{alias}")
    public UrlInfo analytics(@PathVariable String alias) {
        return service.analytics(alias);
    }
    @PutMapping("/update/{alias}")
    public ResponseWithCode updateUrl(@PathVariable String alias, @RequestBody UpdateUrlRequest request) {
        log.info(" alias {}",alias);
        return service.updateUrl(alias, request);
    }

    //
    @DeleteMapping("/delete/{alias}")
    public ResponseWithCode deleteUrl(@PathVariable String alias) {
        return service.deleteUrl(alias);
    }
}
