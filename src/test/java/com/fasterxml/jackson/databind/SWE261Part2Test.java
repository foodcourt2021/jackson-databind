package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class SWE261Part2Test extends BaseTest {
    final JsonMapper MAPPER = JsonMapper.builder().build();

    static class Avenger {
        public String name;
        public int age;
    }

    /*
     * This test covers path S0 -> S1 -> S6
     */
    public void testMissingLeftBracket() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Avenger avenger = mapper.readValue("\"name\":\"Captain America\"}", Avenger.class);
            fail("Should not have passed");
        } catch (MismatchedInputException e) {
            System.out.println(e.getClass().getName());
            verifyException(e, "no String-argument constructor/factory method to deserialize from String value");
        }
    }

    /*
     * This test covers path S0 -> S1 -> S2 -> S6
     * Missing property
     */
    public void testMissingProperty() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Avenger avenger = mapper.readValue("{:\"Captain America\"}", Avenger.class);
            fail("Should not have passed");
        } catch (StreamReadException e) {
            System.out.println(e.getClass().getName());
            verifyException(e, "was expecting double-quote to start property name");
        }
    }

    /*
     * This test covers path S0 -> S1 -> S2 -> S6
     * Invalid Property
     */
    public void testInValidProperty() {
        try {
            Avenger avenger = MAPPER.readerFor(Avenger.class).readValue(
                    "{\"weapon\":\"Shield\"}"
            );
            fail("Should not have passed");
        } catch (UnrecognizedPropertyException e) {
            verifyException(e, "Unrecognized property \"weapon\"");
        }
    }

    /*
     * This test covers path S0 -> S1 -> S2 -> S3 -> S6
     * Missing value
     */
    public void testMissingValue() {
        try {
            Avenger avenger = MAPPER.readerFor(Avenger.class).readValue(
                    "{\"name\":}"
            );
            fail("Should not have passed");
        } catch (StreamReadException e) {
            verifyException(e, "expected a valid value");
        }
    }

    /*
     * This test covers path S0 -> S1 -> S2 -> S3 -> S6
     * Invalid value
     */
    public void testInValidValue() {
        try {
            Avenger avenger = MAPPER.readerFor(Avenger.class).readValue(
                    "{\"name\": [1,2,3] }"
            );
            fail("Should not have passed");
        } catch (MismatchedInputException e) {
            verifyException(e, "Cannot deserialize value of type `java.lang.String` from Array value");
        }
    }

    /*
     * This test covers path S0 -> S1 -> S2 -> S3 -> S4 -> S6
     * Invalid end char
     */
    public void testInValidEnd() {
        try {
            Avenger avenger = MAPPER.readerFor(Avenger.class).readValue(
                    "{\"name\": \"Captain America\" ]"
            );
            fail("Should not have passed");
        } catch (StreamReadException e) {
            verifyException(e, "Unexpected close marker ']'");
        }
    }

    /*
     * This test covers path S0 -> S1 -> S2 -> S3 -> S4 -> S2
     * Invalid value
     */
    public void testTwoProperties() {
        Avenger avenger = MAPPER.readerFor(Avenger.class).readValue(
                "{\"name\": \"Captain America\",\"age\": 102}"
        );
        assertEquals("Captain America", avenger.name);
        assertEquals(102, avenger.age);
    }

    /*
     * This test covers path S0 -> S1 -> S2 -> S3 -> S4 -> S5
     * Valid End char '}'
     */
    public void testValidEnd() {
        Avenger avenger = MAPPER.readerFor(Avenger.class).readValue(
                "{\"name\": \"Captain America\"}"
        );
        assertEquals("Captain America", avenger.name);
        assertEquals(0, avenger.age);
    }

}
