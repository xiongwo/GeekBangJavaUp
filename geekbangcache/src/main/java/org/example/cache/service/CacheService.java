package org.example.cache.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@CacheConfig(cacheNames = "cityWeather")
public class CacheService {

    @Autowired
    private HttpService httpService;

    @Cacheable(key = "#cityName")
    public String get(String cityName) throws IOException {
        log.info("city: " + cityName);
        return httpService.execute(cityName);
    }

}
