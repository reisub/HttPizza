package sexy.code;

import android.os.Build;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

class HttpEngine {

    private final HttpConfig config;

    HttpEngine(final HttpConfig config) {
        this.config = config;
    }

    public Response execute(final Request request) throws IOException {
        final URL httpUrl = request.getUrl().url();
        final HttpURLConnection urlConnection = (HttpURLConnection) httpUrl.openConnection();

        urlConnection.setRequestMethod(request.getMethod());
        urlConnection.setReadTimeout(config.readTimeout);
        urlConnection.setConnectTimeout(config.connectTimeout);
        urlConnection.setDoInput(true);

        final Map<String, String> headers = request.getHeaders();
        if (headers != null && !headers.isEmpty()) {
            addRequestProperties(urlConnection, headers);
        }

        if (isOutput(request)) {
            doOutput(urlConnection, request.getBody());
        }

        urlConnection.connect();

        final int statusCode = urlConnection.getResponseCode();
        final String message = urlConnection.getResponseMessage();
        final Map<String, List<String>> responseHeaders = urlConnection.getHeaderFields();
        final ResponseBody body = doInput(urlConnection);
        return new Response(request, statusCode, message, responseHeaders, body);
    }

    public Executor httpExecutor() {
        return config.httpExecutor;
    }

    public Executor callbackExecutor() {
        return config.callbackExecutor;
    }

    private static void addRequestProperties(final HttpURLConnection urlConnection, final Map<String, String> headers) {
        for (final Map.Entry<String, String> entry : headers.entrySet()) {
            final String value = entry.getValue();
            if (value != null) {
                urlConnection.addRequestProperty(entry.getKey(), value);
            }
        }
    }

    private static boolean isOutput(final Request request) {
        if (request.getBody() == null) {
            return false;
        }
        final String method = request.getMethod();
        return Request.METHOD_POST.equals(method) || Request.METHOD_PUT.equals(method);
    }

    private static void doOutput(final HttpURLConnection urlConnection, final RequestBody body)
            throws IOException {
        urlConnection.setDoOutput(true);
        final String contentType = body.contentType().toString();
        if (contentType != null) {
            urlConnection.addRequestProperty("Content-Type", contentType);
        }
        final long contentLength = body.contentLength();
        if (contentLength > 0) {
            setFixedLengthStreamingMode(urlConnection, contentLength);
            urlConnection.addRequestProperty("Content-Length", Long.toString(contentLength));
        } else {
            urlConnection.setChunkedStreamingMode(0);
        }
        BufferedOutputStream os = null;
        try {
            os = new BufferedOutputStream(urlConnection.getOutputStream());
            body.writeTo(os);
        } finally {
            Util.closeQuietly(os);
        }
    }

    private static ResponseBody doInput(final HttpURLConnection urlConnection) throws IOException {
        final String contentType = urlConnection.getContentType();
        final long contentLength = urlConnection.getContentLength();
        return new ResponseBody(contentType, contentLength, new BufferedInputStream(getResponseStream(urlConnection)), urlConnection);
    }

    private static InputStream getResponseStream(final HttpURLConnection urlConnection) {
        try {
            return urlConnection.getInputStream();
        } catch (final IOException e) {
            return urlConnection.getErrorStream();
        }
    }

    private static void setFixedLengthStreamingMode(final HttpURLConnection urlConnection, final long contentLength) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            urlConnection.setFixedLengthStreamingMode(contentLength);
        } else {
            urlConnection.setFixedLengthStreamingMode((int) contentLength);
        }
    }
}
