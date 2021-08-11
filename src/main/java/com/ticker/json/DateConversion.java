package com.ticker.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.ZonedDateTime;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class DateConversion {

    public static class Deserialize extends JsonDeserializer<ZonedDateTime> {

        @Override
        public ZonedDateTime deserialize(JsonParser jp, DeserializationContext context) throws IOException {
            try {
                String dateString = jp.getText();
                return ZonedDateTime.parse(dateString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
