package com.fasterxml.jackson.failing;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import com.fasterxml.jackson.databind.*;

public class NullConversions2458Test extends BaseMapTest
{
    static class Pojo {
        List<String> list;

        @JsonCreator
        public Pojo(@JsonProperty("list") List<String> list) {
            this.list = Objects.requireNonNull(list, "list");
        }

        public List<String> getList() {
            return list;
        }
    }

    public void testNullsViaCreator() throws Exception {
        ObjectMapper mapper = newJsonMapper();
        mapper.setDefaultSetterInfo(JsonSetter.Value.construct(Nulls.AS_EMPTY,
                Nulls.AS_EMPTY));
        Pojo pojo = mapper.readValue("{}", Pojo.class);
        assertNotNull(pojo);
    }
}