package com.url.shorten.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@Setter
public class MapStore {
    private Map<String, UrlInfo> map = new HashMap<>();

}
