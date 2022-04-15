package org.example.cache;

import lombok.extern.slf4j.Slf4j;
import org.example.cache.service.CacheService;
import org.example.cache.service.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
@Slf4j
@EnableCaching
public class CustomerServiceApplication implements ApplicationRunner {

    @Autowired
    private HttpService httpService;

    @Autowired
    private CacheService cacheService;

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(CustomerServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start("没缓存");
        callDirect("北京", 50);
        callDirect("上海", 40);
        callDirect("广州", 30);
        callDirect("深圳", 20);
        callDirect("佛山", 10);
        stopWatch.stop();

        stopWatch.start("有缓存");
        getCache("北京", 50);
        getCache("上海", 40);
        getCache("广州", 30);
        getCache("深圳", 20);
        getCache("佛山", 10);
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());
        /*
        *
        -----------------------------------------
        ms     %     Task name
        -----------------------------------------
        01784  097%  没缓存
        00054  003%  有缓存
        * */
    }

    private void callDirect(String cityName, int count) throws IOException {
        while (count > 0) {
            httpService.execute(cityName);
            count--;
        }
    }

    private void getCache(String cityName, int count) throws IOException {
        while (count > 0) {
            cacheService.get(cityName);
            count--;
        }
    }

}
