package sexy.code;

import java.util.List;
import java.util.Map;

public class Response {

    private final ResponseBody body;

    private final Request request;

    private final int code;

    private final String message;

    private final Map<String, List<String>> headers;

    Response(final Request request, final int code, String message, final Map<String, List<String>> headers, final ResponseBody body) {
        this.request = request;
        this.code = code;
        this.message = message;
        this.headers = headers;
        this.body = body;
    }

    public Request getRequest() {
        return request;
    }

    public boolean isSuccessful() {
        return (code / 100) == 2;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public ResponseBody body() {
        return body;
    }
}
