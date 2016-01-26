package sexy.code;

import java.net.HttpURLConnection;

public interface ConnectionListener {

    void onPreConnect(Request request, HttpURLConnection connection);

    void onPostConnect(Request request, HttpURLConnection connection);
}
