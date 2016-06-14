package net.kimleo.grabbie.client.component;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class IgnoreHttpStatusHandler implements ResponseErrorHandler {
    private final ResponseErrorHandler originalHandler;
    private final HttpStatus[] ignoredStatuses;

    public IgnoreHttpStatusHandler(ResponseErrorHandler originalHandler, HttpStatus... ignoredStatuses) {
        this.originalHandler = originalHandler;
        this.ignoredStatuses = ignoredStatuses;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = response.getStatusCode();
        for (HttpStatus ignoredStatus : ignoredStatuses) {
            if (statusCode.equals(ignoredStatus)) {
                return false;
            }
        }
        return originalHandler.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        originalHandler.handleError(response);
    }
}
