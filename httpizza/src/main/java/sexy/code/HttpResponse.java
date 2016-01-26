package sexy.code;

import java.util.List;
import java.util.Map;

public class HttpResponse {

    private final Request request;

    private final int code;

    private final Map<String, List<String>> headers;

    private final ResponseBody body;

    HttpResponse(final Request request, final int code, final Map<String, List<String>> headers,
            final ResponseBody body) {
        this.request = request;
        this.code = code;
        this.headers = headers;
        this.body = body;
    }

    public Request getRequest() {
        return request;
    }

    public int getCode() {
        return code;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public ResponseBody getBody() {
        return body;
    }

    public boolean isSuccess() {
        return code / 100 == 2;
    }

}
