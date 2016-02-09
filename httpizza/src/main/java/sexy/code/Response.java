package sexy.code;

import java.util.List;
import java.util.Map;

public class Response {

    private final HttpResponse origin;

    private final ResponseBody body;

    Response(HttpResponse origin, ResponseBody body) {
        this.origin = origin;
        this.body = body;
    }

    public HttpResponse getOrigin() {
        return origin;
    }

    public Request getRequest() {
        return origin.getRequest();
    }

    public boolean isSuccessful() {
        return origin.isSuccess();
    }

    public int statusCode() {
        return origin.getCode();
    }

    public String message() {
        return origin.getMessage();
    }

    public Map<String, List<String>> headers() {
        return origin.getHeaders();
    }

    public ResponseBody body() {
        return body;
    }
}
