package com.healthdata.web.util;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom Gson adapter to serialize/deserialize java.time.LocalDateTime
 * This avoids the reflection errors caused by the Java Module System (JPMS).
 */
public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    // Use the standard ISO format
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
        // Convert LocalDateTime to a standard string
        return new JsonPrimitive(FORMATTER.format(localDateTime));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        // Convert a string back to LocalDateTime
        return LocalDateTime.parse(json.getAsString(), FORMATTER);
    }
}