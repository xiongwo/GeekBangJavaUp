package geektime.starter;

import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import java.net.MalformedURLException;

@Path("/weather")
public class WeatherResource {

    @Inject
    WeatherService weatherService;

    @GET
    @Path("/{cityName}")
    public Uni<String> idUni(String cityName) throws MalformedURLException {
        // 将 Reactor Mono 转换成 Mutiny Uni
        return Uni.createFrom().publisher(weatherService.getWeatherByCityName(cityName));
    }

}
