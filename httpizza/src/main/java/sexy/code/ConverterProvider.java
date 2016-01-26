package sexy.code;

import java.io.IOException;
import java.lang.reflect.Type;

public abstract class ConverterProvider {

    protected abstract <T> RequestConverter<T> requestConverter(Type type);

    protected abstract <T> ResponseConverter<T> responseConverter(Type type);

    public interface RequestConverter<T> {

        RequestBody convert(T origin) throws IOException;
    }

    public interface ResponseConverter<T> {

        T convert(ResponseBody body) throws IOException;
    }
}
