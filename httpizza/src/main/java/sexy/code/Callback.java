package sexy.code;

public interface Callback {

    void onResponse(Response response);

    void onFailure(Throwable t);
}
