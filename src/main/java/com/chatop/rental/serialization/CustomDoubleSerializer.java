package com.chatop.rental.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Custom serializer for Double values.
 * Used for surface and price who are in double type. 
 */
public class CustomDoubleSerializer extends JsonSerializer<Double> {
    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            if (value % 1 == 0) {
                gen.writeNumber(value.intValue());
            } else {
                gen.writeNumber(value);
            }
        }
    }
}