package geektime.starter.http;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class HttpClient {

    private final OkHttpClient okHttpClient;

    public HttpClient(OkHttpClient okHttpClient) {
        Objects.requireNonNull(okHttpClient);
        this.okHttpClient = okHttpClient;
    }

    public Mono<HttpResponse> send(URL url) {
        return Mono.defer(() -> this.sendIntern(url));
    }

    private Mono<HttpResponse> sendIntern(URL url) {
        return Mono.create(sink -> sink.onRequest(
                consumer -> {
                    toOkHttpRequest(url).subscribe(
                            okHttpRequest -> {
                                Call call = okHttpClient.newCall(okHttpRequest);
                                call.enqueue(new OkHttpEnqueueCallback(sink));
                                sink.onCancel(call::cancel);
                            }
                    , sink::error);
                }
        ));
    }

    private static Mono<Request> toOkHttpRequest(URL url) {
        return Mono.just(new Request.Builder())
                    .map(request -> {
                        request.url(url);
                        return request.build();
                    });
    }

    private static class OkHttpEnqueueCallback implements okhttp3.Callback {

        private final MonoSink<HttpResponse> sink;

        OkHttpEnqueueCallback(MonoSink<HttpResponse> sink) {
            this.sink = sink;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            sink.error(e);
        }

        @Override
        public void onResponse(Call call, okhttp3.Response response) {
            sink.success(new HttpResponse(response));
        }
    }

}
