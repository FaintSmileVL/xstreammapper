package ru.fsvl;

import java.io.BufferedOutputStream;

/**
 * Author Nami
 * Date: 19.10.2022.
 * Time: 14:18
 */
public interface IXStreamWriter {
    default <T> void write(T data, BufferedOutputStream bufferedOutputStream, String path, String name) throws Exception {
        XStreamMapper
                .getInstance()
                .write(data, bufferedOutputStream);
    }

    default <T> void write(T data, String path, String name) throws Exception {
        XStreamMapper
                .getInstance()
                .write(data, new BufferedOutputStream(System.out));
    }

    default <T> void write(Class<T> data, BufferedOutputStream bufferedOutputStream, String path, String name) throws Exception {
        XStreamMapper
                .getInstance()
                .write(data, bufferedOutputStream);
    }

    default <T> void write(Class<T> data, String path, String name) throws Exception {
        XStreamMapper
                .getInstance()
                .write(data, new BufferedOutputStream(System.out));
    }
}
