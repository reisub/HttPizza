package sexy.code;


import java.io.IOException;

class RequestConvertTask<T> {

    private T obj;

    private ConverterProvider.RequestConverter<T> converter;

    RequestConvertTask(T obj, ConverterProvider.RequestConverter<T> converter) {
        this.obj = obj;
        this.converter = converter;
    }

    RequestBody execute() throws IOException {
        return converter.convert(obj);
    }

}
