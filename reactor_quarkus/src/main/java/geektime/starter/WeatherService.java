package geektime.starter;

import geektime.starter.http.HttpClient;
import geektime.starter.http.HttpResponse;
import okhttp3.OkHttpClient;
import reactor.core.publisher.Mono;

import javax.enterprise.context.ApplicationScoped;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

@ApplicationScoped
public class WeatherService {

    public Mono<String> getWeatherByCityName(String cityName) throws MalformedURLException {
        HttpClient httpClient = new HttpClient(new OkHttpClient.Builder().build());
        Mono<HttpResponse> responseMono = httpClient.send(new URL("http://wthrcdn.etouch.cn/weather_mini?city=" + cityName));
        return responseMono.flatMap((Function<HttpResponse, Mono<String>>) HttpResponse::getBodyAsString);
    }

}
