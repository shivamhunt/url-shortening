package com.url.shorten;

import com.url.shorten.dto.UrlInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ShortenApplication {
//	public   Map<String, UrlInfo> urlMap = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(ShortenApplication.class, args);
	}

}
