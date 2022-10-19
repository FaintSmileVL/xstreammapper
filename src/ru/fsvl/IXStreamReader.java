package ru.fsvl;

/**
 * Author Nami
 * Date: 19.10.2022.
 * Time: 11:49
 */
public interface IXStreamReader {
    default <T> T parse(T data, String path, String name) throws Exception {
        T result = XStreamMapper
                .getInstance()
                .toObject(path, name, data);
        return result;
    }

    default <T> T parse(Class<T> data, String path, String name) throws Exception {
        T result = XStreamMapper
                .getInstance()
                .toObject(path, name, data);
        return result;
    }
}
