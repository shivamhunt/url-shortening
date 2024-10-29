package com.url.shorten.service;

import com.url.shorten.dto.*;
import com.url.shorten.repo.AccessTimeRepo;
import com.url.shorten.repo.UrlRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class UrlShortenService implements IUrlShortenService {

    @Autowired
    UrlRepo urlRepo;
    @Autowired
    AccessTimeRepo accessTimeRepo;
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    @Override
    public ShortUrlResponse shortUrl(ShortUrlRequest req) {
        int ttl = req.getTtl();
        String longUrl = req.getLongUrl();
        ShortUrlResponse response = new ShortUrlResponse();
        if (ttl <= 0) {
            ttl = 120000;
        }
        Date date = new Date();

        String alias = req.getAlias();
        int maxId = 0;
        if (urlRepo.getMaxId().isEmpty()) {

        } else {
            maxId = urlRepo.getMaxId().get();
        }
        if (alias == null) {
            alias = encode(maxId);
        } else {
            if (urlRepo.getUrlObj(alias).isPresent()) {
                alias = encode(maxId);
            }
        }
        String shortUrl = "http://localhost:8080/" + alias;
        response.setShortUrl(shortUrl);
        Url urlInfo = new Url();
        urlInfo.setOriginalUrl(longUrl);
        urlInfo.setExpireTime(date.getTime() + ttl);
        urlInfo.setAccessCount(1);
        urlInfo.setAlias(alias);


        AccessTime acessTime = new AccessTime();
        acessTime.setAccessTime(new Date());
        acessTime.setAlias(urlInfo);
        urlInfo.getAccessTime().add(acessTime);

        urlRepo.save(urlInfo);
        return response;
    }

    public String encode(int num) {

        StringBuilder encodedString = new StringBuilder();
        if (num == 0) {
            return "base32start";
        }
        while (num > 0) {
            encodedString.append(BASE62.charAt(num % 62));
            num /= 62;
        }
        return encodedString.reverse().toString();
    }

    @Override
    public ResponseWithCode redirecting(String alias) {
        log.info(" alias is ", alias);
        ResponseWithCode response = new ResponseWithCode();

        // invalid alias
        if (alias == null || alias.isEmpty()) {
            response.setErrorMessage("Alias cannot be null");
            response.setErrorCode(422);
            return response;
        }
        if (urlRepo.getUrlObj(alias).isEmpty()) {
            log.info("alias not found");
            response.setErrorCode(404);
            response.setErrorMessage("Not Found");
            return response;
        }

        Url url = urlRepo.getUrlObj(alias).get();
        String originalUrl = url.getOriginalUrl();
        long currentTime = new Date().getTime();
        long expireTime = url.getExpireTime();
        if (currentTime < expireTime) {
            log.info("url expired");
            response.setErrorCode(404);
            response.setErrorMessage("url expired");
            return response;
        }
        response.setErrorMessage(originalUrl);
        response.setErrorCode(200);
        int accessCount = url.getAccessCount() + 1;

        List<AccessTime> accessTime = url.getAccessTime();
        AccessTime newAccessTime = new AccessTime();
        newAccessTime.setAccessTime(new Date());
        newAccessTime.setAlias(url);
        accessTime.add(newAccessTime);
        url.setAccessTime(accessTime);
        url.setAccessCount(accessCount);
        urlRepo.save(url);
        return response;
    }


    //
    @Override
    public ResponseWithCode updateUrl(String alias, UpdateUrlRequest request) {
        log.info(" alias is {} ", alias);
        log.info(" alias length {}", alias.length());
        ResponseWithCode response = new ResponseWithCode();

        // invalid alias
        if (alias == null || alias.isEmpty()) {
            response.setErrorMessage("Alias cannot be null");
            response.setErrorCode(422);
            return response;
        }
        if (urlRepo.getUrlObj(alias).isEmpty()) {
            log.info("alias not found");
            response.setErrorCode(404);
            response.setErrorMessage("Not Found");
            return response;
        }

        Url url = urlRepo.getUrlObj(alias).get();
        long currentTime = new Date().getTime();
        long expireTime = url.getExpireTime();
        if (currentTime > expireTime) {
            log.info("url expired");
            response.setErrorCode(404);
            response.setErrorMessage("url expired");
            return response;
        }
        String originalUrl = request.getOriginalUrl();
        String customAlias = request.getCustomAlias();
        if (!(originalUrl == null || originalUrl.isEmpty())) {
            url.setOriginalUrl(originalUrl);
        }
        if (!(customAlias == null || customAlias.isEmpty())) {
            url.setAlias(customAlias);
        }
        urlRepo.save(url);
        response.setErrorCode(200);
        response.setErrorMessage(HttpStatus.OK.name());
        return response;
    }

    //
    @Override
    public ResponseWithCode deleteUrl(String alias) {
        log.info(" alias is ", alias);
        ;
        ResponseWithCode response = new ResponseWithCode();

        // invalid alias
        if (alias == null || alias.isEmpty()) {
            response.setErrorMessage("Alias cannot be null");
            response.setErrorCode(422);
            return response;
        }
        if (urlRepo.getUrlObj(alias).isEmpty()) {
            log.info("alias not found");
            response.setErrorCode(404);
            response.setErrorMessage("Not Found");
            return response;
        }
        Url url = urlRepo.getUrlObj(alias).get();
        long expireTime = url.getExpireTime();
        if (new Date().getTime() > expireTime) {
            log.info("url expired");
            response.setErrorCode(404);
            response.setErrorMessage("url expired");
            return response;
        }
        urlRepo.deleteARecordByAlias(alias);
        response.setErrorMessage("Url deleted successfully");
        response.setErrorCode(200);
        return response;
    }

    @Override
    public UrlInfo analytics(String alias) {
        log.info(" alias is ", alias);
        ResponseWithCode errorCode = new ResponseWithCode();

        // invalid alias
        UrlInfo response = new UrlInfo();
        if (alias == null || alias.isEmpty()) {
            errorCode.setErrorMessage("Alias cannot be null");
            errorCode.setErrorCode(422);
            response.setResponseCode(errorCode);
            return response;
        }
        if (urlRepo.getUrlObj(alias).isEmpty()) {
            log.info("alias not found");
            errorCode.setErrorCode(404);
            errorCode.setErrorMessage("Not Found");
            response.setResponseCode(errorCode);
            return response;
        }

        Optional<Url> urlObj = urlRepo.getUrlObj(alias);
        Url url = urlObj.get();
        if(new Date().getTime()>url.getExpireTime()){
            log.info("url expired");
            errorCode.setErrorCode(501);
            errorCode.setErrorMessage("url expired");
            response.setResponseCode(errorCode);
            return response;
        }

        response.setLongUrl(url.getOriginalUrl());
        response.setAlias(url.getAlias());
        response.setExpireTime(url.getExpireTime());
        List<AccessTime> accessTime = url.getAccessTime();
//        accessTime.stream().forEach(a-> a.getAccessTime());
        List<Date> time=new ArrayList<>();
        for(AccessTime t:accessTime){
            time.add(t.getAccessTime());
        }
        response.setAccessTime(time);
        response.setAccessCount(url.getAccessCount());
        return response;

    }
}
