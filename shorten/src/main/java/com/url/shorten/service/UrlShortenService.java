package com.url.shorten.service;

import com.url.shorten.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UrlShortenService implements IUrlShortenService {
    @Autowired
    MapStore mapStore;


    @Override
    public ShortUrlResponse shortUrl(ShortUrlRequest req) {
        int ttl = req.getTtl();
        String longUrl = req.getLongUrl();
        ShortUrlResponse response = new ShortUrlResponse();
        if (ttl <= 0) {
            ttl = 120;
        }
        Date date = new Date();
        long time = date.getTime();
//        System.out.print(" timestamp "+time);

        String alias = req.getAlias();
        if (alias == null) {
            alias = String.valueOf(time);
        }
        String shortUrl = "http://localhost:8080/" + alias;
        response.setShortUrl(shortUrl);

        UrlInfo urlInfo = new UrlInfo();
        urlInfo.setLongUrl(longUrl);
        urlInfo.setExpireTime(ttl + time);
        Map<String, UrlInfo> urlMap = mapStore.getMap();
        urlMap.put(String.valueOf(alias), urlInfo);
        return response;
    }

    @Override
    public ResponseWithCode redirecting(String alias) {
        log.info(" alias is ", alias);
        ResponseWithCode response = new ResponseWithCode();
        Map<String, UrlInfo> urlMap = mapStore.getMap();
        System.out.println("current map is " + urlMap);
        if (urlMap.containsKey(alias)) {
            UrlInfo urlInfo = urlMap.get(alias);
            String longUrl = urlInfo.getLongUrl();

            long expireTime = urlInfo.getExpireTime();
            long time = new Date().getTime();
            List<Long> accessTime = urlInfo.getAccessTime();

            if (time < expireTime) {
                log.info("url expired");
                response.setErrorCode(404);
                response.setErrorMessage("Not Found");
            }
//            accessTime.add(new Date().getTime());
//            urlInfo.setAccessTime(accessTime);
            int accessCount = urlInfo.getAccessCount();
            accessCount++;
            urlInfo.setAccessCount(accessCount);
            urlMap.put(alias,urlInfo);
            response.setErrorCode(302);
            response.setErrorMessage(longUrl);
        } else {
            log.info("alias not found");
            response.setErrorCode(404);
            response.setErrorMessage("Not Found");
        }


        return response;
    }

    @Override
    public ResponseWithCode updateUrl(String alias, UpdateUrlRequest request) {
        log.info(" alias is ", alias);
        ResponseWithCode response = new ResponseWithCode();
        Map<String, UrlInfo> urlMap = mapStore.getMap();
//        System.out.println("current map is " +urlMap);
        if (urlMap.containsKey(alias)) {
            UrlInfo urlInfo = urlMap.get(alias);
            String longUrl = urlInfo.getLongUrl();
            long expireTime = urlInfo.getExpireTime();
            long time = new Date().getTime();
            if (time > expireTime) {
                log.info("url expired");
                response.setErrorCode(404);
                response.setErrorMessage("Not Found");

            }
            // updating the url
            UrlInfo newUrl = new UrlInfo();
            newUrl.setLongUrl(urlInfo.getLongUrl());
            int ttl = request.getTtl();
            if (ttl <= 0) {
                ttl = 120;
            }
            newUrl.setExpireTime(new Date().getTime() + ttl);
            String newAlias = request.getCustomAlias();
            if (newAlias == null) {
                newAlias = String.valueOf(time);
            }

            urlMap.put(newAlias, newUrl);
            urlMap.remove(alias);
            response.setErrorCode(200);
            response.setErrorMessage(longUrl);
        } else {
            log.info("alias not found");
            response.setErrorCode(404);
            response.setErrorMessage("Not Found");
        }
        return response;
    }

    @Override
    public ResponseWithCode deleteUrl(String alias) {
        log.info(" alias is ", alias);
        ResponseWithCode response = new ResponseWithCode();
        Map<String, UrlInfo> urlMap = mapStore.getMap();
        System.out.println("current map is " + urlMap);
        if (urlMap.containsKey(alias)) {
            UrlInfo urlInfo = urlMap.get(alias);
            String longUrl = urlInfo.getLongUrl();
            long expireTime = urlInfo.getExpireTime();
            long time = new Date().getTime();

            if (time > expireTime) {
                log.info("url expired");
                response.setErrorCode(404);
                response.setErrorMessage("Not Found");

            }
            urlMap.remove(alias);
            response.setErrorCode(200);
            response.setErrorMessage("OK");
        } else {
            log.info("alias not found");
            response.setErrorCode(404);
            response.setErrorMessage("Not Found");
        }
        return response;
    }
    @Override
    public UrlInfo analytics(String alias) {
        Map<String, UrlInfo> urlMap = mapStore.getMap();
       return urlMap.get(alias);
    }
}
