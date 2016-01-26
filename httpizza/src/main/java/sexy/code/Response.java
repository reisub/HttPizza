package sexy.code;


import java.util.List;
import java.util.Map;

public class Response<T> {

    private final HttpResponse origin;

    private final T body;

    Response(HttpResponse origin, T body) {
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

    public int getCode() {
        return origin.getCode();
    }

    public Map<String, List<String>> getHeaders() {
        return origin.getHeaders();
    }

    public T getBody() {
        return body;
    }
}
