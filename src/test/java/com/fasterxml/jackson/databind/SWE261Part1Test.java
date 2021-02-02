package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class SWE261Part1Test extends BaseTest {

    final JsonMapper MAPPER = JsonMapper.builder().build();

    static class Avenger {
        public String name;
        public int age;
    }

    public void testValidJSONString() {
        ObjectMapper mapper = new ObjectMapper();
        Avenger avenger = mapper.readValue("{\"name\":\"Captain America\", \"age\":102}", Avenger.class);
        assertEquals("Captain America", avenger.name);
        assertEquals(102, avenger.age);
    }

    public void testInvalidJSONString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Avenger avenger = mapper.readValue("{\"name\":}", Avenger.class);
            fail("Should not have passed");
        } catch (StreamReadException e) {
            verifyException(e, "Unexpected character ('}' (code 125))");
        }
    }

    public void testEmptyJSONString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Avenger avenger = mapper.readValue("", Avenger.class);
            fail("Should not have passed");
        } catch (MismatchedInputException e) {
            verifyException(e, "No content to map due to end-of-input");
        }
    }

    public void testNotEnoughFields() {
        Avenger avenger = MAPPER.readerFor(Avenger.class).readValue("{\"name\":\"Captain America\"}");
        assertEquals("Captain America", avenger.name);
        assertEquals(0, avenger.age);
        avenger = MAPPER.readerFor(Avenger.class).readValue("{\"age\":102}");
        assertNull(avenger.name);
        assertEquals(102, avenger.age);
    }

    public void testNotMoreFields() {
        try {
            Avenger avenger = MAPPER.readerFor(Avenger.class).readValue(
                    "{\"weapon\":\"Shield\"}"
                    );
            fail("Should not have passed");
        } catch (UnrecognizedPropertyException e) {
            verifyException(e, "Unrecognized property \"weapon\"");
        }
    }
}
