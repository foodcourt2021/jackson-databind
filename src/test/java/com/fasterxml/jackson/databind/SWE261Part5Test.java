package com.fasterxml.jackson.databind;

public class SWE261Part5Test extends BaseTest {

    static class Avenger {
        public String heroName;
    }

    // Example from TestNamingStrategyStd.java
    public void testKebabCaseWithSWE261() {
        Avenger avenger = new Avenger();
        avenger.heroName = "Captain America";
        PropertyNamingStrategies.setKebabCase(new PropertyNamingStrategies.KebabCaseStrategyWithSWE261());
        ObjectMapper m = jsonMapperBuilder()
                .propertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE)
                .build();
        assertEquals(aposToQuotes("{'hero-name-swe261':'Captain America'}"), m.writeValueAsString(avenger));

        avenger = m.readValue(aposToQuotes("{'hero-name-swe261':'Steve Rogers'}"),
                Avenger.class);
        assertEquals("Steve Rogers", avenger.heroName);

        // Change it back
        PropertyNamingStrategies.setKebabCase(new PropertyNamingStrategies.KebabCaseStrategy());
        m = jsonMapperBuilder()
                .propertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE)
                .build();
        assertEquals(aposToQuotes("{'hero-name':'Steve Rogers'}"), m.writeValueAsString(avenger));
    }
}
