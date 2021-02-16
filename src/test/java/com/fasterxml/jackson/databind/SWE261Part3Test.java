package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.deser.TestGenerics;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

public class SWE261Part3Test extends BaseTest {
    final JsonMapper MAPPER = JsonMapper.builder().build();

    static class Avenger {
        public String name;
        public int age;
    }

    private ObjectMapper mapper = new ObjectMapper();
    private final String content = "{\"name\": \"Captain America\"}";
    private Avenger avenger = null;

    private void check() {
        assertNotNull(avenger);
        assertEquals(Avenger.class, avenger.getClass());
        assertEquals("Captain America", avenger.name);
    }

    @Test
    public void testReadValueWithReaderAndTypeReference() {
        avenger = mapper.readValue(new StringReader(content), new TypeReference<Avenger>() {});
        check();
    }

    @Test
    public void testReadValueWithReaderAndJavaType() {
        JavaType type = mapper.getTypeFactory().constructType(Avenger.class);
        avenger = mapper.readValue(new StringReader(content), type);
        check();
    }

    @Test
    public void testReadValueWithInputStreamAndTypeReference() {
        InputStream targetStream = new ByteArrayInputStream(content.getBytes());
        avenger = mapper.readValue(targetStream, new TypeReference<Avenger>() {});
        check();
    }

    @Test
    public void testReadValueWithInputStreamAndJavaType() {
        JavaType type = mapper.getTypeFactory().constructType(Avenger.class);
        InputStream targetStream = new ByteArrayInputStream(content.getBytes());
        avenger = mapper.readValue(targetStream, type);
        check();
    }

    @Test
    public void testReadValueWithByteArrayAndLengthAndClass() {
        byte[] bytes = content.getBytes();
        avenger = mapper.readValue(bytes, 0, bytes.length, Avenger.class);
        check();
        avenger = mapper.readValue(bytes, 0, bytes.length, new TypeReference<Avenger>() {});
        check();
        JavaType type = mapper.getTypeFactory().constructType(Avenger.class);
        avenger = mapper.readValue(bytes, type);
        check();
        DataInput input = new DataInputStream(new ByteArrayInputStream(bytes));
        avenger = mapper.readValue(input, type);
        check();
    }

    @Test
    public void testReadValueWithFile() {
        File file = new File("src/test/resources/data/avenger.json");
        avenger = mapper.readValue(file, Avenger.class);
        check();
        avenger = mapper.readValue(file, new TypeReference<Avenger>() {});
        check();
        JavaType type = mapper.getTypeFactory().constructType(Avenger.class);
        avenger = mapper.readValue(file, type);
        check();
    }

    @Test
    public void testReadValueWithURL() throws MalformedURLException {
        URL url = new URL("file://"+System.getProperty("user.dir")+"/src/test/resources/data/avenger.json");
        avenger = mapper.readValue(url, Avenger.class);
        check();
        avenger = mapper.readValue(url, new TypeReference<Avenger>() {});
        check();
        JavaType type = mapper.getTypeFactory().constructType(Avenger.class);
        avenger = mapper.readValue(url, type);
        check();
    }

}
