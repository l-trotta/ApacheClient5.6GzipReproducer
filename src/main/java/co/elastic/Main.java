package co.elastic;


import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;

public class Main {

    public static final int DEFAULT_BUFFER_LIMIT = 100 * 1024 * 1024;

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
            //.disableContentCompression()
            .setDefaultHeaders(List.of(new BasicHeader("Accept-Encoding", "gzip")))
            .build()) {

            // Start the client
            httpclient.start();

            // Set producer and consumer
            AsyncRequestProducer requestProducer = AsyncRequestBuilder
                .create("GET")
                .setHttpHost(HttpHost.create("http://www.google.com"))
                .setHeaders().build();

            AsyncResponseConsumer<ClassicHttpResponse> asyncResponseConsumer =
                new BasicAsyncResponseConsumer(new BufferedByteConsumer(DEFAULT_BUFFER_LIMIT));

            // Execute request
            ClassicHttpResponse httpResponse = httpclient.execute(requestProducer,
                asyncResponseConsumer,
                null, null).get();

            // entity content encoding always null
            assertNull(httpResponse.getEntity().getContentEncoding());
            // header content-encoding always present
            assertNotNull(httpResponse.getHeader("content-encoding"));
        } catch (URISyntaxException | ProtocolException e) {
            throw new RuntimeException(e);
        }
    }
}
