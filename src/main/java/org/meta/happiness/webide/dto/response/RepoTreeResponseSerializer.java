package org.meta.happiness.webide.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;


public class RepoTreeResponseSerializer extends JsonSerializer<RepoTreeResponse> {
    public static final String DELIMITER = "/";
    public static final String EXTENSION_SEPARATOR = ".";
    @Override
    public void serialize(RepoTreeResponse value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartObject();

        gen.writeStringField("id", String.valueOf(value.getId()));
        gen.writeStringField("key", value.getKey());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("parentId", value.getParentId());

        if (!value.getName().contains(EXTENSION_SEPARATOR)) {
            gen.writeStringField("type", "internal");
            gen.writeFieldName("children");
            gen.writeStartArray();

            for (RepoTreeResponse child : value.getChildren()) {
                gen.writeObject(child);
            }
            gen.writeEndArray();
        } else {
            if (value.getContent() != null) {
                gen.writeStringField("content", value.getContent());
                gen.writeStringField("uuid", value.getUuid());
                gen.writeStringField("type", "leaf");
            }
        }
        gen.writeEndObject();
    }
}
