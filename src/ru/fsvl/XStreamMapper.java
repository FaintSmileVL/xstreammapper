package ru.fsvl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.Collection;

/**
 * Author Nami
 * Date: 18.10.2022.
 * Time: 23:38
 */
public class XStreamMapper {
    private static volatile XStreamMapper instance;

    public static XStreamMapper getInstance() {
        XStreamMapper localInstance = instance;
        if (localInstance == null) {
            synchronized (XStreamMapper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new XStreamMapper();
                }
            }
        }
        return localInstance;
    }

    /**
     * Получает на вход полный путь к директории, имя файла и класс файла,
     * преобразовывает эти данные в объект
     * @param path
     * @param fileName
     * @param tClass
     * @param <T>
     * @return
     * @throws IOException
     * @throws NoSuchFileException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public <T> T toObject(String path, String fileName, Class<T> tClass) throws IOException, IllegalAccessException, InstantiationException {
        File file = new File(path, fileName);
        if (!file.exists()) {
            throw new NoSuchFileException(fileName + ": no such file or directory.");
        }
        return toObject(path, fileName, tClass.newInstance());
    }

    /**
     * Получает на вход полный путь к директории и имя файла,
     * преобразовывает эти данные в объект
     * @param path
     * @param fileName
     * @param data
     * @param <T>
     * @return
     * @throws IOException
     * @throws NoSuchFileException
     */
    public <T> T toObject(String path, String fileName, T data) throws IOException {
        File file = new File(path, fileName);
        if (!file.exists()) {
            throw new NoSuchFileException(fileName + ": no such file or directory.");
        }
        return toObject(file, data);
    }

    /**
     * Преобразовывает содержимое файла в объект
     * @param file
     * @param data
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T> T toObject(File file, T data) throws IOException {
        return toObject(readFileAsString(file), data);
    }

    /**
     * Преобразовывает содержимое строки в объект
     * @param xml
     * @param data
     * @param <T>
     * @return
     */
    public <T> T toObject(String xml, T data) {
        return (T) getXStream(data).fromXML(xml, data);
    }

    private <T> XStream getXStream(T data) {
        XStream xStream = new XStream();
        xStream.processAnnotations(data.getClass());
        xStream.allowTypeHierarchy(Collection.class);
        xStream.addPermission(NullPermission.NULL);
        xStream.addPermission(PrimitiveTypePermission.PRIMITIVES);
        xStream.allowTypesByWildcard(new String[] {
                "ru.catssoftware.xstream.**"
        });
        return xStream;
    }

    private String readFileAsString(File file) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(file));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    public <T> void write(T data, String path, String fileName) throws FileNotFoundException {
        File file = new File(path, fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        write(data, fileOutputStream, new char[] { '\t' });
    }

    public <T> void write(T data, OutputStream outputStream) {
        write(data, outputStream, new char[] { '\t' });
    }

    public <T> void write(T data, OutputStream outputStream, char[] indent) {
        XStream xstream = new XStream();
        xstream.processAnnotations(data.getClass());
        xstream.marshal(data,
                new PrettyPrintWriter(
                        new OutputStreamWriter(
                                new BufferedOutputStream(outputStream)
                        ), indent
                )
        );
    }

    public <T> T toXML(T data) {
        XStream xs = new XStream();
        xs.processAnnotations(data.getClass());
        return (T) xs.toXML(data);
    }
}
