package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.cfg.DeserializationContexts;
import com.fasterxml.jackson.databind.deser.DeserializationContextExt;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SWE261Part5TestMock extends BaseTest {

    // reference: https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#verify-T-
    // Important gotcha on spying real objects!

    // https://stackoverflow.com/questions/7803944/how-to-mock-private-method-for-testing-using-powermock

    // ObjectMapperTest.test_readValue_JsonParser

//    private final JsonMapper MAPPER = JsonMapper.builder()
//            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
//            .build();

    private final JsonMapper MAPPER = new JsonMapper();

    @Test
    public void testMock() {
        JsonMapper spyMapper = Mockito.spy(MAPPER);

        Mockito.doReturn(JsonToken.END_ARRAY).when(spyMapper)._initForReading(any(), any());

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.nextToken()).thenReturn(null);
        when(jsonParser.currentToken()).thenReturn(JsonToken.valueOf("START_ARRAY"));

        // DeserializationContextExt ctxt = mock(DeserializationContextExt.class);
        // Mockito.doReturn(true).when(ctxt).isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

        DeserializationContextExt ctxt = spyMapper._deserializationContext();
        assertEquals(null, spyMapper._readValue(spyMapper._deserializationContext(), jsonParser, SimpleType.constructUnsafe(String.class)));
        verify(spyMapper, times(1))._initForReading(jsonParser, SimpleType.constructUnsafe(String.class));
    }

}
