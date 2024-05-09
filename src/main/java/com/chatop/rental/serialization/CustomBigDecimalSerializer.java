package com.chatop.rental.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Custom serializer for Double values.
 * Used for surface and price who are in double type. 
 */
public class CustomBigDecimalSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            // Check if the BigDecimal value is a whole number
            BigDecimal scaled = value.stripTrailingZeros();  // Removes unnecessary zero digits
            if (scaled.scale() <= 0 || scaled.compareTo(new BigDecimal(scaled.toBigInteger())) == 0) {
                // It's a whole number, write as integer
                gen.writeNumber(scaled.toBigInteger());
            } else {
                // Not a whole number, write as BigDecimal
                gen.writeNumber(value);
            }
        }
    }
}