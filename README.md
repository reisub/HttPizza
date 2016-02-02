# HttPizza

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/reisub/maven/httpizza/images/download.svg) ](https://bintray.com/reisub/maven/httpizza/_latestVersion)

Lightweight http client using [HttpURLConnection](http://developer.android.com/intl/es/reference/java/net/HttpURLConnection.html) under the hood.
The primary use case is for libraries and SDKs which don't need/want all the features ([and methods!](http://www.methodscount.com/?lib=com.squareup.okhttp3%3Aokhttp%3A3.0.1)) OkHttp/Retrofit provide.

It has the same limitations as HttpURLConnection like not supporting the PATCH method and not permitting request body in DELETE requests.

## Usage

Add the library as a dependency to your ```build.gradle``` to automatically download it from jcenter.

```groovy
compile 'sexy.code:httpizza:0.1.0'
```

We also maintain a [changelog](CHANGELOG.md).

### Create a client

```java
HttPizza client = new HttPizza();
```

### GET request

```java
Request request = client.newRequest()
        .url(url)
        .get()
        .build();

Response<String> response = client.newCall(request).execute();
```

### POST request

```java
Request request = client.newRequest()
        .url(url)
        .post("requestBody", String.class)
        .build();

Response<String> response = client.newCall(request).execute();
```

### Async GET request

```java
Request request = client.newRequest()
        .url(url)
        .build();

client.newCall(request, String.class).enqueue(new Callback<String>() {
    @Override
    public void onResponse(Response<String> response) {
        Timber.d(response.getBody());
    }

    @Override
    public void onFailure(Throwable t) {
        Timber.e(t.getMessage());
    }
});
```

## Credits

HttPizza reuses parts of [OkHttp](https://github.com/square/okhttp) and is largely based on [lighthttp](https://github.com/satorufujiwara/lighthttp).

It also draws inspiration from [OkHttp](https://github.com/square/okhttp) and [Retrofit](https://github.com/square/retrofit) APIs.

## Contributing

Feedback and code contributions are very much welcome. Just make a pull request with a short description of your changes. By making contributions to this project you give permission for your code to be used under the same [license](LICENSE).
