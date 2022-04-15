package org.example.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

@Service
public class HttpService {

    @Autowired
    private RestTemplate restTemplate;

    public String execute(String cityName) throws IOException {
        URI uri = UriComponentsBuilder
                .fromUriString("http://wthrcdn.etouch.cn/weather_mini?city={name}")
                .build(cityName);

        ResponseEntity<byte[]> result = restTemplate.getForEntity(uri, byte[].class);
        String contentEncoding = result.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING);
        if ("gzip".equalsIgnoreCase(contentEncoding)) {
            GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(result.getBody()));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int len;
            while ((len = gzipInputStream.read(buf, 0, buf.length)) != -1) {
                byteArrayOutputStream.write(buf, 0, len);
            }
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return new String(bytes, StandardCharsets.UTF_8);
        }

        return null;
    }

}
