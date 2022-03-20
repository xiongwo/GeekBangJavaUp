package geektime.starter.http;

import okhttp3.Response;
import okhttp3.ResponseBody;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.io.Closeable;
import java.io.IOException;

public final class HttpResponse implements Closeable {

    private final Mono<ResponseBody> responseBodyMono;

    HttpResponse(Response innerResponse) {
        this.responseBodyMono = innerResponse.body() == null ? Mono.empty() : Mono.just(innerResponse.body());
    }

    public Mono<String> getBodyAsString() {
        return this.responseBodyMono.flatMap(responseBody -> {
            try {
                String content = responseBody.string();
                return content.length() == 0 ? Mono.empty() : Mono.just(content);
            } catch (IOException ioe) {
                throw Exceptions.propagate(ioe);
            }
        });
    }

    @Override
    public void close() {
        this.responseBodyMono.subscribe().dispose();
    }

}